package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;

/**
 * 用户配置响应DTO
 *
 * @author VibeCode Team
 */
@Data
public class UserConfigResponse {
    
    /**
     * 用户配置信息（JSON字符串）
     */
    private String config;
} 