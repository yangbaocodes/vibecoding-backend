package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@RequiredArgsConstructor
public class SystemController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("environment", "development");
        data.put("version", "1.0.0");
        data.put("timestamp", System.currentTimeMillis());
        return Result.success("系统运行正常", data);
    }

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("appName", "VibeCode Backend");
        config.put("version", "1.0.0");
        config.put("environment", "development");
        config.put("port", 8080);
        return Result.success("获取系统配置成功", config);
    }
}