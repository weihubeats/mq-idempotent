package com.mq.idempotent.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wh
 * @date : 2021/12/30 21:20
 * @description:
 */
@Getter
@Setter
@ConfigurationProperties(prefix = IdempotentProperties.PREFIX)
public class IdempotentProperties {


    public static final String PREFIX = "idempotent";


    /**
     * 去重key redis名字
     */
    private String redisKey = "mq::unique::";

    /**
     * redis值
     */
    private String redisValue = "s";

    /**
     * 并发获取锁等待时间
     */
    private Long tryLockTime = 1L;

    /**
     * 消费key存放redis时间默认3天
     */
    private Long redisTimeOut = 3L;

    /**
     * 是否开启并发控制
     */
    private Boolean concurrency = true;

}
