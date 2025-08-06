# VibeCode Backend

基于 Spring Boot 3.4 + JDK 21 的现代化后端 API 服务

## 🚀 技术栈

### 核心框架
- **JDK**: OpenJDK 21
- **Spring Boot**: 3.4.0
- **Spring Security**: 6.4.1 (JWT 无状态认证)
- **Spring Data Redis**: 缓存管理

### 数据持久化
- **MyBatis-Plus**: 3.5.7 (分页插件、逻辑删除、自动填充)
- **HikariCP**: 高性能连接池
- **MySQL**: 8.0+ (主数据库)

### 其他依赖
- **Redis**: 缓存和会话管理
- **JWT**: 0.12.3 (无状态认证)
- **Lombok**: 简化代码
- **Jackson**: JSON 序列化
- **Validation**: 参数校验
- **POI-TL**: 1.12.1 (Word文档模板处理)

## 📁 项目结构

```
backend/
├── src/main/java/com/vibecoding/vibecoding_backend/
│   ├── VibeCodingBackendApplication.java    # 主启动类
│   ├── common/                              # 通用类
│   │   ├── Constants.java                   # 全局常量
│   │   ├── Result.java                      # 统一响应结果
│   │   └── ResultCode.java                  # 响应状态码枚举
│   ├── config/                              # 配置类
│   │   ├── CorsConfig.java                  # 跨域配置
│   │   ├── FileConfig.java                  # 文件配置
│   │   ├── MyBatisPlusConfig.java           # MyBatis-Plus配置
│   │   ├── RedisConfig.java                 # Redis配置
│   │   ├── DifyConfig.java                  # Dify服务配置
│   │   └── PasswordConfig.java              # 密码编码器配置
│   ├── controller/                          # 控制器层
│   │   ├── AuthController.java              # 认证控制器
│   │   ├── SystemController.java            # 系统控制器
│   │   ├── UserController.java              # 用户控制器
│   │   ├── FileController.java              # 文件控制器
│   │   ├── ResumeController.java            # 简历控制器
│   │   └── ReportController.java            # 报表控制器
│   ├── dto/                                 # 数据传输对象
│   │   ├── LoginRequest.java                # 登录请求DTO
│   │   ├── EmailVerificationRequest.java    # 邮箱验证请求DTO
│   │   ├── ResumeParseRequest.java          # 简历解析请求DTO
│   │   ├── ResumeInfoResponse.java          # 简历信息响应DTO
│   │   ├── UserInfoResponse.java            # 用户信息响应DTO
│   │   ├── BatchDownloadRequest.java        # 批量下载请求DTO
│   │   ├── BatchDownloadResponse.java       # 批量下载响应DTO
│   │   └── DifyRequest.java                 # Dify服务请求DTO
│   ├── entity/                              # 实体类
│   │   ├── User.java                        # 用户实体
│   │   ├── FileInfo.java                    # 文件信息实体
│   │   └── ReportLog.java                   # 报表日志实体
│   ├── exception/                           # 异常处理
│   │   ├── BusinessException.java           # 业务异常
│   │   └── GlobalExceptionHandler.java      # 全局异常处理器
│   ├── mapper/                              # MyBatis映射器
│   │   ├── UserMapper.java                   # 用户映射器
│   │   ├── FileInfoMapper.java               # 文件信息映射器
│   │   └── ReportLogMapper.java              # 报表日志映射器
│   ├── security/                            # 安全相关
│   │   ├── JwtAccessDeniedHandler.java      # JWT访问拒绝处理器
│   │   ├── JwtAuthenticationEntryPoint.java # JWT认证入口点
│   │   ├── JwtAuthenticationFilter.java     # JWT认证过滤器
│   │   ├── JwtUtils.java                    # JWT工具类
│   │   ├── CustomUserDetailsService.java    # 自定义用户详情服务
│   │   └── SecurityConfig.java              # Spring Security配置
│   ├── service/                             # 服务层
│   │   ├── UserService.java                  # 用户服务
│   │   ├── EmailService.java                 # 邮件服务
│   │   ├── FileService.java                  # 文件服务
│   │   ├── DifyService.java                  # Dify服务
│   │   └── ReportLogService.java            # 报表日志服务
│   ├── aspect/                              # 切面
│   │   └── ReportLogAspect.java             # 报表日志切面
│   ├── annotation/                          # 注解
│   │   └── ReportLog.java                   # 报表日志注解
│   └── util/                                # 工具类
│       └── ResumeGenerator.java              # 简历生成器
├── src/main/resources/
│   ├── application.yml                      # 应用配置
│   ├── db/migration/                        # 数据库脚本
│   │   ├── init.sql                         # 初始化脚本
│   │   ├── file_info.sql                    # 文件信息表
│   │   ├── report_log.sql                   # 报表日志表
│   │   └── fix_database.sql                 # 数据库修复脚本
│   ├── static/                              # 静态资源
│   └── template/                            # 模板文件
│       └── ResumeTemplate.docx              # 简历模板
└── src/test/                                # 测试代码
```

## 🔧 核心功能

