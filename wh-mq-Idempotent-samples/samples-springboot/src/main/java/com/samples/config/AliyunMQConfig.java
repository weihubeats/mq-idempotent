package com.samples.config;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.impl.authority.SessionCredentials;
import com.aliyun.openservices.ons.api.impl.rocketmq.OnsClientRPCHook;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.mq.idempotent.aliyun.rocketmq.AliYunRocketMQListener;
import com.mq.idempotent.core.idempotent.Idempotent;
import com.mq.idempotent.core.idempotent.RedisIdempotent;
import lombok.extern.slf4j.Slf4j;


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
    public DefaultMQPushConsumer defaultMQPushConsumer() {

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, aclAccessKey);
        properties.put(PropertyKeyConst.SecretKey, aclAccessSecret);
        SessionCredentials sessionCredentials = new SessionCredentials();
        sessionCredentials.updateContent(properties);
        DefaultMQPushConsumer defaultMQPushConsumer =
                new DefaultMQPushConsumer("test_group",
                        new OnsClientRPCHook(sessionCredentials),
                        new AllocateMessageQueueAveragely());
        defaultMQPushConsumer.setNamesrvAddr(orderNameSerAddr);
        Idempotent idempotent = new RedisIdempotent(redissonClient);


        AliYunRocketMQListener messageListener = new AliYunRocketMQListener(idempotent) {
            @Override
            protected boolean consume(MessageExt messageExt) {
                System.out.println("获取到消息，不消费" + JSON.toJSONString(messageExt));
                System.out.println("消费完成 " + messageExt.getKeys());
                return true;
            }

        };
        // 注册监听器
        defaultMQPushConsumer.registerMessageListener(messageListener);
        // 订阅所有消息
        try {
            defaultMQPushConsumer.subscribe("domain_event", "*");
            // 启动消费者
            log.info("消费者启动");
            defaultMQPushConsumer.start();
        } catch (Exception e) {
            System.out.println("接入错误：" + e.getMessage());
//            log.error(String.valueOf(e));
        }
        return defaultMQPushConsumer;

    }

}
