package com.mq.idempotent.core.strategy;

/**
 * @author : wh
 * @date : 2022/3/12 18:03
 * @description:
 */
public interface IdempotentStrategy {

    boolean lock(String lockName);

    void save(String uniqueKey);

    void unlock(String lockName);

    boolean exitKey(String key);



}
