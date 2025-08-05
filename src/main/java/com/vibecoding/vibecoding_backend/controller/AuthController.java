package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.EmailVerificationRequest;
import com.vibecoding.vibecoding_backend.dto.LoginRequest;
import com.vibecoding.vibecoding_backend.dto.UserInfoResponse;
import com.vibecoding.vibecoding_backend.entity.User;
import com.vibecoding.vibecoding_backend.security.JwtUtils;
import com.vibecoding.vibecoding_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/send-verification-code")
    public Result<Void> sendVerificationCode(@Valid @RequestBody EmailVerificationRequest request) {
        log.info("发送验证码请求: {}", request.getEmail());
        
        boolean success = userService.sendVerificationCode(request.getEmail());
        
        if (success) {
            return Result.<Void>success("验证码发送成功", null);
        } else {
            return Result.<Void>error("验证码发送失败，请稍后重试");
        }
    }

    /**
     * 统一的注册/登录接口
     * 如果用户不存在则注册，如果存在则登录
     */
    @PostMapping("/login-or-register")
    public Result<Map<String, Object>> loginOrRegister(@Valid @RequestBody LoginRequest request) {
        log.info("用户登录/注册请求: {}", request.getEmail());
        
        // 验证邮箱验证码
        if (!userService.verifyEmailCode(request.getEmail(), request.getVerificationCode())) {
            return Result.error("验证码错误或已过期");
        }
        
        // 查找用户是否存在
        User user = userService.findByEmail(request.getEmail());
        boolean isNewUser = false;
        
        if (user == null) {
            // 用户不存在，创建新用户
            user = userService.createUser(request.getEmail(), null);
            isNewUser = true;
            log.info("创建新用户: {}", user.getEmail());
        }
        
        // 更新最后登录时间
        userService.updateLastLoginTime(user.getId());
        
        // 生成JWT token
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getUsername());
        
        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("isNewUser", isNewUser);
        
        UserInfoResponse userInfo = new UserInfoResponse()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setNickname(user.getNickname())
                .setRole(user.getRole())
                .setStatus(user.getStatus());
        data.put("user", userInfo);
        
        String message = isNewUser ? "注册成功" : "登录成功";
        return Result.success(message, data);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        log.info("用户登出");
        
        // JWT是无状态的，客户端删除token即可完成登出
        // 如果需要服务端控制，可以考虑将token加入黑名单
        
        return Result.<Void>success("登出成功", null);
    }
}