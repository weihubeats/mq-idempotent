package com.samples.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author : wh
 * @date : 2021/11/8 11:52
 * @description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    DefaultMQProducer defaultMQProducer;

    @GetMapping("/send")
    public void test() throws Exception{
        System.out.println("进来了");
        /*Message message = new Message();
        message.setBody("test message".getBytes(StandardCharsets.UTF_8));
        message.setTopic("domain_event");
        message.setKey("test key");*/
        Message message = new Message();
        message.setBody("test message".getBytes(StandardCharsets.UTF_8));
        message.setTopic("domain_event");
        message.setKeys("test key");
        defaultMQProducer.send(message);
        System.out.println("发送消息成功 " + JSON.toJSONString(message));
    }

}
