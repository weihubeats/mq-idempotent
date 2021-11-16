package com.mq.idempotent.core.aop;

/**
 * @author : wh
 * @date : 2021/11/15 13:51
 * @description: 消息转换器
 */
public interface MessageConverter<T> {

    String getUniqueKey(T t);

}
