package com.mq.idempotent.core.alert.strategy;

import com.mq.idempotent.core.spi.Join;

/**
 * @author : wh
 * @date : 2022/6/15 18:05
 * @description:
 */
@Join
public class LarkAlarmStrategy implements AlertStrategy{
    
    
    @Override
    public boolean sendMsg(String text) {
        return false;
    }

}
