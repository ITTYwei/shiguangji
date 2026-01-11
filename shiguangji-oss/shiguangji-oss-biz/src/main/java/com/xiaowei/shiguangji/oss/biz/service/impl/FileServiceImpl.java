package com.xiaowei.shiguangji.oss.biz.service.impl;

import com.xiaowei.framework.common.response.Response;
import com.xiaowei.shiguangji.oss.biz.service.FileService;
import com.xiaowei.shiguangji.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 魏玉石
 * @data: 2026/1/11
 * @description:
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    @Override
    public Response<?> uploadFile(MultipartFile file) {
        // 上传文件到
        fileStrategy.uploadFile(file, "shiguangji");

        return Response.success();
    }
}
