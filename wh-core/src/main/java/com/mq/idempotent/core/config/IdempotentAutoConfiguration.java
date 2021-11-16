package com.mq.idempotent.core.config;

import com.mq.idempotent.core.model.IdempotentConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2021/11/15 11:23
 * @description:
 */
@Configuration
public class IdempotentAutoConfiguration {

    @Value("${idempotent.redis.key:mq::unique::}")
    private String redisKey;

    @Value("${idempotent.redis.value:s}")
    private String redisValue;

    @Value("${idempotent.redis.value:1}")
    private Long tryLockTime;




    @ConditionalOnMissingBean(IdempotentConfig.class)
    @Bean
    public IdempotentConfig idempotentConfig() {
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.setRedisKey(redisKey);
        idempotentConfig.setRedisValue(redisValue);
        idempotentConfig.setTryLockTime(tryLockTime);
        return idempotentConfig;

    }

}
