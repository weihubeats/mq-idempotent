package com.samples.config;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.ons.api.impl.rocketmq.OnsClientRPCHook;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.mq.idempotent.core.annotation.Idempotent;
import lombok.extern.slf4j.Slf4j;
import com.aliyun.openservices.ons.api.Message;


import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

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







    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Consumer consumer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, "aliMQAccessKey");
        properties.put(PropertyKeyConst.SecretKey, "aliMQSecretKey");
        properties.put(PropertyKeyConst.NAMESRV_ADDR, orderNameSerAddr);
        properties.put(PropertyKeyConst.MaxReconsumeTimes, 20);
        properties.put(PropertyKeyConst.GROUP_ID, "orderServiceGid");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("orderTopic", "test || test_event", (message, context) ->
                handleClient(message)
                        ? Action.CommitMessage
                        : Action.ReconsumeLater);
        return consumer;

    }

    @Idempotent
    public boolean handleClient(Message message) {
        String tag = message.getTag();
        switch (tag) {
            case "test" :
                return ((AliyunMQConfig) AopContext.currentProxy()).test(message);
        }
        return true;

    }
    private boolean test(Message message) {
        return false;
    }

}
