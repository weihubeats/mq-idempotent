package com.mq.idempotent.core.idempotent;

import com.mq.idempotent.core.constants.RedisConstants;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2021/11/4 14:44
 * @description:
 */
public class RedisIdempotent implements Idempotent{

    private final RedissonClient redissonClient;


    public RedisIdempotent(RedissonClient redissonClient) {
        if (Objects.isNull(redissonClient)) {
            throw new NullPointerException("redissonClient template is null");
        }
        this.redissonClient = redissonClient;
    }

    @Override
    public void delete(UnMessage unMessage) {

    }

    @Override
    public boolean lock(UnMessage unMessage, IdempotentConfig config) {
        RLock stockLock = redissonClient.getLock(unMessage.getMsgUniqKey());
        try {
            return stockLock.tryLock(config.getWaitTime(), config.getTimeUnit());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unLock(UnMessage unMessage, IdempotentConfig config) {
        RLock stockLock = redissonClient.getLock(unMessage.getMsgUniqKey());
        stockLock.unlock();
    }

    @Override
    public boolean exitKey(UnMessage unMessage) {
        RBucket<String> stockLock = redissonClient.getBucket(RedisConstants.KEY + unMessage.getMsgUniqKey());
        return stockLock.isExists();
    }

    @Override
    public boolean saveKey(UnMessage unMessage) {
        RBucket<String> stockLock = redissonClient.getBucket(RedisConstants.KEY + unMessage.getMsgUniqKey());
        stockLock.set(unMessage.getMsgUniqKey(), 3, TimeUnit.DAYS);
        return true;
    }
}
