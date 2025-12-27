package com.xiaowei.shiguangji.auth.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: xiaowei
 * @url: www.xiaowei.com
 * @date: 2025/5/5 15:21
 * @description: TODO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User2 {
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private Integer sex;

}
