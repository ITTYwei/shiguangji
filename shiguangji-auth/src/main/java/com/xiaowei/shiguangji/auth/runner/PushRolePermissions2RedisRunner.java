package com.xiaowei.shiguangji.auth.runner;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.shiguangji.auth.config.RedisTemplateConfig;
import com.xiaowei.shiguangji.auth.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.auth.domain.dataobject.PermissionDO;
import com.xiaowei.shiguangji.auth.domain.dataobject.RoleDO;
import com.xiaowei.shiguangji.auth.domain.dataobject.RolePermissionRelDO;
import com.xiaowei.shiguangji.auth.domain.mapper.PermissionDOMapper;
import com.xiaowei.shiguangji.auth.domain.mapper.RoleDOMapper;
import com.xiaowei.shiguangji.auth.domain.mapper.RolePermissionRelDOMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: 魏玉石
 * @data: 2026/1/2
 * @description: 推送角色权限数据到 Redis 中
 */
@Component
@Slf4j
public class PushRolePermissions2RedisRunner implements ApplicationRunner {

    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private RolePermissionRelDOMapper rolePermissionRelDOMapper;
    @Resource
    private PermissionDOMapper permissionDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 权限同步标记 Key
    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";

    /**
     * 查询所有已经启用的角色,通过角色查询对应的权限,缓存到redis中
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("==> 服务启动，开始同步角色权限数据到 Redis 中...");
        try {

            // 是否能够同步数据: 原子操作，只有在键 PUSH_PERMISSION_FLAG 不存在时，才会设置该键的值为 "1"，并设置过期时间为 1 天
            Boolean canPushed = redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS);

            // 如果无法同步权限数据
            if (Boolean.FALSE.equals(canPushed)) {
                log.warn("==> 角色权限数据已经同步至 Redis 中，不再同步...");
                return;
            }
            // 查询出所有角色
            List<RoleDO> roleDOS = roleDOMapper.selectEnabledList();
            if (CollUtil.isEmpty(roleDOS)) {
                log.warn("==> 没有找到启用的角色");
                return;
            }
            // 拿到所有角色的 ID
            List<Long> roleIds = roleDOS.stream().map(RoleDO::getId)
                    .toList();
            // 根据角色 ID, 批量查询出所有角色对应的权限
            List<RolePermissionRelDO> rolePermissionRelDOS = rolePermissionRelDOMapper.selectByRoleIds(roleIds);
            if (CollUtil.isEmpty(rolePermissionRelDOS)) {
                log.warn("==> 没有找到角色对应的权限");
                return;
            }
            log.debug("==> 根据ID,查询出的权限:{}", rolePermissionRelDOS);
            // 按角色 ID 分组, 每个角色 ID 对应多个权限 ID
            Map<Long, List<Long>> rolePermissionMap = rolePermissionRelDOS.stream()
                    .collect(Collectors.groupingBy(
                            RolePermissionRelDO::getRoleId,
                            Collectors.mapping(RolePermissionRelDO::getPermissionId, Collectors.toList())
                    ));
            // 查询 APP 端所有被启用的权限
            List<PermissionDO> permissionDOS = permissionDOMapper.selectAppEnabledList();
            if (CollUtil.isEmpty(permissionDOS)) {
                log.warn("==> 没有找到启用的权限数据");
                return;
            }
            Map<Long, PermissionDO> permissionIdDOMap = permissionDOS.stream().collect(
                    Collectors.toMap(PermissionDO::getId, permissionDO -> permissionDO)
            );


            //组织关系
            HashMap<Long, List<PermissionDO>> roleIdPermissionDOMap = Maps.newHashMap();
            roleDOS.forEach(roleDO -> {
                Long roleDOId = roleDO.getId();
                //根据角色ID查询权限id
                List<Long> permissionIds = rolePermissionMap.get(roleDOId);
                if (CollUtil.isNotEmpty(permissionIds)) {
                    ArrayList<PermissionDO> permissionDOArrayList = new ArrayList<>();
                    permissionIds.forEach(permissionId -> {
                        PermissionDO permissionDO = permissionIdDOMap.get(permissionId);
                        if (ObjectUtil.isNotEmpty(permissionDO)) {
                            permissionDOArrayList.add(permissionDO);
                        }
                    });
                    roleIdPermissionDOMap.put(roleDOId, permissionDOArrayList);
                }
            });
            // 同步至 Redis 中，方便后续网关查询鉴权使用
            roleIdPermissionDOMap.forEach((roleId, permissions) -> {
                String key = RedisKeyConstants.buildRolePermissionsKey(roleId);
                redisTemplate.opsForValue().set(key, JsonUtils.toJsonString(permissions));
            });

            log.info("==> 服务启动，成功同步角色权限数据到 Redis 中...");

        } catch (Exception e) {
            log.error("==> 同步角色权限数据到 Redis 中失败: ", e);
        }


    }
}
