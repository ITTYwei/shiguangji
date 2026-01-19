package com.xiaowei.shiguangji.user.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: 根据手机号查询用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByPhoneRespDTO {

    private Long id;

    private String password;
}
