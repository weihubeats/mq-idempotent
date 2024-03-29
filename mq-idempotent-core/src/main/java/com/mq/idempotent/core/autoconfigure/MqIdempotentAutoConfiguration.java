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

import com.mq.idempotent.core.alert.strategy.AlertStrategy;
import com.mq.idempotent.core.alert.strategy.AlertStrategyFactory;
import com.mq.idempotent.core.annotation.Idempotent;
import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationAdvisor;
import com.mq.idempotent.core.aop.MqIdempotentAnnotationInterceptor;
import com.mq.idempotent.core.config.IdempotentProperties;
import com.mq.idempotent.core.model.IdempotentConfig;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
import com.mq.idempotent.core.strategy.impl.RedisIdempotentStrategy;
import com.mq.idempotent.core.utils.TransactionUtil;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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
    public MqIdempotentAnnotationAdvisor mqIdempotentAnnotationAdvisor(AbstractIdempotentStrategy idempotentStrategy, @Autowired(required = false) AlertStrategy alertStrategy,
                                                                       TransactionUtil transactionUtil) {
        MqIdempotentAnnotationInterceptor advisor = new MqIdempotentAnnotationInterceptor(idempotentStrategy, alertStrategy, transactionUtil);
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
    @ConditionalOnProperty(prefix = IdempotentProperties.PREFIX + ".", value = "alertName", havingValue = "lark")
    public AlertStrategy alertStrategy(IdempotentConfig idempotentConfig) {
        return AlertStrategyFactory.newInstance(idempotentConfig.getAlertName());
    }
    
    @Bean
    @ConditionalOnClass(RedissonClient.class)
    @ConditionalOnProperty(prefix = IdempotentProperties.PREFIX + ".strategy", value = "jdbc", havingValue = "true")
    public AbstractIdempotentStrategy jdbcIdempotentStrategy(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter<?> messageConverter) {
        return new RedisIdempotentStrategy(idempotentConfig, messageConverter, redissonClient);
    }
    
    @Bean
    @ConditionalOnClass(AbstractIdempotentStrategy.class)
    public AbstractIdempotentStrategy redisIdempotentStrategy(RedissonClient redissonClient, IdempotentConfig idempotentConfig, MessageConverter<?> messageConverter) {
        return new RedisIdempotentStrategy(idempotentConfig, messageConverter, redissonClient);
    }
    
    @Bean
    public TransactionUtil transactionUtil(PlatformTransactionManager transactionManager) {
        return new TransactionUtil(transactionManager);
    }
    
}
