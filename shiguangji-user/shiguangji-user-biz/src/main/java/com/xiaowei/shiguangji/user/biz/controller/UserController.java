package com.xiaowei.shiguangji.user.biz.controller;

import com.xiaowei.framework.biz.operationlog.aspect.ApiOperationLog;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.user.biz.model.vo.UpdateUserInfoReqVO;
import com.xiaowei.shiguangji.user.biz.service.UserService;
import com.xiaowei.shiguangji.user.dto.req.FindRoleByUserIdReqDTO;
import com.xiaowei.shiguangji.user.dto.req.FindUserByPhoneReqDTO;
import com.xiaowei.shiguangji.user.dto.req.RegisterUserReqDTO;
import com.xiaowei.shiguangji.user.dto.req.UpdateUserPasswordReqDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户信息修改
     * 
     * @param updateUserInfoReqVO
     * @return
     */
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> updateUserInfo(@Validated UpdateUserInfoReqVO updateUserInfoReqVO) {
        return userService.updateUserInfo(updateUserInfoReqVO);
    }

    // ===================================== 对其他服务提供的接口 =====================================
    @PostMapping("/register")
    @ApiOperationLog(description = "用户注册")
    public Response<Long> register(@Validated @RequestBody RegisterUserReqDTO registerUserReqDTO) {
        return userService.register(registerUserReqDTO);
    }

    @PostMapping("/findByPhone")
    @ApiOperationLog(description = "手机号查询用户信息")
    public Response<FindUserByPhoneRespDTO> findByPhone(@Validated @RequestBody FindUserByPhoneReqDTO findUserByPhoneReqDTO) {
        return userService.findByPhone(findUserByPhoneReqDTO);
    }
    @PostMapping("/findRoleByUserId")
    @ApiOperationLog(description = "根据用户Id获取该用户角色")
    public Response<FindRoleByUserIdRespDTO> findRoleByUserId(@Validated @RequestBody FindRoleByUserIdReqDTO findRoleByUserIdReqDTO) {
        return userService.findRoleByUserId(findRoleByUserIdReqDTO);
    }

    @PostMapping("/password/update")
    @ApiOperationLog(description = "密码更新")
    public Response<?> updatePassword(@Validated @RequestBody UpdateUserPasswordReqDTO updateUserPasswordReqDTO) {
        return userService.updatePassword(updateUserPasswordReqDTO);
    }

}