package com.mq.idempotent.core.alert.strategy;

import lombok.Data;

/**
 * @author : wh
 * @date : 2022/6/16 17:12
 * @description:
 */
@Data
public class AlertProperties {

    private String webHook;
    
    private String template;

    /**
     * 是否@所有人
     */
    private boolean atAll;
    
}
