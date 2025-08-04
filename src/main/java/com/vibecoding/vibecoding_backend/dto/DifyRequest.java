package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Dify请求DTO
 *
 * @author VibeCode Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifyRequest {

    /**
     * 用户参数
     */
    private String user;

    /**
     * 响应模式
     */
    private String responseMode;

    /**
     * 输入参数
     */
    private Map<String, Object> inputs;

    /**
     * 构建简历解析请求
     *
     * @param resumeUrl 简历URL
     * @param user 用户标识
     * @param responseMode 响应模式
     * @return DifyRequest
     */
    public static DifyRequest buildResumeRequest(String resumeUrl, String user, String responseMode) {
        Map<String, Object> resumeFileInput = Map.of(
            "type", "document",
            "transfer_method", "remote_url",
            "url", resumeUrl
        );

        Map<String, Object> inputs = Map.of(
            "resumeFile", List.of(resumeFileInput)
        );

        return DifyRequest.builder()
                .user(user)
                .responseMode(responseMode)
                .inputs(inputs)
                .build();
    }
} 