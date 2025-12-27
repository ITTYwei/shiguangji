package com.xiaowei.shiguangji.auth;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xiaowei.shiguangji.auth.domain.mapper")
public class ShiguangjiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiguangjiAuthApplication.class, args);
    }

}
