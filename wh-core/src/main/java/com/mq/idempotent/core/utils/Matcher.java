package com.mq.idempotent.core.utils;

/**
 * @author : wh
 * @date : 2022/1/12 11:52
 * @description:
 */
@FunctionalInterface
public interface Matcher<T> {
    /**
     * 给定对象是否匹配
     *
     * @param t 对象
     * @return 是否匹配
     */
    boolean match(T t);
}
