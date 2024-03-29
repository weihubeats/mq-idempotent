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

package com.mq.idempotent.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wh
 * @date : 2021/12/30 21:20
 * @description:
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IdempotentProperties.PREFIX)
public class IdempotentProperties {
    
    public static final String PREFIX = "idempotent";
    
    /**
     * 去重key redis名字
     */
    private String uniqueKeyPrefix = "mq:unique:";
    
    /**
     * 去重记录后缀
     */
    private String recordKeySuffix = "Record";
    
    /**
     * redis值
     */
    private String uniqueValue = "s";
    
    /**
     * 并发获取锁等待时间
     */
    private Long tryLockTime = 1L;
    
    /**
     * 消费key存放时间默认3天
     */
    private Long keyTimeOut = 3L;
    
    /**
     * 是否开启并发控制
     */
    private Boolean concurrency = true;
    
    /**
     * 报警 url
     */
    private String webHook;
    
    /**
     * 报警策略
     */
    private String alertName;
    
}
