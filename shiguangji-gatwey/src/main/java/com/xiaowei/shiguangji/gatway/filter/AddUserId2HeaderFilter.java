package com.xiaowei.shiguangji.gatway.filter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author: 魏玉石
 * @data: 2026/1/10
 * @description: 转发请求时，将用户 ID 添加到 Header 请求头中，透传给下游服务
 */
@Component
@Slf4j
public class AddUserId2HeaderFilter implements GlobalFilter {

    /**
     * 请求头中，用户 ID 的键
     */
    private static final String HEADER_USER_ID = "userId";
    /**
     *
     * @param exchange 表示当前的 HTTP 请求和响应的上下文，包括请求头、请求体、响应头、响应体等信息。可以通过它来获取和修改请求和响应。
     * @param chain 代表网关过滤器链，通过调用 chain.filter(exchange) 方法可以将请求传递给下一个过滤器进行处理。
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("==================> TokenConvertFilter");

        // 用户 ID
        Long userId = null;
        try {
            // 获取当前登录用户的 ID
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            // 若没有登录，则直接放行
            return chain.filter(exchange);
        }

        log.info("## 当前登录的用户 ID: {}", userId);

        Long finalUserId = userId;
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header(HEADER_USER_ID, String.valueOf(finalUserId))) // 将用户 ID 设置到请求头中
                .build();
        return chain.filter(newExchange);
    }
}
