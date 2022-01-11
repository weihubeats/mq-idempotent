package com.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.constants.RabbitMQConstants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
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

    @GetMapping("/convert/send")
    public String send(String queueName) {
        Map<String, Object> map = getMessageMap();
        rabbitTemplate.convertAndSend(RabbitMQConstants.TEST_DIRECT_EXCHANGE, RabbitMQConstants.ROUTING_KEY, map);
        return "success";

    }

    @GetMapping("/sendMsg")
    public String sendMessage() {
        Map<String, Object> map = getMessageMap();
        // 指定消息类型
        MessageProperties props = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        Message message = new Message( JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8), props);
        rabbitTemplate.send(RabbitMQConstants.TEST_DIRECT_EXCHANGE, RabbitMQConstants.ROUTING_KEY,message);
        return "success";
    }

    public Map<String, Object> getMessageMap() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        return map;
    }

}
