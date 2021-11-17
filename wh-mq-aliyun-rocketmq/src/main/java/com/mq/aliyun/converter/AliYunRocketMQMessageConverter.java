package com.mq.aliyun.converter;

import com.aliyun.openservices.ons.api.Message;
import com.mq.idempotent.core.aop.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author : wh
 * @date : 2021/11/15 14:00
 * @description:
 */
@Component
public class AliYunRocketMQMessageConverter implements MessageConverter<Message> {


    @Override
    public String getUniqueKey(Message message) {
        String messageKey = message.getKey();

        return !StringUtils.isEmpty(messageKey) ? messageKey : message.getMsgID();
    }
}
