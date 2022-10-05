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

package com.rabbitmq.consumer;

import com.mq.idempotent.core.annotation.Idempotent;
import com.rabbitmq.constants.RabbitMQConstants;
import com.rabbitmq.message.TestMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2022/1/11 19:43
 * @description:
 */
@Component
public class RabbitMQConsumer {
    
    /*
     * @RabbitHandler
     * 
     * @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)
     * 
     * @Idempotent public void process(Map testMessage) { System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString()); }
     */
    
    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)
    @Idempotent(field = "id")
    public void consumerTestMessage(TestMessage testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }
    
}
