package com.mq.idempotent.core.idempotent;

/**
 * @author : wh
 * @date : 2021/11/6 14:38
 * @description:
 */
public class MySqlIdempotent implements Idempotent{

    @Override
    public void delete(UnMessage unMessage) {

    }

    @Override
    public boolean lock(UnMessage unMessage, IdempotentConfig config) {
        return false;
    }

    @Override
    public void unLock(UnMessage unMessage, IdempotentConfig config) {

    }

    @Override
    public boolean exitKey(UnMessage unMessage) {
        return false;
    }

    @Override
    public boolean saveKey(UnMessage unMessage) {
        return false;
    }
}
