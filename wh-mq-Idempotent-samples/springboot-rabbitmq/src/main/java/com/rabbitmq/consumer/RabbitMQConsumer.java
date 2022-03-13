package com.rabbitmq.consumer;

import com.mq.idempotent.core.annotation.Idempotent;
import com.rabbitmq.constants.RabbitMQConstants;
import com.rabbitmq.message.TestMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2022/1/11 19:43
 * @description:
 */
@Component
public class RabbitMQConsumer {


    /*@RabbitHandler
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)
    @Idempotent
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }*/



    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstants.QUEUE_NAME)
    @Idempotent(field = "id")
    public void consumerTestMessage(TestMessage testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

}
