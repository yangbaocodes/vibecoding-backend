package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        data.put("version", "1.0.0");
        data.put("environment", "development");
        
        return Result.success(data);
    }

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getSystemConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("appName", "VibeCode Backend");
        config.put("version", "1.0.0");
        config.put("maxFileSize", "10MB");
        config.put("allowedFileTypes", new String[]{"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"});
        
        return Result.success(config);
    }
}