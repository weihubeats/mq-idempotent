/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mq.idempotent.core.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.mq.idempotent.core.alert.AlertDTO;
import com.mq.idempotent.core.alert.strategy.AlertStrategy;
import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.exception.MessageConcurrencyException;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
import com.mq.idempotent.core.utils.TransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * @author : wh
 * @date : 2021/12/21 18:51
 * @description:
 */
@Slf4j
public class MqIdempotentAnnotationInterceptor implements MethodInterceptor {
    
    private final AbstractIdempotentStrategy idempotentStrategy;
    
    private final AlertStrategy alertStrategy;
    
    private final TransactionUtil transactionUtil;
    
    public MqIdempotentAnnotationInterceptor(AbstractIdempotentStrategy idempotentStrategy, @Autowired(required = false) AlertStrategy alertStrategy, TransactionUtil transactionUtil) {
        this.idempotentStrategy = idempotentStrategy;
        this.alertStrategy = alertStrategy;
        this.transactionUtil = transactionUtil;
    }
    
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();

        // 判断返回值是不是void或者boolean
        final boolean isReturnVoid = method.getReturnType().equals(Void.TYPE);
        final boolean isReturnBoolean = method.getReturnType().equals(Boolean.TYPE);
        if (!isReturnVoid && !isReturnBoolean) {
            throw new Exception("method returnType is not boolean or void");
        }
        //方法参数
        Object[] args = methodInvocation.getArguments();
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        String key = idempotentStrategy.getUniqueKey(Arrays.stream(args).findFirst()
                .orElseThrow(() -> new Exception("去重第一个参数不能为空")), annotation.field(), method, args);
        if (!idempotentStrategy.lock(key)) {
            log.info("有消息正在消费");
            // 抛出异常依赖mq自动重试
            throw new MessageConcurrencyException("有消息正在消费");
        }
        if (log.isDebugEnabled()) {
            log.info("唯一key {}", key);
        }
        if (idempotentStrategy.exitKey(key)) {
            log.warn("重复消费 {}", key);
            return isReturnVoid ? null : true;
        }

        Object res;
        try {
            if (annotation.transactional()) {
                res = transactionUtil.transact(() -> proceed(methodInvocation, key));
            } else {
                res = proceed(methodInvocation, key);
            }
        } finally {
            idempotentStrategy.unlock(key);
        }
        return res;
    }
    
    public Object proceed(MethodInvocation methodInvocation, String key) {
        try {
            Object proceed = methodInvocation.proceed();
            idempotentStrategy.save(key);
            return proceed;
        } catch (Throwable throwable) {
            // 监控
            if (!ObjectUtils.isEmpty(alertStrategy)) {
                alertStrategy.sendMsg(new AlertDTO(key, methodInvocation.getMethod(), throwable, idempotentStrategy.getWebHook()));
            }
            log.error("throwable ", throwable);
            throw new RuntimeException(throwable);
        }
    }
    
}
