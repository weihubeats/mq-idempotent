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

package com.mq.idempotent.core.strategy.impl;

import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.model.IdempotentConfig;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author : wh
 * @date : 2022/3/13 09:46
 * @description:
 */
public class JDBCIdempotentStrategy extends AbstractIdempotentStrategy {
    
    private final JdbcTemplate jdbcTemplate;
    
    public JDBCIdempotentStrategy(IdempotentConfig idempotentConfig, MessageConverter<?> messageConverter, JdbcTemplate jdbcTemplate) {
        super(idempotentConfig, messageConverter);
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public boolean lock(String lockName) {
        return false;
    }
    
    @Override
    public void save(String uniqueKey) {
        
    }
    
    @Override
    public void unlock(String lockName) {
        
    }
    
    @Override
    public boolean exitKey(String key) {
        return false;
    }
}
