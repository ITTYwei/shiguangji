package com.xiaowei.shiguangji.user.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 魏玉石
 * @data: 2026/1/18
 * @description: 根据用户Id获取该用户角色
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindRoleByUserIdRespDTO {
    private List<String> roleKey;
}
