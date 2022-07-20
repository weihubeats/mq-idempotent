## wh-mq-Idempotent




General mq message idempotent deduplication framework, out of the box, supports all mainstream mq
1. The required project is a Springboot project
2. The principle is very simple, based on Spring AOP + Redis
3. For now, only aliyun ons-client and RocketMQ Client are supported. If not, you can implement MessageConverter to define it yourself,
   which is very lightweight and convenient
4. Since the current source code is very lightweight, it is also possible to copy the source code directly into the project without quoting the jar.

> Since there are no rabbitmq, Kafka, etc. locally, it is also very convenient to support it. You only need to implement the MessageConverter interface,
> which will be explained in detail later.



## use

####
Necessary condition
1. redis(redission)
2. springboot
3. jdk8+

#### 1. Add dependency
##### rocketmq-client
- maven
```xml
<dependency>
  <groupId>io.github.weihubeats</groupId>
  <artifactId>wh-mq-rocketmq</artifactId>
  <version>1.1.3-Release</version>
</dependency>
```
- gradle
```xml
implementation 'io.github.weihubeats:wh-mq-rocketmq:1.1.3-Release'
```

##### aliyun ons-client
```xml
<dependency>
<groupId>io.github.weihubeats</groupId>
<artifactId>wh-mq-aliyun-rocketmq</artifactId>
<version>1.1.3-Release</version>
</dependency>
```

- gradle
```xml
implementation 'io.github.weihubeats:wh-mq-aliyun-rocketmq'
```
#### 2. Add annotations to methods that need to be idempotent
```java
@Idempotent
```

```java
    @Idempotent
    public void testConsumer(Message message) {
        String msg = new String(message.getBody());
        System.out.println("消息id " + message.getMsgID());
        System.out.println("消息key " + message.getKey());
        System.out.println("消费成功, msg " + msg);
    }
```

> Note, because it is implemented based on AOP, you need to pay attention to the problems caused by AOP failure scenarios
> The parameter of the added method must be Message, and the return value of the method must be void or boolean,
> because aop needs to handle repeated consumption and directly returns true or null
> For more detailed practical methods, please refer to the module wh-mq-Idempotent-samples

## Example reference
For usage examples, please refer to the wh-mq-Idempotent-samples module


## Custom configuration
```java
    @Bean
    public IdempotentConfig idempotentConfig() {
        IdempotentConfig idempotentConfig = new IdempotentConfig();
        // 去重 redis key名 默认 mq::unique::
        idempotentConfig.setRedisKey(redisKey);
        // 去重redis value 默认 s
        idempotentConfig.setRedisValue(redisValue);
        // 去重redis尝试获取锁等待时间 默认1s 单位秒
        idempotentConfig.setTryLockTime(tryLockTime);
        // 设置处理成功消息存放redis时间 默认 3天
        idempotentConfig.setRedisTimeOut(redisTimeOut);
        return idempotentConfig;

    }
```

## Custom mq de-duplication
Introduce dependencies
```java
<dependency>
<groupId>io.github.weihubeats</groupId>
<artifactId>wh-core</artifactId>
<version>1.1.3-Release</version>
</dependency>
```

Implement the interface MessageConverter.java
For example, support rocketMQ
```java
@Component
public class RocketMQMessageConverter implements MessageConverter<MessageExt> {


    @Override
    public String getUniqueKey(MessageExt messageExt) {
        return !StringUtils.isEmpty(messageExt.getKeys()) ? messageExt.getKeys() :messageExt.getMsgId();
    }
}

```

## Module description
- wh-core Core realization
- wh-mq-rocketmq Rocketmq idempotent core implementation
- wh-mq-aliyun-rocketmq aliyun client idempotent core implementation
- wh-mq-Idempotent-samples Usage example

## Design ideas

![img.png](static/img/img.png)

The general idea of the current redis implementation is as follows
1. Consumers get MQ consumption information
2. Based on the business unique key in the configured MQ message, go to reids (Mysql) to determine whether it has been consumed
3. If there is no consumption, then lock to prevent concurrency problems, if the lock is successful, then consume, if it fails,
   return the consumption failure to let MQ re-deliver, because this prevents the first thread that grabs the lock from failing to execute,
   so it cannot directly return to success, and the following tasks are required. Re-deliver to MQ for re-consumption
4. After successful execution of the business code, write consumption is successful (write redis or update Mysql)
5. Release lock

## release version

- 1.0.4 : Support Alibaba Cloud RocketMQ Client
- 1.0.5 : Added support for open source RocketMQ Client, and added automatic configuration `IdempotentConfig.java`
- 1.0.6 : Optimize the problem of repeated consumption and repeated delivery, and optimize the code structure
- 1.1.3-Release : Optimize the redis key expiration time

## Future version

1. Support RabbitMQ
2. Support kafka
3. Support Mysql de-duplication
## in development。。。。。。

## We hope you can join

Author WeChat:
![Author WeChat](static/img/wx.jpg)