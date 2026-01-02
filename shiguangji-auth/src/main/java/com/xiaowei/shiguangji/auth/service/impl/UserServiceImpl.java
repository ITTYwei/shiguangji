package com.xiaowei.shiguangji.auth.service.impl;

import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.shiguangji.auth.config.RedisTemplateConfig;
import com.xiaowei.shiguangji.auth.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.auth.domain.dataobject.UserDO;
import com.xiaowei.shiguangji.auth.domain.mapper.UserDOMapper;
import com.xiaowei.shiguangji.auth.enums.LoginTypeEnum;
import com.xiaowei.shiguangji.auth.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;
import com.xiaowei.shiguangji.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
                if (StringUtils.isBlank(code)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                //构建Key
                String redisKey = RedisKeyConstants.buildVerificationCodeKey(phone);
                String sentCode = (String) redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.equals(sentCode, code)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                UserDO userDO = userDOMapper.selectByPhone(phone);
                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));
                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    // todo

                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                }
                break;
            }


        }

        // SaToken 登录用户，并返回 token 令牌
        // todo

        return Response.success("");
    }
}
