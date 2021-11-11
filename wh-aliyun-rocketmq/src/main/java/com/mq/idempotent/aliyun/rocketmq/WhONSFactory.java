package com.mq.idempotent.aliyun.rocketmq;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.ONSFactoryAPI;

import java.util.Properties;

/**
 * @author : wh
 * @date : 2021/11/10 19:55
 * @description:
 */
public class WhONSFactory extends ONSFactory {

    private static ONSFactoryAPI onsFactory = null;

    static {
        try {
            // ons client 优先加载
            Class<?> factoryClass =
                    ONSFactory.class.getClassLoader().loadClass(
                            "com.aliyun.openservices.ons.api.impl.ONSFactoryNotifyAndMetaQImpl");
            onsFactory = (ONSFactoryAPI) factoryClass.newInstance();
        } catch (Throwable e) {
            try {
                Class<?> factoryClass =
                        ONSFactory.class.getClassLoader().loadClass(
                                "com.mq.idempotent.aliyun.rocketmq.WhONSFactoryImpl");
                onsFactory = (ONSFactoryAPI) factoryClass.newInstance();
            } catch (Throwable e1) {
                e.printStackTrace();
                e1.printStackTrace();
            }
        }
    }



    public static Consumer createConsumer(final Properties properties) {
        return onsFactory.createConsumer(properties);
    }








}
