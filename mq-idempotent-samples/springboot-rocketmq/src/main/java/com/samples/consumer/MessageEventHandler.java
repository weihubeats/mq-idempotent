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

package com.samples.consumer;

import com.mq.idempotent.core.annotation.Idempotent;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/15 20:02
 * @description:
 */
@Service
public class MessageEventHandler {
    
    @Autowired
    private MessageEventHandler messageEventHandler;
    
    public void consumer(MessageExt messageExt) {
        if (Objects.equals(messageExt.getTags(), "TagA")) {
            // 防止AOP失效失效
            messageEventHandler.testConsumer(messageExt);
        }
        
    }
    
    @Idempotent
    public void testConsumer(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        System.out.println("消息id " + messageExt.getMsgId());
        System.out.println("消息keys " + messageExt.getKeys());
        System.out.println("消费成功, msg " + msg);
    }
    
}
