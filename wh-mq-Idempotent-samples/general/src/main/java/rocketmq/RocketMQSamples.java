/*
package rocketmq;

import com.mq.idempotent.core.idempotent.Idempotent;
import com.mq.idempotent.core.idempotent.IdempotentConfig;
import com.mq.idempotent.core.idempotent.RedisIdempotent;
import com.mq.idempotent.rocketmq.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

*/
/**
 * @author : wh
 * @date : 2021/11/5 11:40
 * @description: mq 测试
 *//*

@Slf4j
public class RocketMQSamples extends RocketMQListener {


    public RocketMQSamples(IdempotentConfig config, Idempotent idempotent) {
        super(config, idempotent);
    }

    @Override
    protected boolean consume(MessageExt messageExt) {
        switch (messageExt.getTopic()) {
            case "TEST-TOPIC":
                log.info("假装消费很久....{} {}", new String(messageExt.getBody()), messageExt);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                break;
        }
        System.out.println("消费完成 " + messageExt.getKeys());
        return true;
    }


    public static void main(String[] args) throws MQClientException {

        log.info("test");
        String redisHost = "127.0.0.1";
        String redisPort = "6379";
        String redisPassword = "123456";
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + redisHost + ":" + redisPort + "");
            singleServerConfig.setPassword(redisPassword);
        RedissonClient redisson = Redisson.create(config);

        //利用Redis做幂等表

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TEST-APP1");
        consumer.subscribe("TopicTest", "*");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        String appName = consumer.getConsumerGroup();
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.setApplication("plutus");

        Idempotent idempotent = new RedisIdempotent(redisson);
        RocketMQSamples messageListener = new RocketMQSamples(idempotentConfig, idempotent);
        consumer.registerMessageListener(messageListener);
        log.info("消费者启动");
        consumer.start();
    }

}
*/
