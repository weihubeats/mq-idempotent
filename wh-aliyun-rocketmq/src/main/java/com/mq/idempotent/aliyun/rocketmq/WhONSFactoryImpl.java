package com.mq.idempotent.aliyun.rocketmq;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactoryAPI;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PullConsumer;
import com.aliyun.openservices.ons.api.batch.BatchConsumer;
import com.aliyun.openservices.ons.api.impl.ONSFactoryImpl;
import com.aliyun.openservices.ons.api.impl.rocketmq.ConsumerImpl;
import com.aliyun.openservices.ons.api.impl.rocketmq.ONSUtil;


import java.util.Properties;

/**
 * @author : wh
 * @date : 2021/11/10 19:57
 * @description:
 */
public class WhONSFactoryImpl extends ONSFactoryImpl implements ONSFactoryAPI {


    @Override
    public Consumer createConsumer(final Properties properties) {
        return new WhConsumerImpl(ONSUtil.extractProperties(properties));
    }


}
