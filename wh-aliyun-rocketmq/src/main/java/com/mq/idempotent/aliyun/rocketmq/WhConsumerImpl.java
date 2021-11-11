package com.mq.idempotent.aliyun.rocketmq;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.impl.rocketmq.ConsumerImpl;
import com.aliyun.openservices.ons.api.impl.rocketmq.ONSUtil;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.mq.idempotent.core.idempotent.Idempotent;
import com.mq.idempotent.core.idempotent.RedisIdempotent;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wh
 * @date : 2021/11/11 19:57
 * @description:
 */
public class WhConsumerImpl extends ConsumerImpl {

    private final ConcurrentHashMap<String, MessageListener> subscribeTable = new ConcurrentHashMap<>();


    public RedissonClient redissonClient;


    public WhConsumerImpl(Properties properties) {
        super(properties);
    }

    public void start() {
        Idempotent idempotent = new RedisIdempotent(redissonClient);
        AliYunRocketMQListener aliYunRocketMQListener = new AliYunRocketMQListener(idempotent) {
            @Override
            protected boolean consume(MessageExt messageExt) {

                Message msg = ONSUtil.msgConvert(messageExt);
                final ConsumeContext context = new ConsumeContext();
                MessageListener listener = subscribeTable.get(messageExt.getTopic());
                if (null == listener) {
                    throw new ONSClientException("MessageListener is null");
                }
                Action action = listener.consume(msg, context);
                if (action == null || Objects.equals(action, Action.ReconsumeLater)) {
                    return false;
                }
                return true;
            }
        };
        this.defaultMQPushConsumer.registerMessageListener(aliYunRocketMQListener);
        super.start();
    }







}
