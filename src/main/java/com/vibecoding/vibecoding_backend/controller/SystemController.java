package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    /**
     * 创建文件信息表
     */
    @PostMapping("/create-file-table")
    public Result<String> createFileTable() {
        try {
            String sql = """
                CREATE TABLE IF NOT EXISTS `sys_file_info` (
                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                  `filename` varchar(255) NOT NULL COMMENT '文件名（UUID）',
                  `file_real_name` varchar(255) NOT NULL COMMENT '真实文件名',
                  `path` varchar(500) NOT NULL COMMENT '文件路径',
                  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                  `created_by` varchar(100) NOT NULL COMMENT '创建人',
                  `target_path` varchar(500) DEFAULT NULL COMMENT '目标路径',
                  `is_translated` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已转换（0-否，1-是）',
                  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
                  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
                  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `uk_filename` (`filename`),
                  KEY `idx_created_by` (`created_by`),
                  KEY `idx_created_time` (`created_time`),
                  KEY `idx_file_type` (`file_type`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表'
                """;
            
            jdbcTemplate.execute(sql);
            log.info("文件信息表创建成功");
            return Result.success("文件信息表创建成功");
        } catch (Exception e) {
            log.error("创建文件信息表失败", e);
            return Result.error("创建文件信息表失败: " + e.getMessage());
        }
    }
}