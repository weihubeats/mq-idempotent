package com.mq.idempotent.core.idempotent;

import lombok.Data;

/**
 * @author : wh
 * @date : 2021/11/4 14:36
 * @description: 唯一对象封装
 */
@Data
public class UnMessage {

    /**
     * 应用名
     */
    private String application;

    private String topic;

    private String tag;
    /**
     * 唯一key
     */
    private String msgUniqKey;



}
