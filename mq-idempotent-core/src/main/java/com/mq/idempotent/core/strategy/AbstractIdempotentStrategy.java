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

package com.mq.idempotent.core.strategy;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.model.IdempotentConfig;
import lombok.AllArgsConstructor;

/**
 * @author : wh
 * @date : 2022/3/12 18:07
 * @description:
 */
@AllArgsConstructor
public abstract class AbstractIdempotentStrategy implements IdempotentStrategy {
    
    private final IdempotentConfig idempotentConfig;
    
    private final MessageConverter messageConverter;
    
    public Long getTryLockTime() {
        return idempotentConfig.getTryLockTime();
    }
    
    public TimeUnit getTryLockTimeUnit() {
        return idempotentConfig.getTryLockTimeUnit();
    }
    
    public String getUniqueKey(Object o, String field, Method method, Object[] args) {
        String uniqueKeyPrefix = idempotentConfig.getUniqueKeyPrefix();
        return uniqueKeyPrefix + messageConverter.getUniqueKey(o, field, method, args);
    }
    
    public IdempotentConfig getIdempotentConfig() {
        return idempotentConfig;
    }
    
    public String getUniqueValue() {
        return idempotentConfig.getUniqueValue();
    }
    
    public Long getKeyTimeOut() {
        return idempotentConfig.getKeyTimeOut();
    }
    
    public TimeUnit getTimeOutTimeUnit() {
        return idempotentConfig.getTimeOutTimeUnit();
    }
    
    public String getWebHook() {
        return idempotentConfig.getWebHook();
    }
    
}
