package com.mq.idempotent.aliyun.rocketmq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.impl.rocketmq.ConsumerImpl;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/10 18:17
 * @description:
 */
public class AliYunRocketMQEnhance {

    private MessageListener messageListener;

    public static void enhance() {

        CtClass cc = null;
        ClassPool pool = ClassPool.getDefault();
        try {
            cc = pool.get("com.aliyun.openservices.ons.api.impl.rocketmq.ConsumerImpl");
            if (Objects.nonNull(cc)) {
                CtMethod ctMethod = cc.getDeclaredMethod("start");
                ctMethod.setBody("{ this.defaultMQPushConsumer.registerMessageListener(new MessageListenerImpl());super.start(); }");
                DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
                /*defaultMQPushConsumer.registerMessageListener(new AliYunRocketMQListener() {
                    @Override
                    protected boolean consume(MessageExt messageExt) {
                        MessageListener listener = ConsumerImpl.this.subscribeTable.get(msg.getTopic());
                        if (null == listener) {
                            throw new ONSClientException("MessageListener is null");
                            listener.consume(msg, context);
                        }

                        return false;
                    }
                });*/





            }
        } catch (Exception e) {
            System.out.println("阿里云RocketMQ幂等增强失败");
            e.printStackTrace();
        }


    }

    /*public void start() {
        this.defaultMQPushConsumer.registerMessageListener(new ConsumerImpl.MessageListenerImpl());
        super.start();
    }*/

}
