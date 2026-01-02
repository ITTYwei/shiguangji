package com.xiaowei.shiguangji.auth.domain.mapper;

import com.xiaowei.shiguangji.auth.domain.dataobject.UserDO;

/**
 * @description: 用户DOMapper
 * @author: 魏玉石
 * @data: 2025/12/31
 */
public interface UserDOMapper {
    /**
     * 根据手机号查询记录
     *
     * @param phone
     * @return
     */
    UserDO selectByPhone(String phone);
}
