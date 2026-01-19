package com.xiaowei.shiguangji.user.api;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.user.constant.ApiConstants;
import com.xiaowei.shiguangji.user.dto.req.FindRoleByUserIdReqDTO;
import com.xiaowei.shiguangji.user.dto.req.FindUserByPhoneReqDTO;
import com.xiaowei.shiguangji.user.dto.req.RegisterUserReqDTO;
import com.xiaowei.shiguangji.user.dto.req.UpdateUserPasswordReqDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface UserFeignApi {

    String PREFIX = "/user";

    /**
     * 用户注册
     *
     * @param registerUserReqDTO
     * @return
     */
    @PostMapping(value = PREFIX + "/register")
    Response<Long> registerUser(@RequestBody RegisterUserReqDTO registerUserReqDTO);

    /**
     * 根据手机号查询用户信息
     *
     * @param findUserByPhoneReqDTO
     * @return
     */
    @PostMapping(value = PREFIX + "/findByPhone")
    Response<FindUserByPhoneRespDTO> findByPhone(@RequestBody FindUserByPhoneReqDTO findUserByPhoneReqDTO);

    /**
     * 根据用户Id获取该用户角色
     *
     * @param findRoleByUserIdReqDTO
     * @return
     */
    @PostMapping(value = PREFIX + "/findRoleByUserId")
    Response<FindRoleByUserIdRespDTO> findRoleByUserId(@RequestBody FindRoleByUserIdReqDTO findRoleByUserIdReqDTO);

    /**
     * 更新密码
     *
     * @param updateUserPasswordReqDTO
     * @return
     */
    @PostMapping(value = PREFIX + "/password/update")
    Response<?> updatePassword(@RequestBody UpdateUserPasswordReqDTO updateUserPasswordReqDTO);

}