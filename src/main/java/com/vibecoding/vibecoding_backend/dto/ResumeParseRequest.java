package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 简历解析请求DTO
 *
 * @author VibeCode Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeParseRequest {

    /**
     * 简历URL
     */
    @NotBlank(message = "简历URL不能为空")
    private String resumeUrl;

    /**
     * 用户标识
     */
    private String user = "2938922@qq.com";

    /**
     * 响应模式
     */
    private String responseMode = "streaming";
} 