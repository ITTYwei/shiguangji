package com.xiaowei.shiguangji.auth.service;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.model.vo.user.UpdatePasswordReqVO;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;

/**
 * @description:
 * @author: 魏玉石
 * @data: 2025/12/31
 */
public interface AuthService {

    /**
     * 登录与注册
     *
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);
    /**
     * 退出登录
     * @return
     */
    Response<?> logout();

    /**
     * 修改密码
     * @param updatePasswordReqVO
     * @return
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
