package com.xiaowei.shiguangji.auth.alarm.impl;

import com.xiaowei.shiguangji.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: 魏玉石
 * @data: 2026/1/4
 * @description: 短信通知助手
 */
@Slf4j
public class SmsAlarmHelper implements AlarmInterface {

    /**
     * 发送告警信息
     *
     * @param message
     * @return
     */
    @Override
    public boolean send(String message) {
        log.info("==> 【短信告警】：{}", message);
        
        // 业务逻辑...
        
        return true;
    }
}
