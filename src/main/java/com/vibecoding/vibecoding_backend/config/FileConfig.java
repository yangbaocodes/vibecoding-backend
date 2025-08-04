package com.vibecoding.vibecoding_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件配置类
 * 统一管理文件下载URL等配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.file")
public class FileConfig {
    
    /**
     * 文件上传路径
     */
    private String uploadPath = "/uploads/";
    
    /**
     * 最大文件大小
     */
    private Long maxSize = 10485760L; // 10MB
    
    /**
     * 文件下载基础URL
     * 根据不同环境配置不同的域名
     */
    private String downloadBaseUrl;
    
    /**
     * 文件存储目录
     */
    private String storagePath = "filesource";
    
    /**
     * 文件输出目录
     */
    private String outputPath = "filetarget";
    
    /**
     * 构建完整的文件下载URL
     * 
     * @param path 文件路径
     * @param filename 文件名
     * @return 完整的下载URL
     */
    public String buildDownloadUrl(String path, String filename) {
        if (downloadBaseUrl == null || downloadBaseUrl.trim().isEmpty()) {
            // 如果没有配置基础URL，使用相对路径
            return "/api/file/files/" + path + "/" + filename;
        }
        
        // 确保基础URL不以斜杠结尾
        String baseUrl = downloadBaseUrl.endsWith("/") 
            ? downloadBaseUrl.substring(0, downloadBaseUrl.length() - 1) 
            : downloadBaseUrl;
            
        return baseUrl + "/api/file/files/" + path + "/" + filename;
    }
    
    /**
     * 构建文件上传URL
     * 
     * @param path 文件路径
     * @param filename 文件名
     * @return 完整的文件URL
     */
    public String buildFileUrl(String path, String filename) {
        return buildDownloadUrl(path, filename);
    }
} 