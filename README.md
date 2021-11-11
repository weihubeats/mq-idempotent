## wh-mq-Idempotent

通用的mq消息幂等去重框架，开箱即用

1. 支持使用Redis or Mysql(待开发) 作幂等表
2. 支持使用业务主键去重或消息ID去重(默认)
3. 支持消息并发控制
4. 目前支持mq:RocketMQ(支持阿里云客户端ons-client,也支持rocketmq-client)


## 使用

### rocketmq-client
- maven
```xml
<dependency>
  <groupId>io.github.weihubeats</groupId>
  <artifactId>wh-rocketmq</artifactId>
  <version>1.0.3</version>
</dependency>
```
- gradle
```xml
implementation 'io.github.weihubeats:wh-rocketmq:1.0.3'
```

使用例子请参考 wh-mq-Idempotent-samples 模块

springboot 使用

- 最简单使用
```java
    @Bean
    public DefaultMQPushConsumer defaultMQProducer1() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TEST-APP1");
        consumer.subscribe("TopicTest", "*");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        Idempotent idempotent = new RedisIdempotent(redissonClient);
        RocketMQListener messageListener = new RocketMQListener(idempotent) {
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
        };
        consumer.registerMessageListener(messageListener);
        log.info("消费者启动");
        consumer.start();
        return consumer;

    }
```

## ons-client 客户端使用
```xml
<dependency>
  <groupId>io.github.weihubeats</groupId>
  <artifactId>wh-aliyun-rocketmq</artifactId>
  <version>1.0.3</version>
</dependency>
```

参考 com.samples.config.AliyunMQConfig

## 自定义 idempotentConfig中的一些策略
1. 并发消费等待时间
2. 自定义监控
```java
IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.setApplication("test");
        // 自定义监控报警
        idempotentConfig.setMonitor(new Monitor() {
            @Override
            public void monitor(UnMessage unMessage) {
                Monitor.super.monitor(unMessage);
            }
        });
```
> 默认redis 写入成功仅打印异常log
3. 自定义业务去重key(RocketMQ 默认使用消息唯一key,如果要使用业务设置的keys)
```java
        idempotentConfig.setStrategyEnum(StrategyEnum.KEYS);
```
4. 

```java
@Configuration
@Slf4j
public class MQConfig {

    @Autowired
    private RedissonClient redissonClient;


    @Bean
    public DefaultMQPushConsumer defaultMQProducer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("TEST-APP1");
        consumer.subscribe("TopicTest", "*");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        idempotentConfig.setApplication("plutus");

        Idempotent idempotent = new RedisIdempotent(redissonClient);
        RocketMQListener messageListener = new RocketMQListener(idempotentConfig, idempotent) {
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
        };
        consumer.registerMessageListener(messageListener);
        log.info("消费者启动");
        consumer.start();
        return consumer;

    }

}

```






## 模块说明
- wh-core 核心
- wh-rocketmq rocketmq幂等核心实现
- wh-aliyun-rocketmq 阿里云client幂等核心实现
- wh-mq-Idempotent-samples 使用例子

## 设计思路

![img.png](static/img/img.png)

目前redis实现的大致思路如下
1. 消费者获取到MQ消费信息
2. 基于配置的MQ消息中的业务唯一键去reids(Mysql) 判断是否已消费
3. 如果没有消费则加锁防止并发问题,加锁成功则消费,失败则返回消费失败让MQ重新投递,因为这里防止第一个抢到锁的线程执行失败,所以不能直接返回成功,需要后面的任务重新投递到MQ重新消费
4. 执行业务代码成功后写入消费成功(写入redis或更新Mysql),但是需要注意的是这里有一个待优化点就是业务代码执行和写入消费成功消息不是一个原子操作,所以这里再写入消费成功信息失败后添加了报警(后续考虑优化为原子操作)
5. 释放锁

## 未来版本

1. 支持RabbitMQ
2. 支持kafka
3. 支持 rocketmq-spring-boot-starter 整合开箱即用
4. 上传到中央Maven

## 正在开发中。。。。。。

## 期待你的加入

作者微信:
![作者微信](static/img/wx.jpg)