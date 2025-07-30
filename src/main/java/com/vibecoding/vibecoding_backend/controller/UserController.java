package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        log.info("获取用户信息");
        
        // TODO: 从JWT token中获取用户信息
        // 模拟用户信息
        UserInfoResponse user = new UserInfoResponse()
                .setId(1L)
                .setUsername("admin")
                .setEmail("admin@example.com")
                .setNickname("管理员")
                .setRole("ADMIN")
                .setStatus(1)
                .setLastLoginTime(LocalDateTime.now())
                .setCreateTime(LocalDateTime.now());
        
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<UserInfoResponse> updateUserInfo(@RequestBody UserInfoResponse request) {
        log.info("更新用户信息: {}", request.getUsername());
        
        // TODO: 实现更新用户信息逻辑
        
        return Result.success("更新成功", request);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> request) {
        log.info("修改密码");
        
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        
        // TODO: 实现修改密码逻辑
        
        return Result.<Void>success("密码修改成功", null);
    }
}