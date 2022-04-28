package com.mq.idempotent.core.model;

import com.mq.idempotent.core.config.IdempotentProperties;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2021/11/15 11:07
 * @description:
 */
@Data
public class IdempotentConfig {

    /**
     * 唯一key 消息去重key
     */
    private String uniqueKey;

    /**
     * 去重值
     */
    private String uniqueValue;

    /**
     * 并发获取锁等待时间 TimeUnit.SECONDS
     */
    private Long tryLockTime;
    /**
     * 并发获取锁等待时间单位
     */
    private TimeUnit tryLockTimeUnit = TimeUnit.SECONDS;

    /**
     * 存放已消费消息时间，过期则删除。
     */
    private Long keyTimeOut;

    /**
     * 消费key存放redis时间单位
     */
    private TimeUnit timeOutTimeUnit = TimeUnit.DAYS;


    public void initConfig(IdempotentProperties properties) {
        this.uniqueKey = properties.getUniqueKey();
        this.uniqueValue = properties.getUniqueValue();
        this.tryLockTime = properties.getTryLockTime();
        this.keyTimeOut = properties.getKeyTimeOut();
    }


}
