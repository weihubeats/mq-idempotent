package com.mq.idempotent.core.aop;

import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.exception.MessageConcurrencyException;
import com.mq.idempotent.core.model.IdempotentConfig;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/12/21 18:51
 * @description:
 */
@Slf4j
public class MqIdempotentAnnotationInterceptor implements MethodInterceptor {

    private final RedissonClient redissonClient;

    private final IdempotentConfig idempotentConfig;

    private final MessageConverter<Object> messageConverter;

    public MqIdempotentAnnotationInterceptor(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter<Object> messageConverter) {
        if (Objects.isNull(redissonClient)) {
            throw new NullPointerException("redissonClient template is null");
        }
        this.redissonClient = redissonClient;
        this.idempotentConfig = idempotentConfig;
        this.messageConverter = messageConverter;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Method method = methodInvocation.getMethod();

        String returnTypeName = method.getReturnType().getName();
        // 方法返回值仅支持 boolean void 返回值
        boolean isVoid = returnTypeName.equalsIgnoreCase("void");
        if (!isVoid && !returnTypeName.equalsIgnoreCase("boolean")) {
            throw new Exception("method returnType is not boolean or void");
        }
        //方法参数
        Object[] args = methodInvocation.getArguments();
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        String msgID = messageConverter.getUniqueKey(Arrays.stream(args).findFirst().orElseThrow(() -> new Exception("仅支持第一个消息为Message")), annotation.fileName());
        String key = idempotentConfig.getRedisKey() + msgID;
        if (log.isDebugEnabled()) {
            log.info("唯一key {}", key);
        }
        if (exitKey(key)) {
            log.warn("重复消费 {}", key);
            return isVoid ? null : true;
        }
        if (!lock(key)) {
            log.info("有消息正在消费");
            throw new MessageConcurrencyException("有消息正在消费");
        }
        return proceed(methodInvocation, key);
    }

    public Object proceed(MethodInvocation methodInvocation, String key) {
        try {
            Object proceed = methodInvocation.proceed();
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(idempotentConfig.getRedisValue(), idempotentConfig.getRedisTimeOut(), idempotentConfig.getRedisTimeOutTimeUnit());
            return proceed;
        } catch (Throwable throwable) {
            log.error("throwable ", throwable);
            throw new RuntimeException(throwable);
        } finally {
            RLock stockLock = redissonClient.getLock(key);
            stockLock.unlock();
        }
    }

    public boolean lock(String lockName) {
        RLock stockLock = redissonClient.getLock(lockName);
        try {
            return stockLock.tryLock(idempotentConfig.getTryLockTime(), idempotentConfig.getTryLockTimeUnit());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exitKey(String key) {
        RBucket<String> stockLock = redissonClient.getBucket(key);
        return stockLock.isExists();
    }
}
