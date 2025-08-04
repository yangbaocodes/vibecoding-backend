-- 报表日志表
CREATE TABLE IF NOT EXISTS `sys_report_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '用户邮箱',
  `interface_name` varchar(100) NOT NULL COMMENT '接口名称',
  `interface_path` varchar(200) NOT NULL COMMENT '接口路径',
  `request_method` varchar(10) NOT NULL COMMENT '请求方法(GET/POST等)',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态(1:成功 0:失败)',
  `response_time` bigint(20) DEFAULT NULL COMMENT '响应时间(毫秒)',
  `error_message` text COMMENT '错误信息',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_interface_name` (`interface_name`),
  KEY `idx_created_time` (`created_time`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表日志表';

-- 报表统计表（可选，用于缓存统计结果）
CREATE TABLE IF NOT EXISTS `sys_report_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '用户邮箱',
  `interface_name` varchar(100) NOT NULL COMMENT '接口名称',
  `statistics_type` varchar(20) NOT NULL COMMENT '统计类型(year/month/day)',
  `statistics_value` varchar(20) NOT NULL COMMENT '统计值(2024/2024-01/2024-01-01)',
  `call_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '调用次数',
  `success_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '成功次数',
  `fail_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '失败次数',
  `avg_response_time` bigint(20) DEFAULT NULL COMMENT '平均响应时间(毫秒)',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_interface_type_value` (`user_id`, `interface_name`, `statistics_type`, `statistics_value`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_interface_name` (`interface_name`),
  KEY `idx_statistics_type` (`statistics_type`),
  KEY `idx_statistics_value` (`statistics_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表统计表'; 