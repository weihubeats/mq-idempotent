/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mq.idempotent.core.autoconfigure;

import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationAdvisor;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationInterceptor;
import com.mq.idempotent.core.config.IdempotentProperties;
import com.mq.idempotent.core.model.IdempotentConfig;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
import com.mq.idempotent.core.strategy.impl.RedisIdempotentStrategy;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    public MqIdempotentAnnotationAdvisor mqIdempotentAnnotationAdvisor(AbstractIdempotentStrategy idempotentStrategy) {
        MqIdempotentAnnotationInterceptor advisor = new MqIdempotentAnnotationInterceptor(idempotentStrategy);
        return new MqIdempotentAnnotationAdvisor(advisor, Idempotent.class);
    }


    @ConditionalOnMissingBean(IdempotentConfig.class)
    @Bean
    public IdempotentConfig idempotentConfig() {
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.initConfig(properties);
        return idempotentConfig;
    }

    @Bean
    @ConditionalOnClass(RedissonClient.class)
    @ConditionalOnProperty(prefix = IdempotentProperties.PREFIX + ".strategy", value = "redis", matchIfMissing = true, havingValue = "true")
    public AbstractIdempotentStrategy idempotentStrategy(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter<?> messageConverter) {
        return new RedisIdempotentStrategy(idempotentConfig, messageConverter, redissonClient);
    }


}
