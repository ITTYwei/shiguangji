package com.xiaowei.shiguangji.auth.controller;

import com.xiaowei.framework.biz.operationlog.aspect.ApiOperationLog;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.model.vo.verificationcodeSend.SendVerificationCodeReqVO;
import com.xiaowei.shiguangji.auth.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VerificationCodeController {

    @Resource
    private VerificationCodeService verificationCodeService;

    @PostMapping("/verification/code/send")
    @ApiOperationLog(description = "发送短信验证码")
    public Response<?> send(@Validated @RequestBody SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        return verificationCodeService.send(sendVerificationCodeReqVO);
    }

}