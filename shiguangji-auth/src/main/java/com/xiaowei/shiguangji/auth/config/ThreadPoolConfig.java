package com.xiaowei.shiguangji.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 魏玉石
 * @data: 2025/12/30
 * @description: 自定义线程池
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(10);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(50);
        //队列容量
        threadPoolTaskExecutor.setQueueCapacity(20);
        //线程活跃时间
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        //线程前缀名
        threadPoolTaskExecutor.setThreadNamePrefix("AuthExecutor-");

        // 拒绝策略：由调用线程处理（一般为主线程）
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //关闭线程池时, 等待所有任务完成才退出
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待时间，如果超过这个时间还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是被没有完成的任务阻塞
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
