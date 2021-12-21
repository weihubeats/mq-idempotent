package com.mq.idempotent.core.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author : wh
 * @date : 2021/12/21 18:51
 * @description:
 */
public class MqIdempotentAnnotationInterceptor implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
