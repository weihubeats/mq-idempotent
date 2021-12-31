package com.samples.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;




/**
 * @author : wh
 * @date : 2021/11/15 15:44
 * @description:
 */
@Configuration
public class MQConfig {

    @Value("${rocketmq.consumer.namesrvAddr:127.0.0.1:9876}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.groupName:please_rename_unique_group_name_4}")
    private String groupName;

    @Value("${rocketmq.consumer.consumeThreadMin:10}")
    private int consumeThreadMin;

    @Value("${rocketmq.consumer.consumeThreadMax:10}")
    private int consumeThreadMax;

    @Value("${rocketmq.consumer.topics:TopicTest}")
    private String topics;

    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize:1}")
    private int consumeMessageBatchMaxSize;

    @Autowired
    MessageEventHandler handler;




    @Bean
    @ConditionalOnMissingBean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws RuntimeException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);

        // 设置 consumer 第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 设置消费模型，集群还是广播，默认为集群
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 设置一次消费消息的条数，默认为 1 条
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            if (CollectionUtils.isEmpty(msgs)) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            MessageExt messageExt = msgs.get(0);
            handler.consumer(messageExt);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        try {
            consumer.subscribe(topics, "*");
            // 启动消费
            consumer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return consumer;
    }




}
