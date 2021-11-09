package com.mq.idempotent.rocketmq;

import com.mq.idempotent.core.constants.StrategyEnum;
import com.mq.idempotent.core.idempotent.Idempotent;
import com.mq.idempotent.core.idempotent.IdempotentConfig;
import com.mq.idempotent.core.idempotent.UnMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageClientIDSetter;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : wh
 * @date : 2021/11/4 15:17
 * @description:
 */
@Slf4j
public abstract class RocketMQListener implements MessageListenerConcurrently {

    private final IdempotentConfig config;

    private final Idempotent idempotent;


    public RocketMQListener(IdempotentConfig config, Idempotent idempotent) {
//        this.strategy = strategy;
        this.config = config;
        this.idempotent = idempotent;
        log.info("Redis 幂等组件加载成功");

    }

    public RocketMQListener(Idempotent idempotent) {
        this.config = new IdempotentConfig();
        this.idempotent = idempotent;
        log.info("Redis 幂等组件加载成功");

    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        boolean hasConsumeFail = false;
        int ackIndexIfFail = -1;
        for (int i = 0; i < msgs.size(); i++) {
            MessageExt msg = msgs.get(i);
            try {
                hasConsumeFail = !consumeByIdempotent(msg);
            } catch (Exception e) {
                log.error("消费异常", e);
                hasConsumeFail = true;
            }
            //如果前面出现消费失败的话，后面也不用消费了，因为都会重发
            if (hasConsumeFail) {
                break;
            } else { //到现在都消费成功
                ackIndexIfFail = i;
            }
        }

        //全都消费成功
        if (!hasConsumeFail) {
            log.info("consume [{}] msg(s) all successfully", msgs.size());
        } else {//存在失败的
            //标记成功位，后面的会重发以重新消费，在这个位置之前的不会重发。 详情见源码：ConsumeMessageConcurrentlyService#processConsumeResult
            context.setAckIndex(ackIndexIfFail);
            log.warn("consume [{}] msg(s) fails, ackIndex = [{}] ", msgs.size(), context.getAckIndex());
        }
        //无论如何最后都返回成功
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    /**
     * 业务处理
     *
     * @param messageExt
     * @return
     */
    protected abstract boolean consume(final MessageExt messageExt);

    /**
     * 默认使用 uniqKey作去重标识
     *
     * @param messageExt
     * @return
     */
    protected String uniqID(final MessageExt messageExt) {
        String uniqID = MessageClientIDSetter.getUniqID(messageExt);
        return uniqID == null ? messageExt.getMsgId() : uniqID;
    }

    /**
     * 幂等消费
     *
     * @param messageExt
     * @return
     */
    private boolean consumeByIdempotent(final MessageExt messageExt) {

        UnMessage unMessage = new UnMessage();
        if (Objects.equals(config.getStrategyEnum(), StrategyEnum.UnIQ_ID)) {
            unMessage.setMsgUniqKey(MessageClientIDSetter.getUniqID(messageExt));
        } else {
            unMessage.setMsgUniqKey(messageExt.getKeys());
        }
        unMessage.setMsgUniqKey(messageExt.getKeys());
        log.info("uniqID {}", messageExt.getKeys());
        boolean exitKey = idempotent.exitKey(unMessage);
        // 重复消费
        if (exitKey) {
            log.info("重复消费 exitKey {}", exitKey);
            return true;
        }
        boolean lock = idempotent.lock(unMessage, config);
        // 正在消费 重新投递
        if (!lock) {
            log.info("正在消费 key {}", exitKey);
            return false;
        }
        // 其他情况消费完成
        boolean consume;
        try {
            consume = consume(messageExt);
        if (consume) {
            try {
                idempotent.saveKey(unMessage);
            } catch (Exception e) {
                config.getMonitor().monitor(unMessage);
                throw e;
            }
        }
        return consume;
        } finally {
            try {
                idempotent.unLock(unMessage, config);
            } catch (Exception ex) {
                log.error("释放锁异常 {}", unMessage);
            }
        }
    }


}
