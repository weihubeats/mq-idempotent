package com.mq.idempotent.core.spi;

/**
 * @author : wh
 * @date : 2022/6/15 15:52
 * @description:
 */
@SPI("spi")
public interface ExtensionFactory {

    /**
     * Gets Extension.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the extension
     */
    <T> T getExtension(String key, Class<T> clazz);
}
