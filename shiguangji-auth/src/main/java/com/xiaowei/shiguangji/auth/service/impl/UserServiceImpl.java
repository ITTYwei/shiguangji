package com.xiaowei.shiguangji.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.xiaowei.framework.common.enums.DeletedEnum;
import com.xiaowei.framework.common.enums.StatusEnum;
import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.shiguangji.auth.config.RedisTemplateConfig;
import com.xiaowei.shiguangji.auth.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.auth.constant.RoleConstants;
import com.xiaowei.shiguangji.auth.domain.dataobject.UserDO;
import com.xiaowei.shiguangji.auth.domain.dataobject.UserRoleRelDO;
import com.xiaowei.shiguangji.auth.domain.mapper.UserDOMapper;
import com.xiaowei.shiguangji.auth.domain.mapper.UserRoleRelDOMapper;
import com.xiaowei.shiguangji.auth.enums.LoginTypeEnum;
import com.xiaowei.shiguangji.auth.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;
import com.xiaowei.shiguangji.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author: 魏玉石
 * @data: 2025/12/31
 * @description:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserRoleRelDOMapper userRoleDOMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        String code = userLoginReqVO.getCode();
        String password = userLoginReqVO.getPassword();
        Integer type = userLoginReqVO.getType();
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        Long userId = null;
        switch (loginTypeEnum) {

            case PASSWORD -> {
                UserDO userDO = userDOMapper.selectByPhone(phone);


            }
            case VERIFICATION_CODE -> {
                // 验证码不能为空
                Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");
                //构建Key
                String redisKey = RedisKeyConstants.buildVerificationCodeKey(phone);
                String sentCode = (String) redisTemplate.opsForValue().get(redisKey);
                if (!StringUtils.equals(sentCode, code)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                UserDO userDO = userDOMapper.selectByPhone(phone);
                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));
                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    registerUser(phone);

                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                }
            }
            default -> throw new BizException(ResponseCodeEnum.LOGIN_TYPE_ERROR);
        }




        // SaToken 登录用户, 入参为用户 ID
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 系统自动注册用户
     *
     * @param phone
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                //构建全局 userId
                Long shiguangjiId = redisTemplate.opsForValue().increment(RedisKeyConstants.SHIGUANGJI_ID_GENERATOR_KEY);
                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .shiguangjiId(String.valueOf(shiguangjiId)) // 自动生成拾光号 ID
                        .nickname("时光" + shiguangjiId) // 自动生成昵称
                        .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                        .build();
                userDOMapper.insert(userDO);
                Long userId = userDO.getId();
                log.info("==> 用户注册成功, userDO: {}", JsonUtils.toJsonString(userDO));
                //添加给用户角色
                UserRoleRelDO userRoleRelDO = UserRoleRelDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleDOMapper.insert(userRoleRelDO);
                log.info("==> 用户添加角色成功, userRoleRelDO: {}", JsonUtils.toJsonString(userRoleRelDO));
                // 将该用户的角色 ID 存入 Redis 中
                List<Long> roles = Lists.newArrayList();
                roles.add(RoleConstants.COMMON_USER_ROLE_ID);
                String userRolesKey = RedisKeyConstants.buildUserRoleKey(phone);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));


                return userId;
            }catch (Exception e) {
                status.setRollbackOnly(); // 标记事务为回滚
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });
    }
}
