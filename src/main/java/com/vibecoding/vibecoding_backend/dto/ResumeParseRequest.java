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
     * 简历名称
     */
    @NotBlank(message = "简历名称不能为空")
    private String fileName;

    /**
     * 响应模式
     */
    private String responseMode = "streaming";
} 