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
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author : wh
 * @date : 2022/3/12 18:05
 * @description:
 */
public class RedisIdempotentStrategy extends AbstractIdempotentStrategy {

    private final RedissonClient redissonClient;

    public RedisIdempotentStrategy(IdempotentConfig idempotentConfig, MessageConverter<?> messageConverter, RedissonClient redissonClient) {
        super(idempotentConfig, messageConverter);
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean lock(String lockName) {
        RLock stockLock = redissonClient.getLock(lockName);
        try {
            return stockLock.tryLock(getTryLockTime(), getTryLockTimeUnit());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save(String uniqueKey) {
        final String recordKey = getRecordKey(uniqueKey);
        RBucket<String> bucket = redissonClient.getBucket(recordKey);
        bucket.set(getUniqueValue(), getKeyTimeOut(), getTimeOutTimeUnit());
    }

    @Override
    public void unlock(String lockName) {
        RLock stockLock = redissonClient.getLock(lockName);
        stockLock.unlock();
    }

    @Override
    public boolean exitKey(String key) {
        final String recordKey = getRecordKey(key);
        RBucket<String> record = redissonClient.getBucket(recordKey);
        final RBucket<Object> currentLock = redissonClient.getBucket(key);

        return record.isExists() || currentLock.isExists();
    }

    private String getRecordKey(String uniqueKey){
        return uniqueKey + "_" + getIdempotentConfig().getRecordKeySuffix();
    }
}
