package com.vibecoding.vibecoding_backend.common;

/**
 * 全局常量类
 *
 * @author VibeCode Team
 */
public class Constants {
    
    /**
     * JWT相关常量
     */
    public static class JWT {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String TOKEN_TYPE = "JWT";
        public static final String SECRET_KEY = "vibecoding-jwt-secret-key-2024";
        public static final long EXPIRATION_TIME = 86400000L; // 24小时
    }
    
    /**
     * Redis相关常量
     */
    public static class Redis {
        public static final String USER_TOKEN_PREFIX = "user:token:";
        public static final String USER_INFO_PREFIX = "user:info:";
        public static final String SYSTEM_CONFIG_PREFIX = "system:config:";
        public static final int DEFAULT_EXPIRE_TIME = 3600; // 1小时
    }
    
    /**
     * 用户相关常量
     */
    public static class User {
        public static final String DEFAULT_AVATAR = "/static/images/default-avatar.png";
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String USER_ROLE = "USER";
    }
    
    /**
     * 系统相关常量
     */
    public static class System {
        public static final String SYSTEM_USER = "system";
        public static final String DEFAULT_PASSWORD = "123456";
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
    }
    
    /**
     * 文件相关常量
     */
    public static class File {
        public static final String UPLOAD_PATH = "/uploads/";
        public static final long MAX_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
        public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "webp"};
        public static final String[] ALLOWED_DOCUMENT_TYPES = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx"};
    }
}