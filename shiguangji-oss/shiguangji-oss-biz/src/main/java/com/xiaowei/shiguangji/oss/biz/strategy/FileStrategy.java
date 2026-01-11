package com.xiaowei.shiguangji.oss.biz.strategy;

import org.springframework.web.multipart.MultipartFile;
/**
 * @author: 魏玉石
 * @data: 2026/1/11
 * @description:
 */
public interface FileStrategy {

    /**
     * 文件上传
     * 
     * @param file
     * @param bucketName
     * @return
     */
    String uploadFile(MultipartFile file, String bucketName);

}