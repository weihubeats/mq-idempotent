package com.rabbitmq.message;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wh
 * @date : 2022/1/12 13:44
 * @description:
 */
@Data
public class TestMessage implements Serializable {

    private Integer id;

    private String msg;
}
