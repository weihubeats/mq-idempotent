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

package com.rabbitmq.controller;

import com.rabbitmq.constants.RabbitMQConstants;
import com.rabbitmq.message.TestMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : wh
 * @date : 2022/1/11 19:39
 * @description:
 */
@RestController
public class SendMessageController {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /*
     * @GetMapping("/convert/send") public String send(String queueName) { Map<String, Object> map = getMessageMap(); rabbitTemplate.convertAndSend(RabbitMQConstants.TEST_DIRECT_EXCHANGE,
     * RabbitMQConstants.ROUTING_KEY, map); return "success";
     * 
     * }
     */
    
    @GetMapping("/convert/send")
    public String send1(String queueName) {
        TestMessage testMessage = new TestMessage();
        testMessage.setId(1);
        testMessage.setMsg("test message, hello");
        rabbitTemplate.convertAndSend(RabbitMQConstants.TEST_DIRECT_EXCHANGE, RabbitMQConstants.ROUTING_KEY, testMessage);
        return "success";
        
    }
    
    /*
     * @GetMapping("/sendMsg") public String sendMessage() { Map<String, Object> map = getMessageMap(); // 指定消息类型 MessageProperties props = MessagePropertiesBuilder.newInstance()
     * .setContentType(MessageProperties.CONTENT_TYPE_JSON) .build(); Message message = new Message( JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8), props);
     * rabbitTemplate.send(RabbitMQConstants.TEST_DIRECT_EXCHANGE, RabbitMQConstants.ROUTING_KEY,message); return "success"; }
     */
    
    public Map<String, Object> getMessageMap() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        return map;
    }
    
}
