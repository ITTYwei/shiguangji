package com.xiaowei.shiguangji.user.biz.service;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.user.biz.model.vo.UpdateUserInfoReqVO;

/**
 * @author: 魏玉石
 * @data: 2026/1/15
 * @description: 用户业务
 */
public interface UserService {

    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO
     * @return
     */
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);
}
