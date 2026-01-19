package com.xiaowei.shiguangji.user.biz.service;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.user.biz.model.vo.UpdateUserInfoReqVO;
import com.xiaowei.shiguangji.user.dto.req.FindRoleByUserIdReqDTO;
import com.xiaowei.shiguangji.user.dto.req.FindUserByPhoneReqDTO;
import com.xiaowei.shiguangji.user.dto.req.RegisterUserReqDTO;
import com.xiaowei.shiguangji.user.dto.req.UpdateUserPasswordReqDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;

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

    /**
     * 用户注册
     *
     * @param registerUserReqDTO
     * @return
     */
    Response<Long> register(RegisterUserReqDTO registerUserReqDTO);

    /**
     * 根据手机号查询用户信息
     *
     * @param findUserByPhoneReqDTO
     * @return
     */
    Response<FindUserByPhoneRespDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO);

    /**
     * 根据用户Id获取该用户角色
     *
     * @param findRoleByUserIdReqDTO
     * @return
     */
    Response<FindRoleByUserIdRespDTO> findRoleByUserId(FindRoleByUserIdReqDTO findRoleByUserIdReqDTO);
    /**
     * 更新密码
     *
     * @param updateUserPasswordReqDTO
     * @return
     */
    Response<?> updatePassword(UpdateUserPasswordReqDTO updateUserPasswordReqDTO);

}
