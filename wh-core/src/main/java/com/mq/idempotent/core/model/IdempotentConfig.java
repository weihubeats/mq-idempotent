package com.mq.idempotent.core.model;

import lombok.Data;

/**
 * @author : wh
 * @date : 2021/11/15 11:07
 * @description:
 */
@Data
public class IdempotentConfig {

    /**
     * 去重key redis名字
     */
    private String redisKey;
    /**
     * redis值
     */
    private String redisValue;
    /**
     * 消费key存放redis时间默认3天
     */
    private Long redisTimeOut;

    /**
     * 并发获取锁等待时间
     */
    private Long tryLockTime;




}
