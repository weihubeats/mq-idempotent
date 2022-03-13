package com.mq.idempotent.core.strategy.impl;

import com.mq.idempotent.core.model.IdempotentConfig;
import com.mq.idempotent.core.strategy.AbstractIdempotentStrategy;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author : wh
 * @date : 2022/3/13 09:46
 * @description:
 */
public class JDBCIdempotentStrategy extends AbstractIdempotentStrategy {

    private final JdbcTemplate jdbcTemplate;

    public JDBCIdempotentStrategy(IdempotentConfig idempotentConfig, JdbcTemplate jdbcTemplate) {
        super(idempotentConfig);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public boolean lock(String lockName) {
        return false;
    }

    @Override
    public void save(String uniqueKey) {

    }

    @Override
    public void unlock(String lockName) {

    }

    @Override
    public boolean exitKey(String key) {
        return false;
    }
}
