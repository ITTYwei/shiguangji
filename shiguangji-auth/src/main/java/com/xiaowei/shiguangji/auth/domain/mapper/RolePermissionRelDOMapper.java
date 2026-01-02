package com.xiaowei.shiguangji.auth.domain.mapper;

import com.xiaowei.shiguangji.auth.domain.dataobject.RolePermissionRelDO;

public interface RolePermissionRelDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionRelDO record);

    int insertSelective(RolePermissionRelDO record);

    RolePermissionRelDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionRelDO record);

    int updateByPrimaryKey(RolePermissionRelDO record);
}