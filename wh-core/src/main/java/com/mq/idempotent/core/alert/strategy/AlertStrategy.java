package com.mq.idempotent.core.alert.strategy;

import com.mq.idempotent.core.spi.SPI;

/**
 * @author : wh
 * @date : 2022/6/15 18:04
 * @description:
 */
@SPI
public interface AlertStrategy {

    boolean sendMsg(String text);
    
}
