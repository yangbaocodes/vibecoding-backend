-- 用户配置字段迁移脚本

USE `vibecoding_dev`;

-- 为用户表添加config字段
ALTER TABLE `sys_user` 
ADD COLUMN `config` JSON DEFAULT NULL COMMENT '用户配置信息' AFTER `deleted`;

-- 为现有用户设置默认配置
UPDATE `sys_user` 
SET `config` = '{"default_doc_type":"docx"}' 
WHERE `config` IS NULL; 