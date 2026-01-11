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
public class MinioFileStrategy implements FileStrategy {

    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至 Minio ...");
        return null;
    }
}