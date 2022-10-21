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

package com.rabbitmq.config;

import com.rabbitmq.constants.RabbitMQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2022/1/11 19:32
 * @description:
 */
@Configuration
public class DirectRabbitConfig {
    
    /**
     * 邮件队列
     * @return
     */
    @Bean
    public Queue testDirectQueue() {
        return new Queue(RabbitMQConstants.QUEUE_NAME, true);
    }
    
    /**
     * 交换机
     * @return
     */
    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange(RabbitMQConstants.TEST_DIRECT_EXCHANGE, true, false);
    }
    
    /**
     * 将队列和交换机绑定
     * @return
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(testDirectQueue()).to(TestDirectExchange()).with(RabbitMQConstants.ROUTING_KEY);
    }
    
}