### 用户认证系统
- **邮箱验证码登录**: 支持邮箱验证码登录和注册
- **JWT 认证**: 无状态认证，支持自动刷新
- **用户管理**: 用户信息管理、密码修改

### 文件处理系统
- **文件上传**: 支持多文件上传，自动生成UUID文件名
- **文件下载**: 支持单个文件和批量文件下载
- **文件管理**: 文件信息数据库存储和管理

### 简历转换系统
- **AI 解析**: 集成 Dify 服务解析简历信息
- **模板生成**: 基于 Cognizant 模板生成标准化简历
- **文件命名**: 批量下载时统一使用 "Cognizant_" 前缀和 .docx 后缀
- **状态跟踪**: 实时跟踪转换状态和进度

### 报表统计系统
- **操作日志**: 记录用户操作和系统事件
- **数据统计**: 生成各类统计报表
- **调用统计**: 统计用户API调用次数和响应时间

## 📋 API 接口文档

### 认证相关

#### 发送邮箱验证码
- **接口**: `POST /api/auth/send-verification-code`
- **参数**: 
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **认证**: 无需token验证
- **返回**: 验证码发送结果

#### 邮箱验证码登录/注册
- **接口**: `POST /api/auth/login-or-register`
- **参数**: 
  ```json
  {
    "email": "user@example.com",
    "verificationCode": "123456"
  }
  ```
- **认证**: 无需token验证
- **返回**: JWT token 和用户信息

#### 用户登出
- **接口**: `POST /api/auth/logout`
- **认证**: 需要JWT token认证
- **返回**: 登出结果

### 用户相关

#### 获取用户信息
- **接口**: `GET /api/user/info`
- **认证**: 需要JWT token认证
- **返回**: 用户详细信息

### 文件相关

#### 文件上传
- **接口**: `POST /api/file/upload`
- **参数**: MultipartFile[] files
- **认证**: 需要JWT token认证
- **返回**: 上传文件信息列表

#### 批量文件下载
- **接口**: `POST /api/file/files/downloads`
- **参数**: 
  ```json
  {
    "filenames": ["file1.docx", "file2.pdf"]
  }
  ```
- **认证**: 需要JWT token认证
- **返回**: ZIP压缩包文件流

#### 单个文件下载
- **接口**: `GET /api/file/files/{path}/{filename}`
- **参数**: path (文件路径), filename (文件名)
- **认证**: 无需token验证
- **返回**: 文件流

### 简历相关

#### 简历生成
- **接口**: `POST /api/resume/generate`
- **参数**: 
  ```json
  {
    "fileName": "resume_80eae4a028d1468baf292a4a460ad5df.docx",
    "responseMode": "blocking",
    "targetLanguage": "zh",
    "targetFileType": "word"
  }
  ```
- **参数说明**:
  - `fileName`: 简历文件名（必填）
  - `responseMode`: 响应模式，默认为 "streaming"
  - `targetLanguage`: 目标语言，只能是 "en" 或 "zh"（必填）
  - `targetFileType`: 目标文件类型，只能是 "word" 或 "ppt"，默认为 "word"（可选）
- **认证**: 需要JWT token认证
- **返回**: 生成的文件下载URL

#### 下载生成的简历文件（*web前端不要只要该接口，请使用批量文件下载*）
- **接口**: `GET /api/resume/download/{filename}`
- **参数**: filename (文件名)
- **认证**: 无需token验证
- **返回**: Word文档文件流

### 系统相关

#### 系统健康检查
- **接口**: `GET /api/system/health`
- **认证**: 无需token验证
- **返回**: 系统状态信息

#### 获取系统配置
- **接口**: `GET /api/system/config`
- **认证**: 无需token验证
- **返回**: 系统配置信息

#### 创建文件信息表
- **接口**: `POST /api/system/create-file-table`
- **认证**: 无需token验证
- **返回**: 表创建结果

### 报表相关

#### 获取用户一年内每天调用接口的累计次数
- **接口**: `GET /api/report/yearly-daily-calls`
- **认证**: 需要JWT token认证
- **参数**: 
  - `year` (可选): 年份，如2025，默认当前年份
- **返回**: 用户指定年份内每天调用接口的累计次数（只返回有调用记录的日期）

**响应示例**:
```json
{
  "code": 200,
  "message": "获取2025年每天调用次数统计成功",
  "data": {
    "userEmail": "user@example.com",
    "userId": 1,
    "year": 2025,
    "totalDays": 10,
    "dailyCallCount": [
      {
        "callDate": "2025-08-04",
        "totalCallCount": 5,
        "successCount": 4,
        "failCount": 1,
        "avgResponseTime": 2300
      },
      {
        "callDate": "2025-08-03",
        "totalCallCount": 3,
        "successCount": 3,
        "failCount": 0,
        "avgResponseTime": 2100
      }
    ]
  },
  "timestamp": 1754243158837
}
```

**功能说明**:
- 支持指定年份查询，如不指定则默认当前年份
- 只返回指定年份内有调用记录的日期，不返回365天的空数据
- 如果指定年份只有10天调用了接口，那么返回的数据只有10天
- 按日期倒序排列（最新的日期在前）
- 包含每天的总调用次数、成功次数、失败次数和平均响应时间

