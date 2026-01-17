package com.xiaowei.framework.biz.context.interceptor;

import com.xiaowei.framework.biz.context.holder.LoginUserContextHolder;
import com.xiaowei.framework.common.constant.GlobalConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: Feign 请求拦截器
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = LoginUserContextHolder.getUserId();
        // 若不为空，则添加到请求头中
        if (Objects.nonNull(userId)) {
            requestTemplate.header(GlobalConstants.USER_ID, String.valueOf(userId));
            log.info("########## feign 请求设置请求头 userId: {}", userId);
        }
    }
}
