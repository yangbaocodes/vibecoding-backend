package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    /**
     * 目标语言
     */
    @NotBlank(message = "目标语言不能为空")
    @Pattern(regexp = "^(en|zh)$", message = "目标语言只能是 en 或 zh")
    private String targetLanguage;

    /**
     * 目标文件类型
     */
    @Pattern(regexp = "^(word|ppt)$", message = "目标文件类型只能是 word 或 ppt")
    private String targetFileType = "word";
} 