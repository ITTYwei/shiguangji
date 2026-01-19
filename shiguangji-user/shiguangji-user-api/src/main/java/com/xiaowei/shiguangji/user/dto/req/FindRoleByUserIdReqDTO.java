package com.xiaowei.shiguangji.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: 根据用户Id获取该用户角色
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindRoleByUserIdReqDTO {
    private Long userId;
}
