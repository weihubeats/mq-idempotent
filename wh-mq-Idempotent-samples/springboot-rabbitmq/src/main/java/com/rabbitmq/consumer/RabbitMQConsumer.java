package com.rabbitmq.consumer;

import com.rabbitmq.constants.RabbitMQConstants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wh
 * @date : 2022/1/11 19:43
 * @description:
 */
@Component
public class RabbitMQConsumer {


    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
