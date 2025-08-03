package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 注册请求DTO
 *
 * @author VibeCode Team
 */
@Data
public class RegisterRequest {

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须是6位数字")
    private String verificationCode;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
}