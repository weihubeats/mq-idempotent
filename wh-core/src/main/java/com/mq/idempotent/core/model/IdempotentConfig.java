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

package com.mq.idempotent.core.model;

import com.mq.idempotent.core.config.IdempotentProperties;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2021/11/15 11:07
 * @description:
 */
@Data
public class IdempotentConfig {

    /**
     * 去重key 前缀
     */
    private String uniqueKeyPrefix;

    /**
     * 去重值
     */
    private String uniqueValue;

    private String recordKeySuffix;

    /**
     * 并发获取锁等待时间 TimeUnit.SECONDS
     */
    private Long tryLockTime;
    /**
     * 并发获取锁等待时间单位
     */
    private TimeUnit tryLockTimeUnit = TimeUnit.SECONDS;

    /**
     * 存放已消费消息时间，过期则删除。
     */
    private Long keyTimeOut;

    /**
     * 消费key存放redis时间单位
     */
    private TimeUnit timeOutTimeUnit = TimeUnit.DAYS;

    /**
     * 报警策略
     */
    private String alertName;


    public void initConfig(IdempotentProperties properties) {
        this.uniqueKeyPrefix = properties.getUniqueKeyPrefix();
        this.uniqueValue = properties.getUniqueValue();
        this.tryLockTime = properties.getTryLockTime();
        this.keyTimeOut = properties.getKeyTimeOut();
        this.recordKeySuffix = properties.getRecordKeySuffix();
    }


}
