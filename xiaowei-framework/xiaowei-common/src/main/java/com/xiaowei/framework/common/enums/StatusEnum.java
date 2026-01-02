package com.xiaowei.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: xiaowei
 * @date: 2026/1/2 15:50
 * @description: 状态
 **/
@Getter
@AllArgsConstructor
public enum StatusEnum {
    // 启用
    ENABLE(0),
    // 禁用
    DISABLED(1);

    private final Integer value;
}