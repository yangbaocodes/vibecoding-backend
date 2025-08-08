package com.vibecoding.vibecoding_backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户配置验证工具类
 *
 * @author VibeCode Team
 */
@Slf4j
@Component
public class UserConfigValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 允许的配置字段集合
     */
    private static final Set<String> ALLOWED_CONFIG_FIELDS = new HashSet<>(Arrays.asList(
            "default_doc_type",      // 默认文档类型
            "theme",                 // 主题设置
            "language",              // 语言设置
            "auto_save",             // 自动保存
            "file_preview",          // 文件预览设置
            "notification_enabled"   // 通知设置
    ));
    
    /**
     * 验证用户配置JSON格式和字段
     *
     * @param configJson 配置JSON字符串
     * @return 验证结果
     */
    public ValidationResult validateConfig(String configJson) {
        try {
            // 验证JSON格式
            JsonNode jsonNode = objectMapper.readTree(configJson);
            
            // 检查所有字段是否在允许列表中
            Set<String> invalidFields = new HashSet<>();
            jsonNode.fieldNames().forEachRemaining(fieldName -> {
                if (!ALLOWED_CONFIG_FIELDS.contains(fieldName)) {
                    invalidFields.add(fieldName);
                }
            });
            
            if (!invalidFields.isEmpty()) {
                return ValidationResult.error("不允许的配置字段: " + String.join(", ", invalidFields) + 
                        "。允许的字段: " + String.join(", ", ALLOWED_CONFIG_FIELDS));
            }
            
            // 验证字段值类型
            ValidationResult valueValidation = validateFieldValues(jsonNode);
            if (!valueValidation.isSuccess()) {
                return valueValidation;
            }
            
            return ValidationResult.success();
            
        } catch (Exception e) {
            log.error("配置验证失败: {}", configJson, e);
            return ValidationResult.error("配置格式错误: " + e.getMessage());
        }
    }
    
    /**
     * 验证字段值的类型和格式
     *
     * @param jsonNode JSON节点
     * @return 验证结果
     */
    private ValidationResult validateFieldValues(JsonNode jsonNode) {
        // 验证 default_doc_type 字段
        if (jsonNode.has("default_doc_type")) {
            JsonNode docTypeNode = jsonNode.get("default_doc_type");
            if (!docTypeNode.isTextual()) {
                return ValidationResult.error("default_doc_type 字段必须是字符串类型");
            }
            String docType = docTypeNode.asText();
            if (!Arrays.asList("docx", "pdf", "txt").contains(docType)) {
                return ValidationResult.error("default_doc_type 字段值必须是 docx、pdf 或 txt");
            }
        }
        
        // 验证 theme 字段
        if (jsonNode.has("theme")) {
            JsonNode themeNode = jsonNode.get("theme");
            if (!themeNode.isTextual()) {
                return ValidationResult.error("theme 字段必须是字符串类型");
            }
            String theme = themeNode.asText();
            if (!Arrays.asList("light", "dark", "auto").contains(theme)) {
                return ValidationResult.error("theme 字段值必须是 light、dark 或 auto");
            }
        }
        
        // 验证 language 字段
        if (jsonNode.has("language")) {
            JsonNode languageNode = jsonNode.get("language");
            if (!languageNode.isTextual()) {
                return ValidationResult.error("language 字段必须是字符串类型");
            }
            String language = languageNode.asText();
            if (!Arrays.asList("zh", "en").contains(language)) {
                return ValidationResult.error("language 字段值必须是 zh 或 en");
            }
        }
        
        // 验证 auto_save 字段
        if (jsonNode.has("auto_save")) {
            JsonNode autoSaveNode = jsonNode.get("auto_save");
            if (!autoSaveNode.isBoolean()) {
                return ValidationResult.error("auto_save 字段必须是布尔类型");
            }
        }
        
        // 验证 file_preview 字段
        if (jsonNode.has("file_preview")) {
            JsonNode filePreviewNode = jsonNode.get("file_preview");
            if (!filePreviewNode.isBoolean()) {
                return ValidationResult.error("file_preview 字段必须是布尔类型");
            }
        }
        
        // 验证 notification_enabled 字段
        if (jsonNode.has("notification_enabled")) {
            JsonNode notificationNode = jsonNode.get("notification_enabled");
            if (!notificationNode.isBoolean()) {
                return ValidationResult.error("notification_enabled 字段必须是布尔类型");
            }
        }
        
        return ValidationResult.success();
    }
    
    /**
     * 获取允许的配置字段列表
     *
     * @return 允许的字段列表
     */
    public Set<String> getAllowedConfigFields() {
        return new HashSet<>(ALLOWED_CONFIG_FIELDS);
    }
    
    /**
     * 合并配置（支持部分更新）
     *
     * @param currentConfig 当前配置（JSON字符串）
     * @param newConfig 新配置（JSON字符串）
     * @return 合并后的配置（JSON字符串）
     */
    public String mergeConfig(String currentConfig, String newConfig) {
        try {
            // 解析当前配置
            JsonNode currentNode = objectMapper.readTree(currentConfig);
            
            // 解析新配置
            JsonNode newNode = objectMapper.readTree(newConfig);
            
            // 创建合并后的对象
            var mergedNode = objectMapper.createObjectNode();
            
            // 先添加当前配置的所有字段
            currentNode.fieldNames().forEachRemaining(fieldName -> {
                mergedNode.set(fieldName, currentNode.get(fieldName));
            });
            
            // 然后用新配置覆盖或添加字段
            newNode.fieldNames().forEachRemaining(fieldName -> {
                mergedNode.set(fieldName, newNode.get(fieldName));
            });
            
            return objectMapper.writeValueAsString(mergedNode);
            
        } catch (Exception e) {
            log.error("合并配置失败: currentConfig={}, newConfig={}", currentConfig, newConfig, e);
            throw new RuntimeException("合并配置失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证结果类
     */
    public static class ValidationResult {
        private final boolean success;
        private final String message;
        
        private ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
} 