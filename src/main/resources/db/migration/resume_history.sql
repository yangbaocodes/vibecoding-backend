-- 简历转换历史表
CREATE TABLE IF NOT EXISTS `resume_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `source_name` varchar(255) NOT NULL COMMENT '转换前的文件名',
  `source_type` varchar(50) NOT NULL COMMENT '转换前的文件类型',
  `source_path` varchar(500) NOT NULL COMMENT '转换前的存储位置',
  `target_name` varchar(255) DEFAULT NULL COMMENT '转换后的文件名',
  `target_type` varchar(50) DEFAULT NULL COMMENT '转换后的文件类型',
  `target_language` varchar(10) DEFAULT NULL COMMENT '转换后的文件语言',
  `translate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转换时间',
  `translate_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '转换状态（0-失败，1-成功）',
  `created_by` varchar(100) NOT NULL COMMENT '操作人',
  `error_message` text DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记（0-未删除，1-已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_translate_time` (`translate_time`),
  KEY `idx_translate_status` (`translate_status`),
  KEY `idx_source_name` (`source_name`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='简历转换历史表';
