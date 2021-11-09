/*
package com.samples.config;

import com.mq.idempotent.core.constants.StrategyEnum;
import com.mq.idempotent.core.idempotent.*;
import com.mq.idempotent.rocketmq.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

*/
/**
 * @author : wh
 * @date : 2021/11/6 11:09
 * @description:
 *//*

@Configuration
@Slf4j
public class MQConfig {

    @Autowired
    private RedissonClient redissonClient;


    @Bean
    public DefaultMQPushConsumer defaultMQProducer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TEST-APP1");
        consumer.subscribe("TopicTest", "*");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        */
/*IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.setApplication("test");
        // 设置并发加锁等待时间 默认1s
        idempotentConfig.setWaitTime(1L);
        idempotentConfig.setTimeUnit(TimeUnit.SECONDS);
        idempotentConfig.setStrategyEnum(StrategyEnum.KEYS);
        // 自定义监控报警
        idempotentConfig.setMonitor(new Monitor() {
            @Override
            public void monitor(UnMessage unMessage) {
                Monitor.super.monitor(unMessage);
            }
        });*//*


        Idempotent idempotent = new RedisIdempotent(redissonClient);
        RocketMQListener messageListener = new RocketMQListener(idempotent) {
            @Override
            protected boolean consume(MessageExt messageExt) {
                switch (messageExt.getTopic()) {
                    case "TEST-TOPIC":
                        log.info("假装消费很久....{} {}", new String(messageExt.getBody()), messageExt);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                        break;
                }
                System.out.println("消费完成 " + messageExt.getKeys());
                return true;
            }
        };
        consumer.registerMessageListener(messageListener);
        log.info("消费者启动");
        consumer.start();
        return consumer;

    }




}
*/
