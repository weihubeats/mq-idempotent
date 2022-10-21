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

package com.mq.idempotent.aliyun.rocketmq.converter;

import java.lang.reflect.Method;

import com.aliyun.openservices.ons.api.Message;
import com.mq.idempotent.core.aop.MessageConverter;

import org.springframework.util.ObjectUtils;

/**
 * @author : wh
 * @date : 2021/11/15 14:00
 * @description:
 */
public class AliYunRocketMQMessageConverter implements MessageConverter<Message> {
    
    @Override
    public String getUniqueKey(Message message, String field, Method method, Object[] args) {
        String messageKey = message.getKey();
        return !ObjectUtils.isEmpty(messageKey) ? messageKey : message.getMsgID();
    }
    
    public static void main(String[] args) {
        System.out.println();
    }
}
