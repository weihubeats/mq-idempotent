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

package com.samples.config;

import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.samples.consumer.MessageEventHandler;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2021/11/8 10:55
 * @description:
 */
@Configuration
@Slf4j
public class AliyunMQConfig {
    
    @Value("${alimq.accessKey:test}")
    private String aclAccessKey;
    
    @Value("${alimq.accessKey:test}")
    private String aclAccessSecret;
    
    @Value("${alimq.orderActionTopicNameSerAddr:test}")
    private String orderNameSerAddr;
    
    @Autowired
    MessageEventHandler handler;
    
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Consumer consumer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, "");
        properties.put(PropertyKeyConst.SecretKey, "");
        properties.put(PropertyKeyConst.NAMESRV_ADDR, "");
        properties.put(PropertyKeyConst.MaxReconsumeTimes, 20);
        properties.put(PropertyKeyConst.GROUP_ID, "");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("topic", "*", (message, context) -> handler.consumer(message)
                ? Action.CommitMessage
                : Action.ReconsumeLater);
        return consumer;
        
    }
    
}
