-- VibeCode Backend 数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `vibecoding_dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `vibecoding_dev`;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `password` VARCHAR(100) DEFAULT NULL COMMENT '密码（已废弃，使用验证码登录）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `role` VARCHAR(50) DEFAULT 'USER' COMMENT '角色',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入默认管理员用户
INSERT INTO `sys_user` (`username`, `email`, `password`, `nickname`, `role`, `status`) 
VALUES ('admin', 'admin@vibecoding.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTneMeGZv0pByCM6hgWERvQXXIm', '系统管理员', 'ADMIN', 1);

-- 插入测试用户
INSERT INTO `sys_user` (`username`, `email`, `password`, `nickname`, `role`, `status`) 
VALUES ('user', 'user@vibecoding.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTneMeGZv0pByCM6hgWERvQXXIm', '测试用户', 'USER', 1);

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_desc` VARCHAR(255) DEFAULT NULL COMMENT '配置描述',
    `config_type` VARCHAR(20) DEFAULT 'STRING' COMMENT '配置类型',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入默认系统配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_desc`, `config_type`) VALUES
('system.name', 'VibeCode Backend', '系统名称', 'STRING'),
('system.version', '1.0.0', '系统版本', 'STRING'),
('file.upload.max_size', '10485760', '文件上传最大大小（字节）', 'NUMBER'),
('file.upload.allowed_types', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,ppt,pptx', '允许上传的文件类型', 'STRING');

-- 文件表
CREATE TABLE IF NOT EXISTS `sys_file` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_size` BIGINT(20) NOT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型',
    `mime_type` VARCHAR(100) DEFAULT NULL COMMENT 'MIME类型',
    `upload_user_id` BIGINT(20) DEFAULT NULL COMMENT '上传用户ID',
    `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_upload_user_id` (`upload_user_id`),
    KEY `idx_file_type` (`file_type`),
    KEY `idx_upload_time` (`upload_time`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `sys_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作',
    `method` VARCHAR(100) DEFAULT NULL COMMENT '请求方法',
    `params` TEXT COMMENT '请求参数',
    `result` TEXT COMMENT '操作结果',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `execute_time` BIGINT(20) DEFAULT NULL COMMENT '执行时间（毫秒）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation` (`operation`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 文件信息表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';