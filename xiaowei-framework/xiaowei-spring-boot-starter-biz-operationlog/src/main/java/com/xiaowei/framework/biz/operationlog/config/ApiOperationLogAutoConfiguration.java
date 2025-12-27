package com.xiaowei.framework.biz.operationlog.config;

import com.xiaowei.framework.biz.operationlog.aspect.ApiOperationLogAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author: xiaowei
 * @date: 2025/4/15 13:50
 * @version: v1.0.0
 * @description: TODO
 **/
@AutoConfiguration
public class ApiOperationLogAutoConfiguration {

    @Bean
    public ApiOperationLogAspect apiOperationLogAspect() {
        return new ApiOperationLogAspect();
    }
}
