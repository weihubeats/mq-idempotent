package com.mq.idempotent.core.strategy;

import com.mq.idempotent.core.aop.MessageConverter;
import com.mq.idempotent.core.model.IdempotentConfig;
import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2022/3/12 18:07
 * @description:
 */
@AllArgsConstructor
public abstract class AbstractIdempotentStrategy implements IdempotentStrategy {

    private final IdempotentConfig idempotentConfig;

    private final MessageConverter messageConverter;

    public Long getTryLockTime() {
        return idempotentConfig.getTryLockTime();
    }

    public TimeUnit getTryLockTimeUnit() {
        return idempotentConfig.getTryLockTimeUnit();
    }

    public String getUniqueKey(Object o, String field) {
        String uniqueKey = idempotentConfig.getUniqueKey();
        return uniqueKey + messageConverter.getUniqueKey(o, field);
    }

    public IdempotentConfig getIdempotentConfig() {
        return idempotentConfig;
    }

    public String getUniqueValue() {
        return idempotentConfig.getUniqueValue();
    }

    public Long getKeyTimeOut() {
        return idempotentConfig.getKeyTimeOut();
    }

    public TimeUnit getTimeOutTimeUnit() {
        return idempotentConfig.getTimeOutTimeUnit();
    }

}
