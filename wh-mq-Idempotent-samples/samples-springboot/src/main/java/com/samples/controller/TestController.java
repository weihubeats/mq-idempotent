package com.samples.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.Message;
import com.mq.idempotent.core.annotation.Idempotent;
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
    Test test;



    @GetMapping("/send")
    public void test() throws Exception{
        System.out.println("hahah");
        test.test();


    }

}
