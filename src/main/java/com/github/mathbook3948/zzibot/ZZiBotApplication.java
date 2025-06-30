package com.github.mathbook3948.zzibot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.github.mathbook3948.zzibot.mapper")
public class ZZiBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZZiBotApplication.class, args);
    }

}
