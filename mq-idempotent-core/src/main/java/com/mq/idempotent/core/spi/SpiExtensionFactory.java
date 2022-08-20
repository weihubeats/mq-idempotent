package com.mq.idempotent.core.spi;

import com.mq.idempotent.core.alert.strategy.AlertStrategy;
import com.mq.idempotent.core.alert.strategy.LarkAlarmStrategy;

import java.util.Optional;

/**
 * @author : wh
 * @date : 2022/6/15 15:51
 * @description:
 */
@Join
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultJoin)
                .orElse(null);
    }
}

