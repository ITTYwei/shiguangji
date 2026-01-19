package com.xiaowei.shiguangji.auth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.xiaowei.shiguangji")
public class ShiguangjiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiguangjiAuthApplication.class, args);
    }

}
