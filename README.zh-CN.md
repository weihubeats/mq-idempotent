## wh-mq-Idempotent

通用的mq消息幂等去重框架，开箱即用

1. 支持使用Redis or Mysql 作幂等表
2. 支持使用业务主键去重或消息ID去重(默认)
3. 支持消息并发控制
4. 目前支持mq：RocketMQ

## 模块说明
- wh-core 核心
- wh-rocketmq mq幂等核心实现
- wh-mq-Idempotent-samples 使用例子

## 未来版本

1. 支持RabbitMQ
2. 支持kafka
3. 支持 rocketmq-spring-boot-starter 整合开箱即用