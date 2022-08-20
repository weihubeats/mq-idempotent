package com.mq.idempotent.core.alert.strategy;

import com.mq.idempotent.core.spi.ExtensionLoader;

/**
 * @author : wh
 * @date : 2022/6/15 18:07
 * @description:
 */
public class AlertStrategyFactory {

    public static AlertStrategy newInstance(final String alertStrategyName) {
        return ExtensionLoader.getExtensionLoader(AlertStrategy.class).getJoin(alertStrategyName);
    }
}
