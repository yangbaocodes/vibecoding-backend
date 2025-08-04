package com.vibecoding.vibecoding_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Dify配置类
 *
 * @author VibeCode Team
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.dify")
public class DifyConfig {

    /**
     * Dify服务地址
     */
    private String baseUrl = "https://dify.aistudio.ltd/v1";

    /**
     * Bearer Token
     */
    private String bearerToken = "app-blj7fVtI82UlI0HWyberMKe4";

    /**
     * 工作流路径
     */
    private String workflowPath = "/workflows/run";

    /**
     * 接口名称
     */
    private String interfaceName = "getresumeinfo";

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 10000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 30000;
} 