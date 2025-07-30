package com.vibecoding.vibecoding_backend.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author VibeCode Team
 */
@Getter
public enum ResultCode {
    
    // 通用状态码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 参数相关 4xx
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    PARAM_VALIDATION_ERROR(422, "参数校验失败"),
    
    // 业务相关 5xx
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    // 认证相关 1xxx
    LOGIN_REQUIRED(1001, "请先登录"),
    TOKEN_INVALID(1002, "Token无效"),
    TOKEN_EXPIRED(1003, "Token已过期"),
    ACCOUNT_DISABLED(1004, "账户已禁用"),
    PASSWORD_ERROR(1005, "密码错误"),
    
    // 用户相关 2xxx
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_ALREADY_EXISTS(2002, "用户已存在"),
    EMAIL_ALREADY_EXISTS(2003, "邮箱已存在"),
    USERNAME_ALREADY_EXISTS(2004, "用户名已存在"),
    
    // 文件相关 3xxx
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),
    FILE_SIZE_EXCEED(3002, "文件大小超出限制"),
    FILE_TYPE_NOT_SUPPORTED(3003, "文件类型不支持");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}