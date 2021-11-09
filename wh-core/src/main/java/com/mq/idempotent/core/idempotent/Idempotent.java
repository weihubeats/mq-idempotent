package com.mq.idempotent.core.idempotent;

/**
 * @author : wh
 * @date : 2021/11/4 14:20
 * @description: 幂等核心接口
 */
public interface Idempotent {


    /**
     * 删除唯一消息
     * @param unMessage
     */
    void delete(UnMessage unMessage);

    /**
     * 加锁
     *
     * @return
     */
    boolean lock(UnMessage unMessage, IdempotentConfig config);

    /**
     * 释放锁
     * @param unMessage
     * @param config
     */
    void unLock(UnMessage unMessage, IdempotentConfig config);

    /**
     * 唯一key是否存在
     */
    boolean exitKey(UnMessage unMessage);

    /**
     * 写入消费完成记录表
     * @param unMessage
     * @return boolean
     */
    boolean saveKey(UnMessage unMessage);









}
