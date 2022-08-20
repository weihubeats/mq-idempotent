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
        return new Queue(RabbitMQConstants.QUEUE_NAME,true);
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    DirectExchange TestDirectExchange() {
        return new DirectExchange(RabbitMQConstants.TEST_DIRECT_EXCHANGE,true,false);
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
