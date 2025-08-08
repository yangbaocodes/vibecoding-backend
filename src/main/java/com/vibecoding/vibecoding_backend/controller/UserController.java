package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.UserConfigRequest;
import com.vibecoding.vibecoding_backend.dto.UserConfigResponse;
import com.vibecoding.vibecoding_backend.dto.UserInfoResponse;
import com.vibecoding.vibecoding_backend.entity.User;
import com.vibecoding.vibecoding_backend.service.UserService;
import com.vibecoding.vibecoding_backend.util.UserConfigValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Set;

/**
 * 用户控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserConfigValidator userConfigValidator;

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        log.info("获取用户信息");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("用户未认证");
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        UserInfoResponse userInfo = new UserInfoResponse()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setNickname(user.getNickname())
                .setRole(user.getRole())
                .setStatus(user.getStatus())
                .setLastLoginTime(user.getLastLoginTime())
                .setCreateTime(user.getCreateTime());
        
        return Result.success(userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<UserInfoResponse> updateUserInfo(@Valid @RequestBody UserInfoResponse request) {
        log.info("更新用户信息: {}", request.getUsername());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("用户未认证");
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 更新用户信息
        user.setNickname(request.getNickname());
        userService.updateUser(user);
        
        UserInfoResponse userInfo = new UserInfoResponse()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setNickname(user.getNickname())
                .setRole(user.getRole())
                .setStatus(user.getStatus())
                .setLastLoginTime(user.getLastLoginTime())
                .setCreateTime(user.getCreateTime());
        
        return Result.success("更新成功", userInfo);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody java.util.Map<String, String> request) {
        log.info("修改密码");
        
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return Result.error("密码参数不能为空");
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("用户未认证");
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 验证旧密码并更新新密码
        boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
        
        if (success) {
            return Result.<Void>success("密码修改成功", null);
        } else {
            return Result.error("旧密码错误或修改失败");
        }
    }

    /**
     * 更新用户配置
     */
    @PostMapping("/updateConfig")
    public Result<UserConfigResponse> updateUserConfig(@Valid @RequestBody UserConfigRequest request) {
        log.info("更新用户配置: {}", request.getConfig());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("用户未认证");
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        try {
            String updatedConfig = userService.updateUserConfig(user.getId(), request.getConfig());
            
            UserConfigResponse response = new UserConfigResponse();
            response.setConfig(updatedConfig);
            
            return Result.success("配置更新成功", response);
        } catch (Exception e) {
            log.error("更新用户配置失败", e);
            return Result.error("更新配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户配置
     */
    @GetMapping("/getConfig")
    public Result<UserConfigResponse> getUserConfig() {
        log.info("获取用户配置");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.error("用户未认证");
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        try {
            String config = userService.getUserConfig(user.getId());
            
            UserConfigResponse response = new UserConfigResponse();
            response.setConfig(config);
            
            return Result.success(response);
        } catch (Exception e) {
            log.error("获取用户配置失败", e);
            return Result.error("获取配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取允许的配置字段列表
     */
    @GetMapping("/config/fields")
    public Result<Set<String>> getAllowedConfigFields() {
        log.info("获取允许的配置字段列表");
        
        try {
            Set<String> allowedFields = userConfigValidator.getAllowedConfigFields();
            return Result.success("获取允许的配置字段成功", allowedFields);
        } catch (Exception e) {
            log.error("获取允许的配置字段失败", e);
            return Result.error("获取配置字段失败: " + e.getMessage());
        }
    }
}