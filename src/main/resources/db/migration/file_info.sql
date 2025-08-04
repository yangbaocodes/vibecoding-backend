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