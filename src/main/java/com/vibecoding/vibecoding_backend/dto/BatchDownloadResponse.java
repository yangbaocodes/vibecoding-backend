package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;

/**
 * 批量下载响应DTO
 *
 * @author VibeCode Team
 */
@Data
public class BatchDownloadResponse {

    /**
     * 下载URL
     */
    private String downloadUrl;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 是否为压缩文件
     */
    private Boolean isZip;

    /**
     * 文件数量
     */
    private Integer fileCount;

    /**
     * 消息
     */
    private String message;
} 