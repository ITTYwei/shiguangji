package com.xiaowei.shiguangji.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.xiaowei.framework.biz.context.holder.LoginUserContextHolder;
import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.shiguangji.auth.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.auth.enums.LoginTypeEnum;
import com.xiaowei.shiguangji.auth.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.auth.model.vo.user.UpdatePasswordReqVO;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;
import com.xiaowei.shiguangji.auth.rpc.UserRpcService;
import com.xiaowei.shiguangji.auth.service.AuthService;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author: 魏玉石
 * @data: 2025/12/31
 * @description:
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRpcService userRpcService;

    @Override
    @Transactional
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        String code = userLoginReqVO.getCode();
        String password = userLoginReqVO.getPassword();
        Integer type = userLoginReqVO.getType();
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);
        if (Objects.isNull(loginTypeEnum)) {
            throw new BizException(ResponseCodeEnum.LOGIN_TYPE_ERROR);
        }
        Long userId = null;
        switch (loginTypeEnum) {
            case PASSWORD -> {
                FindUserByPhoneRespDTO userByPhone = userRpcService.findUserByPhone(phone);

            }
            case VERIFICATION_CODE -> {
                // 验证码不能为空
                Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");
                //构建Key
                String redisKey = RedisKeyConstants.buildVerificationCodeKey(phone);
                String sentCode = (String) redisTemplate.opsForValue().get(redisKey);
                sentCode = "123456";
                if (!StringUtils.equals(sentCode, code)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                FindUserByPhoneRespDTO userByPhone = userRpcService.findUserByPhone(phone);

                // 判断是否注册
                if (Objects.isNull(userByPhone)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    userId = userRpcService.registerUser(phone);
                    if (Objects.isNull(userId)){
                        throw new BizException(ResponseCodeEnum.LOGIN_FAIL);
                    }

                } else {
                    // 已注册，则获取其用户 ID
                    userId = userByPhone.getId();
                    //获取该用户的角色,存入redis中
                    FindRoleByUserIdRespDTO roles = userRpcService.findRoleByUserId(userId);

                    List<String> list = roles.getRoleKey();
                    redisTemplate.opsForValue().set(RedisKeyConstants.buildUserRoleKey(userId), JsonUtils.toJsonString(list));
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
     * 退出登录
     *
     * @return
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
        log.info("==> 用户退出登录, userId: {}", userId);

        threadPoolTaskExecutor.submit(() -> {
            Long userId2 = LoginUserContextHolder.getUserId();
            log.info("==> 异步线程中获取 userId: {}", userId2);
        });
        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        // 删除用户角色缓存
        redisTemplate.delete(RedisKeyConstants.buildUserRoleKey(userId));
        log.info("==> 用户退出登录, userId: {}", userId);
        return Response.success();
    }


    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        // 新密码
        String newPassword = updatePasswordReqVO.getNewPassword();
        // 密码加密
        String encodePassword = passwordEncoder.encode(newPassword);

        // 获取当前请求对应的用户 ID
        Long userId = LoginUserContextHolder.getUserId();

        // 更新密码
        userRpcService.updatePassword(encodePassword);

        return Response.success();
    }
}
