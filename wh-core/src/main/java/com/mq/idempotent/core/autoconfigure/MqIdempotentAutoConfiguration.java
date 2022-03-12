package com.mq.idempotent.core.autoconfigure;

import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationAdvisor;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationInterceptor;
import com.mq.idempotent.core.model.IdempotentConfig;
import com.mq.idempotent.core.config.IdempotentProperties;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2021/12/30 18:13
 * @description:
 */
@Configuration
@EnableConfigurationProperties({IdempotentProperties.class})
@AllArgsConstructor
public class MqIdempotentAutoConfiguration {

    private IdempotentProperties properties;


    @Bean
    public MqIdempotentAnnotationAdvisor mqIdempotentAnnotationAdvisor(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter messageConverter) {
        MqIdempotentAnnotationInterceptor advisor = new MqIdempotentAnnotationInterceptor(redissonClient, idempotentConfig, messageConverter);
        return new MqIdempotentAnnotationAdvisor(advisor, Idempotent.class);
    }


    @ConditionalOnMissingBean(IdempotentConfig.class)
    @Bean
    public IdempotentConfig idempotentConfig() {
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.initConfig(properties);
        return idempotentConfig;
    }

}
