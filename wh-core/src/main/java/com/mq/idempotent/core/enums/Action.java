package com.mq.idempotent.core.enums;

/**
 * @author : wh
 * @date : 2021/11/11 21:00
 * @description:
 */
public enum Action {

    /**
     * 消费成功
     */
    CommitMessage,
    /**
     * 消费失败，告知服务器稍后再投递这条消息，继续消费其他消息
     */
    ReconsumeLater,

}
