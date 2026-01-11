package com.xiaowei.shiguangji.oss.biz.strategy.impl;

import com.xiaowei.shiguangji.oss.biz.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author: 魏玉石
 * @data: 2026/1/11
 * @description:
 */
@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {

    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至阿里云 OSS ...");
        return null;
    }
}