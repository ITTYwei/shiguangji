package com.xiaowei.shiguangji.oss.biz.service;

import com.xiaowei.framework.common.response.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: 魏玉石
 * @data: 2026/1/11
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    Response<?> uploadFile(MultipartFile file);
}
