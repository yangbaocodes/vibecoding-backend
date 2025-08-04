-- 修复数据库表结构问题
-- 执行此脚本前请备份数据库

USE vibecoding_dev;

-- 1. 修复 sys_report_log 表的 user_id 字段
ALTER TABLE `sys_report_log` MODIFY COLUMN `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID';

-- 2. 修复 sys_report_statistics 表的 user_id 字段
ALTER TABLE `sys_report_statistics` MODIFY COLUMN `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID';

-- 3. 验证修复结果
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_TYPE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'vibecoding_dev' 
AND TABLE_NAME IN ('sys_report_log', 'sys_report_statistics')
AND COLUMN_NAME = 'user_id'
ORDER BY TABLE_NAME; 