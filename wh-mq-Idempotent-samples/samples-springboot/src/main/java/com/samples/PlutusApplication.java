package com.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

/**
 * @author XiaoLei
 * @date 2021/1/9 12:32
 * @description
 */
@SpringBootApplication
public class PlutusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlutusApplication.class, args);
    }

}
