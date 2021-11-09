package com.mq.idempotent.core.idempotent;

import com.mq.idempotent.core.constants.StrategyEnum;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2021/11/4 14:53
 * @description:
 */
@Data
public class IdempotentConfig {

    /**
     * 应用名
     */
    private String application;

    /**
     * 锁默认等待时间
     */
    private Long waitTime = 1L;
    /**
     * 等带时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 默认监控
     */
    private Monitor monitor = new DefaultMonitor();

    /**
     * 去重key
     */
    private StrategyEnum strategyEnum = StrategyEnum.UnIQ_ID;


















}
