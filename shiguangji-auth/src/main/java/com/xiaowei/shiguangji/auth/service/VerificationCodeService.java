package com.xiaowei.shiguangji.auth.service;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.model.vo.verificationcodeSend.SendVerificationCodeReqVO;

public interface VerificationCodeService {

    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVO
     * @return
     */
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}