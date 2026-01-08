package com.xiaowei.shiguangji.gatway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.shiguangji.gatway.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: 魏玉石
 * @data: 2026/1/5
 * @description: 自定义权限验证接口扩展
 */
@Component
@Slf4j
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
        //首先通过用户Id获取到用户的角色,通过角色获取key,在通过key去redis中获取到权限

        //通过id去redis中获取角色
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(Long.valueOf(loginId.toString()));
        String userRolesValue  = redisTemplate.opsForValue().get(userRolesKey);
        if (StringUtils.isBlank(userRolesValue)) {

            return null;
        }
        List<String> roleKeys = objectMapper.readValue(userRolesValue, new TypeReference<>() {
        });
        if (CollUtil.isEmpty(roleKeys)){
            return Collections.emptyList();
        }
        List<String> permissionsKeys = new ArrayList<>();
        roleKeys.forEach(roleKey -> {
            String rolePermissionsKey = RedisKeyConstants.buildRolePermissionsKey(roleKey);
            String permissionsKey = redisTemplate.opsForValue().get(rolePermissionsKey);
            try {
                // 将 JSON 字符串转换为 List<String> 权限集合
                List<String> rolePermissions = objectMapper.readValue(permissionsKey, new TypeReference<>() {});
                permissionsKeys.addAll(rolePermissions);
            } catch (JsonProcessingException e) {
                log.error("==> JSON 解析错误: ", e);
            }
        });
        // 返回此用户所拥有的权限
        return permissionsKeys;

    }

    /**
     * 获取此登录用户所拥有的角色
     *
     * @param loginId 登录id
     * @param loginType 登录类型
     * @return
     */
    @Override
    @SneakyThrows
    public List<String> getRoleList(Object loginId, String loginType) {


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
