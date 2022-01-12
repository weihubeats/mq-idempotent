package com.mq.converter;

import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.utils.ReflectUtil;

/**
 * @author : wh
 * @date : 2022/1/12 11:11
 * @description:
 */
public class RabbitMQConverter implements MessageConverter<Object> {


    @Override
    public String getUniqueKey(Object o, String fileName) {
        try {
            return ReflectUtil.getFieldValue(o, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
