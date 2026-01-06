package com.xiaowei.shiguangji.gatway.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaowei.shiguangji.gatway.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author: 魏玉石
 * @data: 2026/1/5
 * @description: 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 返回此登录用户所拥有的权限
     *
     * @param loginId 登录id
     * @param loginType 登录类型
     * @return
     */
    @Override
    @SneakyThrows
    public List<String> getPermissionList(Object loginId, String loginType) {
        // todo 从 redis 获取
        //获取用户当前角色所有的权限Key
        return null;
    }

    @Override
    @SneakyThrows
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表

        // todo 从 redis 获取
        //获取用户当前角色所有的角色Key
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(Long.valueOf(loginId.toString()));
        String useRolesValue  = redisTemplate.opsForValue().get(userRolesKey);
        if (StringUtils.isBlank(useRolesValue)) {
            return null;
        }
        return objectMapper.readValue(useRolesValue, new TypeReference<>() {
        });
    }

}
