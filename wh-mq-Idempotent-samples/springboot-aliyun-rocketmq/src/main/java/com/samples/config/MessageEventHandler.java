package com.samples.config;

import com.aliyun.openservices.ons.api.Message;
import com.mq.idempotent.core.annotation.Idempotent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/15 20:02
 * @description:
 */
@Service
public class MessageEventHandler {

    @Autowired
    private MessageEventHandler messageEventHandler;




    public boolean consumer(Message message) {
        if (Objects.equals(message.getTag(), "TagA")) {
            // 防止AOP失效失效
            messageEventHandler.testConsumer(message);
            return true;
        }
        return true;

    }

    @Idempotent
    public void testConsumer(Message message) {
        String msg = new String(message.getBody());
        System.out.println("消息id " + message.getMsgID());
        System.out.println("消息key " + message.getKey());
        System.out.println("消费成功, msg " + msg);
    }

}
