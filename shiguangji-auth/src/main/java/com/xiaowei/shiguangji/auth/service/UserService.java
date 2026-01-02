package com.xiaowei.shiguangji.auth.service;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;

/**
 * @description:
 * @author: 魏玉石
 * @data: 2025/12/31
 */
public interface UserService {

    /**
     * 登录与注册
     *
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);
}
