package com.xiaowei.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: 魏玉石
 * @data: 2026/1/2
 * @description: 逻辑删除
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    YES(true),
    NO(false);

    private final Boolean value;
}
