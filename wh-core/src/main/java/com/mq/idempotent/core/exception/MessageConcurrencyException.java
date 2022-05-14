package com.mq.idempotent.core.exception;

/**
 * @author : wh
 * @date : 2022/3/12 17:50
 * @description:
 */
public class MessageConcurrencyException extends RuntimeException {

    public MessageConcurrencyException(String message) {
        super(message);
    }
}
