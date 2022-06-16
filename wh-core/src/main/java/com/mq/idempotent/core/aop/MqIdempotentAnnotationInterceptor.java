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

import com.mq.idempotent.core.alert.strategy.AlertStrategy;
import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.exception.MessageConcurrencyException;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
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

    private final AbstractIdempotentStrategy idempotentStrategy;

    private final AlertStrategy alertStrategy;

    public MqIdempotentAnnotationInterceptor(AbstractIdempotentStrategy idempotentStrategy, AlertStrategy alertStrategy) {
        this.idempotentStrategy = idempotentStrategy;
        this.alertStrategy = alertStrategy;
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
        String key = idempotentStrategy.getUniqueKey(Arrays.stream(args).findFirst().orElseThrow(() -> new Exception("去重第一个参数不能为空")), annotation.field(), method, args);
        if (log.isDebugEnabled()) {
            log.info("唯一key {}", key);
        }
        if (idempotentStrategy.exitKey(key)) {
            log.warn("重复消费 {}", key);
            return isVoid ? null : true;
        }
        if (!idempotentStrategy.lock(key)) {
            log.info("有消息正在消费");
            // 抛出异常依赖mq自动重试
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
            // 监控
            alertStrategy.sendMsg("");
            log.error("throwable ", throwable);
            throw new RuntimeException(throwable);
        } finally {
            idempotentStrategy.unlock(key);
        }
    }

}
