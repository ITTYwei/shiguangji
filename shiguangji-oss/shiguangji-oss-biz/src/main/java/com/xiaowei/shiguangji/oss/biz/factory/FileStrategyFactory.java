package com.xiaowei.shiguangji.oss.biz.factory;

import com.xiaowei.shiguangji.oss.biz.strategy.FileStrategy;
import com.xiaowei.shiguangji.oss.biz.strategy.impl.AliyunOSSFileStrategy;
import com.xiaowei.shiguangji.oss.biz.strategy.impl.MinioFileStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 魏玉石
 * @data: 2026/1/11
 * @description:
 */
@Configuration
public class FileStrategyFactory {

    @Value("${storage.type}")
    private String strategyType;

    @Bean
    public FileStrategy getFileStrategy() {
        if (StringUtils.equals(strategyType, "minio")) {
            return new MinioFileStrategy();
        } else if (StringUtils.equals(strategyType, "aliyun")) {
            return new AliyunOSSFileStrategy();
        }

        throw new IllegalArgumentException("不可用的存储类型");
    }

}
