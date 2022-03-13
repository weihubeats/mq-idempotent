package com.mq.idempotent.core.annotation;

import java.lang.annotation.*;

/**
 * @author : wh
 * @date : 2021/11/12 11:27
 * @description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    String field() default "msgId";

    boolean lock() default true;
}
