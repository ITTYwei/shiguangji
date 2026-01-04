package com.xiaowei.shiguangji.auth.controller;

import com.xiaowei.framework.biz.operationlog.aspect.ApiOperationLog;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.auth.alarm.AlarmInterface;
import com.xiaowei.shiguangji.auth.model.vo.user.UserLoginReqVO;
import com.xiaowei.shiguangji.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 魏玉石
 * @data: 2026/1/2
 * @description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> loginAndRegister(@Validated @RequestBody UserLoginReqVO userLoginReqVO) {
        return userService.loginAndRegister(userLoginReqVO);
    }

    @Resource
    private AlarmInterface alarm;
    @GetMapping("/alarm")
    public String sendAlarm() {
        alarm.send("系统出错啦，犬小哈这个月绩效没了，速度上线解决问题！");
        return "alarm success";
    }


}
