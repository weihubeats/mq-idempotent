package com.samples.controller;

import com.mq.idempotent.core.annotation.Idempotent;
import org.springframework.stereotype.Service;

/**
 * @author : wh
 * @date : 2021/11/12 18:05
 * @description:
 */
@Service
public class Test {

    @Idempotent
    public void test() {

    }
}
