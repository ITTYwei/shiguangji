package com.xiaowei.shiguangji.user.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.xiaowei.shiguangji.user.biz.domain.mapper")
@EnableFeignClients(basePackages = "com.xiaowei.shiguangji")
public class ShiguangjiUserBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiguangjiUserBizApplication.class, args);
    }

}
