package com.samples.config;

import com.mq.idempotent.core.annotation.Idempotent;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/15 20:02
 * @description:
 */
@Service
public class MessageEventHandler  {

    @Autowired
    private MessageEventHandler messageEventHandler;




    public void consumer(MessageExt messageExt) {
        if (Objects.equals(messageExt.getTags(), "TagA")) {
            // 防止AOP失效失效
            messageEventHandler.testConsumer(messageExt);
        }

    }

    @Idempotent
    public void testConsumer(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        System.out.println("消息id " + messageExt.getMsgId());
        System.out.println("消息keys " + messageExt.getKeys());
        System.out.println("消费成功, msg " + msg);
    }

}
