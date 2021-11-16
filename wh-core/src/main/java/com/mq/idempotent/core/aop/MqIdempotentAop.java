package com.mq.idempotent.core.aop;

import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.model.IdempotentConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2021/11/12 11:18
 * @description:
 */
@Aspect
@Slf4j
@Component
public class MqIdempotentAop {


    private final RedissonClient redissonClient;

    private final IdempotentConfig idempotentConfig;

    private final MessageConverter messageConverter;


    public MqIdempotentAop(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter messageConverter) {
        if (Objects.isNull(redissonClient)) {
            throw new NullPointerException("redissonClient template is null");
        }
        this.redissonClient = redissonClient;
        this.idempotentConfig = idempotentConfig;
        this.messageConverter = messageConverter;
    }


    @Pointcut("@annotation(com.mq.idempotent.core.annotation.Idempotent)")
    public void ciderDistributedLockAspect() {

    }

    @Around(value = "ciderDistributedLockAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        //切点所在的类
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String returnTypeName = method.getReturnType().getName();
        // 方法返回值仅支持 boolean void 返回值
        boolean isVoid = returnTypeName.equalsIgnoreCase("void");
        if (!isVoid && !returnTypeName.equalsIgnoreCase("boolean")) {
            throw new Exception("method returnType is not boolean or void");
        }
        //方法参数
        Object[] args = pjp.getArgs();
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        String msgID = messageConverter.getUniqueKey(Arrays.stream(args).findFirst().orElseThrow(() -> new Exception("参数异常")));
        String key = idempotentConfig.getRedisKey() + msgID;
        if (log.isDebugEnabled()) {
            log.info("唯一key {}", key);
        }
        if (exitKey(key)) {
            log.info("重复消费");
            return isVoid ? null : true;
        }
        if (!lock(key)) {
            log.info("有消息正在消费");
            throw new Exception("有消息正在消费");
        }
        try {
            Object proceed = pjp.proceed();
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(idempotentConfig.getRedisValue());
            return proceed;
        } finally {
            RLock stockLock = redissonClient.getLock(key);
            stockLock.unlock();
        }
    }


    public boolean lock(String lockName) {
        RLock stockLock = redissonClient.getLock(lockName);
        try {
            return stockLock.tryLock(idempotentConfig.getTryLockTime(), TimeUnit.SECONDS);
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
