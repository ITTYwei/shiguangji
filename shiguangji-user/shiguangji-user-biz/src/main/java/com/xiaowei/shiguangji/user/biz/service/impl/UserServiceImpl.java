package com.xiaowei.shiguangji.user.biz.service.impl;

import com.google.common.base.Preconditions;
import com.xiaowei.framework.biz.context.holder.LoginUserContextHolder;
import com.xiaowei.framework.common.exception.BizException;
import com.xiaowei.framework.common.response.Response;
import com.xiaowei.framework.common.util.ParamUtils;
import com.xiaowei.shiguangji.oss.api.FileFeignApi;
import com.xiaowei.shiguangji.user.biz.domain.dataobject.UserDO;
import com.xiaowei.shiguangji.user.biz.domain.mapper.UserDOMapper;
import com.xiaowei.shiguangji.user.biz.enums.ResponseCodeEnum;
import com.xiaowei.shiguangji.user.biz.enums.SexEnum;
import com.xiaowei.shiguangji.user.biz.model.vo.UpdateUserInfoReqVO;
import com.xiaowei.shiguangji.user.biz.rpc.OssRpcService;
import com.xiaowei.shiguangji.user.biz.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        userDO.setId(LoginUserContextHolder.getUserId());
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

}
