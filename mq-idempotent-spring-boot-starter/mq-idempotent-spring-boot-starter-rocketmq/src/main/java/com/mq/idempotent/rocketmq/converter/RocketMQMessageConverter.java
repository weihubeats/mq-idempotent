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

package com.mq.idempotent.rocketmq.converter;

import java.lang.reflect.Method;

import com.mq.idempotent.core.aop.MessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;

import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2021/11/15 14:29
 * @description:
 */
@Component
public class RocketMQMessageConverter implements MessageConverter<MessageExt> {
    
    @Override
    public String getUniqueKey(MessageExt messageExt, String field, Method method, Object[] args) {
        return StringUtils.isNoneBlank(messageExt.getKeys()) ? messageExt.getKeys() : messageExt.getMsgId();
    }
}
