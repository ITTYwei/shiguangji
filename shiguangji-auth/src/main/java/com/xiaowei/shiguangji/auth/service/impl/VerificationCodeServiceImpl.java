package com.xiaowei.shiguangji.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.auth.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.auth.model.vo.verificationcodeSend.SendVerificationCodeReqVO;
import com.xiaowei.shiguangji.auth.service.VerificationCodeService;
import com.xiaowei.shiguangji.auth.sms.AliyunSmsHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: 魏玉石
 * @data: 2025/12/30
 * @description: 验证码
 */
@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private AliyunSmsHelper aliyunSmsHelper;


    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        //手机号
        String phone = sendVerificationCodeReqVO.getPhone();

        //构建验证码Key
        String verificationCodeKey = RedisKeyConstants.buildVerificationCodeKey(phone);

        //判断验证码是否已发送
        if (Boolean.TRUE.equals(redisTemplate.hasKey(verificationCodeKey))) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }
        //生成验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        // 调用第三方短信发送服务
        threadPoolTaskExecutor.submit(() -> {
            String signName = "速通互联验证码"; // 签名，个人测试签名无法修改
            String templateCode = "100001"; // 短信模板编码
            // 短信模板参数，code 表示要发送的验证码；min 表示验证码有时间时长，即 3 分钟
            String templateParam = String.format("{\"code\":\"%s\",\"min\":\"3\"}", verificationCode);
            aliyunSmsHelper.sendMessage(signName, templateCode, phone, templateParam);
        });

        log.info("==> 手机号: {}, 已发送验证码：【{}】", phone, verificationCode);

        // 存储验证码到 redis, 并设置过期时间为 3 分钟
        redisTemplate.opsForValue().set(verificationCodeKey, verificationCode, 3, TimeUnit.MINUTES);

        return Response.success();
    }
}
