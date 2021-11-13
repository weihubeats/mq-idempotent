## wh-mq-Idempotent

通用的mq消息幂等去重框架，开箱即用

1. 需要项目是Springboot项目
2. 原理很简单基于Spring AOP + Redis做的
3. 现在暂时只支持Aliyun Client
4. 由于目前源代码非常轻量，所以不引用jar直接copy源代码到项目中使用也是可以的


## 使用

### rocketmq-client
- maven
```xml
<dependency>
  <groupId>io.github.weihubeats</groupId>
  <artifactId>wh-rocketmq</artifactId>
  <version>1.0.4</version>
</dependency>
```
- gradle
```xml
implementation 'io.github.weihubeats:wh-rocketmq:1.0.4'
```

使用例子请参考 wh-mq-Idempotent-samples 模块

springboot 使用

- 最简单使用

- 基于AOP实现，在需要幂等的方法添加注解
```java
@Idempotent
```
一个简单例子 参考模块 samples-springboot config AliyunMQConfig.java
```java
@Bean(initMethod = "start", destroyMethod = "shutdown")
    public Consumer consumer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, "aliMQAccessKey");
        properties.put(PropertyKeyConst.SecretKey, "aliMQSecretKey");
        properties.put(PropertyKeyConst.NAMESRV_ADDR, orderNameSerAddr);
        properties.put(PropertyKeyConst.MaxReconsumeTimes, 20);
        properties.put(PropertyKeyConst.GROUP_ID, "orderServiceGid");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("orderTopic", "test || test_event", (message, context) ->
                handleClient(message)
                        ? Action.CommitMessage
                        : Action.ReconsumeLater);
        return consumer;

    }

    @Idempotent
    public boolean handleClient(Message message) {
        String tag = message.getTag();
        switch (tag) {
            case "test" :
                return ((AliyunMQConfig) AopContext.currentProxy()).test(message);
        }
        return true;

    }
    private boolean test(Message message) {
        return false;
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