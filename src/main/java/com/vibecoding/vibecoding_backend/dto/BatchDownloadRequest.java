package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 批量下载请求DTO
 *
 * @author VibeCode Team
 */
@Data
public class BatchDownloadRequest {

    /**
     * 文件名列表
     */
    @NotEmpty(message = "文件名列表不能为空")
    @Size(max = 10, message = "单次最多下载10个文件")
    private List<String> filenames;
} 