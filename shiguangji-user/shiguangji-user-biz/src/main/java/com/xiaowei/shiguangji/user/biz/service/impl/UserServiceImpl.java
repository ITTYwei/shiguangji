package com.xiaowei.shiguangji.user.biz.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.base.Preconditions;
import com.xiaowei.framework.biz.context.holder.LoginUserContextHolder;
import com.xiaowei.framework.common.enums.DeletedEnum;
import com.xiaowei.framework.common.enums.StatusEnum;
import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.framework.common.util.JsonUtils;
import com.xiaowei.framework.common.util.ParamUtils;
import com.xiaowei.shiguangji.user.biz.constant.RedisKeyConstants;
import com.xiaowei.shiguangji.user.biz.constant.RoleConstants;
import com.xiaowei.shiguangji.user.biz.domain.dataobject.RoleDO;
import com.xiaowei.shiguangji.user.biz.domain.dataobject.UserDO;
import com.xiaowei.shiguangji.user.biz.domain.dataobject.UserRoleRelDO;
import com.xiaowei.shiguangji.user.biz.domain.mapper.RoleDOMapper;
import com.xiaowei.shiguangji.user.biz.domain.mapper.UserDOMapper;
import com.xiaowei.shiguangji.user.biz.domain.mapper.UserRoleRelDOMapper;
import com.xiaowei.shiguangji.user.biz.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.user.biz.enums.SexEnum;
import com.xiaowei.shiguangji.user.biz.model.vo.UpdateUserInfoReqVO;
import com.xiaowei.shiguangji.user.biz.rpc.OssRpcService;
import com.xiaowei.shiguangji.user.biz.service.UserService;
import com.xiaowei.shiguangji.user.dto.req.FindRoleByUserIdReqDTO;
import com.xiaowei.shiguangji.user.dto.req.FindUserByPhoneReqDTO;
import com.xiaowei.shiguangji.user.dto.req.RegisterUserReqDTO;
import com.xiaowei.shiguangji.user.dto.req.UpdateUserPasswordReqDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindRoleByUserIdRespDTO;
import com.xiaowei.shiguangji.user.dto.resp.FindUserByPhoneRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: 魏玉石
 * @data: 2026/1/15
 * @description: 用户业务
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private OssRpcService ossRpcService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private UserRoleRelDOMapper userRoleDOMapper;



    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO
     * @return
     */
    @Override
    public Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO) {
        UserDO userDO = new UserDO();
        // 设置当前需要更新的用户 ID
        Long userId = LoginUserContextHolder.getUserId();
        userDO.setId(userId);
        // 标识位：是否需要更新
        boolean needUpdate = false;

        // 头像
        MultipartFile avatarFile = updateUserInfoReqVO.getAvatar();

        if (Objects.nonNull(avatarFile)) {
            String avatarUrl = ossRpcService.uploadFile(avatarFile);
            // 若上传头像失败，则抛出业务异常
            if (StringUtils.isBlank(avatarUrl)) {
                log.error("==> 调用 oss 服务上传头像失败");
                throw new BizException(ResponseCodeEnum.UPLOAD_AVATAR_FAIL);
            }
            log.info("==> 调用 oss 服务成功，上传头像，url：{}", avatarUrl);

            userDO.setAvatar(avatarUrl);
        }

        // 昵称
        String nickname = updateUserInfoReqVO.getNickname();
        if (StringUtils.isNotBlank(nickname)) {
            Preconditions.checkArgument(ParamUtils.checkNickname(nickname), ResponseCodeEnum.NICK_NAME_VALID_FAIL.getErrorMessage());
            userDO.setNickname(nickname);
            needUpdate = true;
        }

        // 小哈书号
        String shiguangjiId = updateUserInfoReqVO.getShiguangjiId();
        if (StringUtils.isNotBlank(shiguangjiId)) {
            Preconditions.checkArgument(ParamUtils.checkShiguangjiId(shiguangjiId), ResponseCodeEnum.SHIGUANGJI_ID_VALID_FAIL.getErrorMessage());
            userDO.setShiguangjiId(shiguangjiId);
            needUpdate = true;
        }

        // 性别
        Integer sex = updateUserInfoReqVO.getSex();
        if (Objects.nonNull(sex)) {
            Preconditions.checkArgument(SexEnum.isValid(sex), ResponseCodeEnum.SEX_VALID_FAIL.getErrorMessage());
            userDO.setSex(sex);
            needUpdate = true;
        }

        // 生日
        LocalDate birthday = updateUserInfoReqVO.getBirthday();
        if (Objects.nonNull(birthday)) {
            userDO.setBirthday(birthday);
            needUpdate = true;
        }

        // 个人简介
        String introduction = updateUserInfoReqVO.getIntroduction();
        if (StringUtils.isNotBlank(introduction)) {
            Preconditions.checkArgument(ParamUtils.checkLength(introduction, 100), ResponseCodeEnum.INTRODUCTION_VALID_FAIL.getErrorMessage());
            userDO.setIntroduction(introduction);
            needUpdate = true;
        }

        // 背景图
        MultipartFile backgroundImgFile = updateUserInfoReqVO.getBackgroundImg();
        if (Objects.nonNull(backgroundImgFile)) {
            String backgroundImgUrl = ossRpcService.uploadFile(backgroundImgFile);
            // 若上传背景图失败，则抛出业务异常
            if (StringUtils.isBlank(backgroundImgUrl)) {
                log.error("==> 调用 oss 服务上传背景图失败");
                throw new BizException(ResponseCodeEnum.UPLOAD_AVATAR_FAIL);
            }
            log.info("==> 调用 oss 服务成功，上传背景图，url：{}", backgroundImgUrl);
            userDO.setBackgroundImg(backgroundImgUrl);
            needUpdate = true;
        }

        if (needUpdate) {
            // 更新用户信息
            userDO.setUpdateTime(LocalDateTime.now());
            userDOMapper.updateByPrimaryKeySelective(userDO);
        }
        return Response.success();
    }


    /**
     * 系统自动注册用户
     *
     * @param registerUserReqDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Response<Long> register(RegisterUserReqDTO registerUserReqDTO) {

        String phone = registerUserReqDTO.getPhone();
        // 先判断该手机号是否已被注册
        UserDO userDO1 = userDOMapper.selectByPhone(phone);
        log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO1));
        // 若已注册，则直接返回用户 ID
        if (Objects.nonNull(userDO1)) {
            return Response.success(userDO1.getId());
        }
        // 否则注册新用户
        // 获取全局自增的拾光集 ID
        Long shiguangjiId = redisTemplate.opsForValue().increment(RedisKeyConstants.SHIGUANGJI_ID_GENERATOR_KEY);
        UserDO userDO = UserDO.builder()
                .phone(phone)
                .shiguangjiId(String.valueOf(shiguangjiId)) // 自动生成拾光号 ID
                .nickname("时光" + shiguangjiId) // 自动生成昵称
                .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                .build();
        userDOMapper.insert(userDO);
        Long userId = userDO.getId();
        log.debug("==> 用户注册成功, userDO: {}", JsonUtils.toJsonString(userDO));

        //添加给用户角色
        UserRoleRelDO userRoleRelDO = UserRoleRelDO.builder()
                .userId(userId)
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue())
                .build();
        userRoleDOMapper.insert(userRoleRelDO);
        log.debug("==> 用户添加角色成功, userRoleRelDO: {}", JsonUtils.toJsonString(userRoleRelDO));
        // 将该用户的角色 ID 存入 Redis 中，指定初始容量为 1，这样可以减少在扩容时的性能开销
        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

        List<String> roleKey = new ArrayList<>(1);
        roleKey.add(roleDO.getRoleKey());
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roleKey));
        return Response.success(userId);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param findUserByPhoneReqDTO
     * @return
     */
    @Override
    public Response<FindUserByPhoneRespDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO) {
        String phone = findUserByPhoneReqDTO.getPhone();

        // 根据手机号查询用户信息
        UserDO userDO = userDOMapper.selectByPhone(phone);

        // 判空
        if (Objects.isNull(userDO)) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        // 构建返参
        FindUserByPhoneRespDTO findUserByPhoneRespDTO = FindUserByPhoneRespDTO.builder()
                .id(userDO.getId())
                .password(userDO.getPassword())
                .build();

        return Response.success(findUserByPhoneRespDTO);
    }

    @Override
    public Response<FindRoleByUserIdRespDTO> findRoleByUserId(FindRoleByUserIdReqDTO findRoleByUserIdReqDTO) {
        Long userId = findRoleByUserIdReqDTO.getUserId();
        List<RoleDO> roleDOS = roleDOMapper.selectRoleByUserId(userId);
        if (CollectionUtils.isNotEmpty(roleDOS)) {
            throw new BizException(ResponseCodeEnum.USER_ROLE_NOT_FOUND);
        }
        List<String> roleKeys = roleDOS.stream().map(RoleDO::getRoleKey).toList();
        FindRoleByUserIdRespDTO findRoleByUserIdRespDTO = FindRoleByUserIdRespDTO.builder()
                .roleKey(roleKeys)
                .build();
        return Response.success(findRoleByUserIdRespDTO);
    }

    /**
     * 更新密码
     *
     * @param updateUserPasswordReqDTO
     * @return
     */
    @Override
    public Response<?> updatePassword(UpdateUserPasswordReqDTO updateUserPasswordReqDTO) {
        // 获取当前请求对应的用户 ID
        Long userId = LoginUserContextHolder.getUserId();

        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(updateUserPasswordReqDTO.getEncodePassword()) // 加密后的密码
                .updateTime(LocalDateTime.now())
                .build();
        // 更新密码
        userDOMapper.updateByPrimaryKeySelective(userDO);

        return Response.success();
    }

}
