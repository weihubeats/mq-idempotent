package com.samples.config;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.ons.api.impl.rocketmq.OnsClientRPCHook;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.mq.idempotent.aliyun.rocketmq.AliYunRocketMQListener;
import com.mq.idempotent.aliyun.rocketmq.WhONSFactory;
import com.mq.idempotent.core.idempotent.Idempotent;
import com.mq.idempotent.core.idempotent.RedisIdempotent;
import lombok.extern.slf4j.Slf4j;
import com.aliyun.openservices.ons.api.Message;


import org.redisson.api.RedissonClient;
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

    @Value("${alimq.accessKey}")
    private String aclAccessKey;

    @Value("${alimq.accessKey}")
    private String aclAccessSecret;

    @Value("${alimq.orderActionTopicNameSerAddr}")
    private String orderNameSerAddr;

    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public DefaultMQProducer defaultMQProducer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, aclAccessKey);
        properties.put(PropertyKeyConst.SecretKey, aclAccessSecret);
        SessionCredentials sessionCredentials = new SessionCredentials();
        sessionCredentials.updateContent(properties);
        DefaultMQProducer defaultMQProducer =
                new DefaultMQProducer("test_group",
                        new OnsClientRPCHook(sessionCredentials));
        defaultMQProducer.setNamesrvAddr(orderNameSerAddr);
        // 发送失败重试次数
        defaultMQProducer.setRetryTimesWhenSendFailed(3);
        try {
            defaultMQProducer.start();
        } catch (Exception e) {
            log.error("异常", e);
        }
        return defaultMQProducer;
    }




    @Bean
    public Consumer defaultMQPushConsumer() {

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, "aliMQAccessKey");
        properties.put(PropertyKeyConst.SecretKey, "aliMQSecretKey");
        properties.put(PropertyKeyConst.NAMESRV_ADDR, orderNameSerAddr);

        properties.put(PropertyKeyConst.MaxReconsumeTimes, 20);
        properties.put(PropertyKeyConst.GROUP_ID, "orderServiceGid");

        Consumer consumer = ONSFactory.createConsumer(properties);
        Consumer consumer1 = WhONSFactory.createConsumer(properties);

        consumer.subscribe("orderActionTopic", "CLIENT_ORDER_CANCEL_EVENT || ORDER_REFUND_EVENT", (message, context) ->
                handleClient(message)
                        ? Action.CommitMessage
                        : Action.ReconsumeLater);

        consumer1.subscribe("orderActionTopic", "CLIENT_ORDER_CANCEL_EVENT || ORDER_REFUND_EVENT", (message, context) ->
                handleClient(message)
                        ? Action.CommitMessage
                        : Action.ReconsumeLater);

        return consumer;

    }

    public boolean handleClient(Message message) {
        return true;

    }

}
