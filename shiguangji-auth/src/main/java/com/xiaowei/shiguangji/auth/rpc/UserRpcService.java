package com.xiaowei.shiguangji.auth.rpc;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.user.api.UserFeignApi;
import com.xiaowei.shiguangji.user.dto.req.FindRoleByUserIdReqDTO;
import com.xiaowei.shiguangji.user.dto.req.FindUserByPhoneReqDTO;
import com.xiaowei.shiguangji.user.dto.req.RegisterUserReqDTO;
import com.xiaowei.shiguangji.user.dto.req.UpdateUserPasswordReqDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: 用户服务
 */
@Component
public class UserRpcService {

    @Resource
    private UserFeignApi userFeignApi;

    /**
     * 用户注册
     *
     * @param phone
     * @return
     */
    public Long registerUser(String phone) {
        RegisterUserReqDTO registerUserReqDTO = new RegisterUserReqDTO();
        registerUserReqDTO.setPhone(phone);
        Response<Long> response = userFeignApi.registerUser(registerUserReqDTO);
        if (!response.isSuccess()) {
            return null;
        }
        return response.getData();
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone
     * @return
     */
    public FindUserByPhoneRespDTO findUserByPhone(String phone) {
        FindUserByPhoneReqDTO findUserByPhoneReqDTO = new FindUserByPhoneReqDTO();
        findUserByPhoneReqDTO.setPhone(phone);

        Response<FindUserByPhoneRespDTO> response = userFeignApi.findByPhone(findUserByPhoneReqDTO);

        if (!response.isSuccess()) {
            return null;
        }

        return response.getData();
    }


    /**
     * 根据用户Id获取该用户角色
     *
     * @param userId
     * @return
     */
    public FindRoleByUserIdRespDTO findRoleByUserId(Long userId) {
        FindRoleByUserIdReqDTO findRoleByUserIdReqDTO = FindRoleByUserIdReqDTO.builder()
                .userId(userId)
                .build();
        Response<FindRoleByUserIdRespDTO> response = userFeignApi.findRoleByUserId(findRoleByUserIdReqDTO);
        if (!response.isSuccess()) {
            return null;
        }
        return response.getData();
    }

    /**
     * 密码更新
     *
     * @param encodePassword
     */
    public void updatePassword(String encodePassword) {
        UpdateUserPasswordReqDTO updateUserPasswordReqDTO = new UpdateUserPasswordReqDTO();
        updateUserPasswordReqDTO.setEncodePassword(encodePassword);

        userFeignApi.updatePassword(updateUserPasswordReqDTO);
    }

}