## 🔧 配置说明

### 文件配置

文件相关配置在 `application.yml` 中：

```yaml
app:
  file:
    upload-path: /uploads/
    max-size: 10485760 # 10MB
    download-base-url: http://localhost:8080 # 开发环境默认URL
    storage-path: filesource
    output-path: filetarget
```

**功能特性**:
- 支持不同环境的文件下载URL配置
- 支持环境变量覆盖（生产环境）
- 自动构建完整的文件下载URL
- 文件上传大小限制：10MB
- 支持的文件类型：DOCX、PDF

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vibecoding
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_redis_password
    database: 0
```

### Dify 服务配置

```yaml
app:
  dify:
    base-url: https://dify.aistudio.ltd/v1
    bearer-token: your_dify_bearer_token
    workflow-path: /workflows/run
    interface-name: getresumeinfo
    connect-timeout: 15000   # 连接超时（毫秒）
    read-timeout: 120000     # 读取超时（毫秒，2分钟）
```

**超时配置说明**:
- `connect-timeout`: 连接建立的超时时间，默认15秒
- `read-timeout`: 数据读取的超时时间，默认120秒（2分钟）
- 系统会自动进行最多2次重试，每次重试间隔5秒
- WebClient超时时间会设置为读取超时的2倍，确保有足够的处理时间

## 🚀 部署指南

### 环境要求
- JDK 21+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 开发环境启动

1. **配置数据库**
   ```bash
   # 创建数据库
   CREATE DATABASE vibecoding_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 执行初始化脚本
   mysql -u username -p vibecoding_dev < src/main/resources/db/migration/init.sql
   mysql -u username -p vibecoding_dev < src/main/resources/db/migration/report_log.sql
   ```

2. **配置环境变量**
   ```bash
   # 设置数据库连接
   export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/vibecoding_dev
   export SPRING_DATASOURCE_USERNAME=your_username
   export SPRING_DATASOURCE_PASSWORD=your_password
   
   # 设置Redis连接
   export SPRING_REDIS_HOST=localhost
   export SPRING_REDIS_PORT=6379
   export SPRING_REDIS_PASSWORD=your_redis_password
   
   # 设置邮件服务
   export SPRING_MAIL_HOST=smtp.yeah.net
   export SPRING_MAIL_USERNAME=your_email@yeah.net
   export SPRING_MAIL_PASSWORD=your_email_password
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

### 生产环境部署

1. **打包应用**
   ```bash
   mvn clean package -Dmaven.test.skip=true
   ```

2. **运行应用**
   ```bash
   java -jar target/vibecoding-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

3. **使用Docker部署**
   ```bash
   # 构建镜像
   docker build -t vibecoding-backend .
   
   # 运行容器
   docker run -d -p 8080:8080 --name vibecoding-backend vibecoding-backend
   ```

4. **环境变量配置**
   ```bash
   # 生产环境数据库
   export DB_USERNAME=your_prod_username
   export DB_PASSWORD=your_prod_password
   
   # 生产环境Redis
   export REDIS_HOST=your_redis_host
   export REDIS_PORT=6379
   export REDIS_PASSWORD=your_redis_password
   
   # 文件下载URL
   export FILE_DOWNLOAD_BASE_URL=https://your-domain.com
   ```

## 📝 开发规范

### 代码风格
- 遵循 Spring Boot 最佳实践
- 使用 Lombok 简化代码
- 统一的异常处理和响应格式
- 完整的日志记录

### 架构设计
- **循环依赖处理**: 使用独立的配置类管理Bean，避免循环依赖
- **配置分离**: 将不同职责的配置分离到独立的配置类中
- **依赖注入**: 优先使用构造器注入，提高代码可测试性

### API 设计规范
- RESTful API 设计
- 统一的响应格式
- 完整的参数验证
- 详细的错误信息

### 数据库设计
- 使用 MyBatis-Plus 简化数据访问
- 统一的命名规范
- 完整的索引设计
- 数据完整性约束

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 贡献流程
1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 📞 联系方式

如有问题或建议，请联系项目维护者。

## 🔄 最新更新

### v0.0.1-SNAPSHOT (2025-08-06)
- ✅ 修复了Dify服务调用超时问题
- ✅ 优化了WebClient配置，使用配置文件中的超时设置
- ✅ 增加了重试机制，提高API调用的可靠性
- ✅ 改进了超时异常处理和错误日志记录
- ✅ 将读取超时时间从30秒增加到120秒（2分钟）
- ✅ 添加了详细的调试日志，便于问题诊断

### v0.0.1-SNAPSHOT (2025-08-05)
- ✅ 修复了Spring Security循环依赖问题
- ✅ 优化了文件批量下载功能，统一使用.docx后缀
- ✅ 新增了PasswordConfig配置类
- ✅ 完善了报表统计功能
- ✅ 优化了邮件服务配置
- ✅ 更新了Dify服务集成

## 📄 许可证

本项目采用 MIT 许可证。