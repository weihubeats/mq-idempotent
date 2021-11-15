package com.mq.converter;

import com.mq.idempotent.core.aop.MessageConverter;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/15 14:29
 * @description:
 */
@Component
public class RocketMQMessageConverter implements MessageConverter<MessageExt> {


    @Override
    public String getUniqueKey(MessageExt messageExt) {
        return Objects.nonNull(messageExt.getKeys()) ? messageExt.getKeys() :messageExt.getMsgId();
    }
}
