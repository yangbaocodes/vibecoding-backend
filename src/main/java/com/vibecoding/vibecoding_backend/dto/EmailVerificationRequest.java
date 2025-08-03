package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 邮箱验证码请求DTO
 *
 * @author VibeCode Team
 */
@Data
public class EmailVerificationRequest {
    
    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
} 