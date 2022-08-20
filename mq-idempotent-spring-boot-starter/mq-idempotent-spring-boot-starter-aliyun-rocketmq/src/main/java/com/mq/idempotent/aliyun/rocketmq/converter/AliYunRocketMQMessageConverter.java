package com.mq.idempotent.aliyun.rocketmq.converter;

import java.lang.reflect.Method;

import com.aliyun.openservices.ons.api.Message;
import com.mq.idempotent.core.aop.MessageConverter;

import org.springframework.util.ObjectUtils;

/**
 * @author : wh
 * @date : 2021/11/15 14:00
 * @description:
 */
public class AliYunRocketMQMessageConverter implements MessageConverter<Message> {


    @Override
    public String getUniqueKey(Message message, String field, Method method, Object[] args) {
        String messageKey = message.getKey();
        return !ObjectUtils.isEmpty(messageKey) ? messageKey : message.getMsgID();
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
