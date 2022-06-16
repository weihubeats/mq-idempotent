package com.mq.converter;

import com.mq.idempotent.core.aop.MessageConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author : wh
 * @date : 2021/11/15 14:29
 * @description:
 */
@Component
public class RocketMQMessageConverter implements MessageConverter<MessageExt> {


    @Override
    public String getUniqueKey(MessageExt messageExt, String field, Method method, Object[] args) {
        return StringUtils.isNoneBlank(messageExt.getKeys()) ? messageExt.getKeys() :messageExt.getMsgId();
    }
}
