package com.xiaowei.framework.biz.context.config;

import com.xiaowei.framework.biz.context.interceptor.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: Feign 请求拦截器自动配置
 */
@Component
public class FeignContextAutoConfiguration {

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

}
