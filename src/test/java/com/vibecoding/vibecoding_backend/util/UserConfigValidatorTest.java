package com.vibecoding.vibecoding_backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户配置验证器测试类
 *
 * @author VibeCode Team
 */
@SpringBootTest
class UserConfigValidatorTest {

    private final UserConfigValidator validator = new UserConfigValidator();

    @Test
    void testValidConfig() {
        // 测试有效的配置
        String validConfig = "{\"default_doc_type\":\"docx\",\"theme\":\"dark\",\"auto_save\":true}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(validConfig);
        
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    @Test
    void testInvalidField() {
        // 测试包含无效字段的配置
        String invalidConfig = "{\"default_doc_type\":\"docx\",\"invalid_field\":\"value\"}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("不允许的配置字段"));
    }

    @Test
    void testInvalidDocType() {
        // 测试无效的文档类型
        String invalidConfig = "{\"default_doc_type\":\"invalid_type\"}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("default_doc_type 字段值必须是"));
    }

    @Test
    void testInvalidTheme() {
        // 测试无效的主题设置
        String invalidConfig = "{\"theme\":\"invalid_theme\"}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("theme 字段值必须是"));
    }

    @Test
    void testInvalidLanguage() {
        // 测试无效的语言设置
        String invalidConfig = "{\"language\":\"fr\"}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("language 字段值必须是"));
    }

    @Test
    void testInvalidBooleanField() {
        // 测试布尔字段类型错误
        String invalidConfig = "{\"auto_save\":\"true\"}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("auto_save 字段必须是布尔类型"));
    }

    @Test
    void testInvalidStringField() {
        // 测试字符串字段类型错误
        String invalidConfig = "{\"default_doc_type\":123}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("default_doc_type 字段必须是字符串类型"));
    }

    @Test
    void testInvalidJsonFormat() {
        // 测试无效的JSON格式
        String invalidConfig = "{\"default_doc_type\":\"docx\",}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(invalidConfig);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("配置格式错误"));
    }

    @Test
    void testEmptyConfig() {
        // 测试空配置
        String emptyConfig = "{}";
        UserConfigValidator.ValidationResult result = validator.validateConfig(emptyConfig);
        
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    @Test
    void testAllowedFields() {
        // 测试获取允许的字段列表
        var allowedFields = validator.getAllowedConfigFields();
        
        assertTrue(allowedFields.contains("default_doc_type"));
        assertTrue(allowedFields.contains("theme"));
        assertTrue(allowedFields.contains("language"));
        assertTrue(allowedFields.contains("auto_save"));
        assertTrue(allowedFields.contains("file_preview"));
        assertTrue(allowedFields.contains("notification_enabled"));
        assertEquals(6, allowedFields.size());
    }
    
    @Test
    void testMergeConfig() {
        // 测试配置合并功能
        String currentConfig = "{\"default_doc_type\":\"docx\",\"theme\":\"light\",\"auto_save\":true}";
        String newConfig = "{\"theme\":\"dark\",\"language\":\"zh\"}";
        
        String mergedConfig = validator.mergeConfig(currentConfig, newConfig);
        
        // 验证合并结果
        assertTrue(mergedConfig.contains("\"default_doc_type\":\"docx\""));
        assertTrue(mergedConfig.contains("\"theme\":\"dark\"")); // 新配置覆盖了旧配置
        assertTrue(mergedConfig.contains("\"auto_save\":true"));
        assertTrue(mergedConfig.contains("\"language\":\"zh\"")); // 新增字段
    }
    
    @Test
    void testMergeConfigWithEmptyCurrent() {
        // 测试空当前配置的合并
        String currentConfig = "{}";
        String newConfig = "{\"default_doc_type\":\"pdf\",\"theme\":\"dark\"}";
        
        String mergedConfig = validator.mergeConfig(currentConfig, newConfig);
        
        assertTrue(mergedConfig.contains("\"default_doc_type\":\"pdf\""));
        assertTrue(mergedConfig.contains("\"theme\":\"dark\""));
    }
    
    @Test
    void testMergeConfigWithEmptyNew() {
        // 测试空新配置的合并
        String currentConfig = "{\"default_doc_type\":\"docx\",\"theme\":\"light\"}";
        String newConfig = "{}";
        
        String mergedConfig = validator.mergeConfig(currentConfig, newConfig);
        
        assertTrue(mergedConfig.contains("\"default_doc_type\":\"docx\""));
        assertTrue(mergedConfig.contains("\"theme\":\"light\""));
    }
} 