package com.mq.idempotent.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.function.Function;

/**
 * @author : wh
 * @date : 2021/11/4 19:59
 * @description: 消费策略
 */
public interface ConsumeStrategy {


    boolean invoke(Function<MessageExt, Boolean> consumeCallback, MessageExt messageExt);


}
