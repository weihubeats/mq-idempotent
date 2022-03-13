package com.mq.idempotent.core.aop;

import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.exception.MessageConcurrencyException;
import com.mq.idempotent.core.strategy.IdempotentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author : wh
 * @date : 2021/12/21 18:51
 * @description:
 */
@Slf4j
public class MqIdempotentAnnotationInterceptor implements MethodInterceptor {

    private final IdempotentStrategy idempotentStrategy;

    public MqIdempotentAnnotationInterceptor(IdempotentStrategy idempotentStrategy) {
        this.idempotentStrategy = idempotentStrategy;
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
        String key = idempotentStrategy.getUniqueKey(Arrays.stream(args).findFirst().orElseThrow(() -> new Exception("仅支持第一个消息为Message")), annotation.field());
        if (log.isDebugEnabled()) {
            log.info("唯一key {}", key);
        }
        if (idempotentStrategy.exitKey(key)) {
            log.warn("重复消费 {}", key);
            return isVoid ? null : true;
        }
        if (!idempotentStrategy.lock(key)) {
            log.info("有消息正在消费");
            throw new MessageConcurrencyException("有消息正在消费");
        }
        return proceed(methodInvocation, key);
    }

    public Object proceed(MethodInvocation methodInvocation, String key) {
        try {
            Object proceed = methodInvocation.proceed();
            idempotentStrategy.save(key);
            return proceed;
        } catch (Throwable throwable) {
            log.error("throwable ", throwable);
            throw new RuntimeException(throwable);
        } finally {
            idempotentStrategy.unlock(key);
        }
    }

}
