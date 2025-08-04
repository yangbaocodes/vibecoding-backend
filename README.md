# VibeCode Backend

基于 Spring Boot 3.4 + JDK 21 的现代化后端 API 服务

## 🚀 技术栈

### 核心框架
- **JDK**: OpenJDK 21
- **Spring Boot**: 3.4.0
- **Spring Security**: 6.x (JWT 无状态认证)
- **Spring Data Redis**: 缓存管理

### 数据持久化
- **MyBatis-Plus**: 3.5.5 (分页插件、逻辑删除、自动填充)
- **HikariCP**: 高性能连接池
- **MySQL**: 8.0+ (主数据库)

### 其他依赖
- **Redis**: 缓存和会话管理
- **JWT**: 无状态认证
- **Lombok**: 简化代码
- **Jackson**: JSON 序列化
- **Validation**: 参数校验

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
│   │   ├── MyBatisPlusConfig.java           # MyBatis-Plus配置
│   │   ├── RedisConfig.java                 # Redis配置
│   │   └── DifyConfig.java                  # Dify配置
│   ├── controller/                          # 控制器层
│   │   ├── AuthController.java              # 认证控制器
│   │   ├── SystemController.java            # 系统控制器
│   │   ├── UserController.java              # 用户控制器
│   │   ├── FileController.java              # 文件控制器
│   │   └── DifyController.java              # Dify控制器
│   ├── dto/                                 # 数据传输对象
│   │   ├── LoginRequest.java                # 登录请求DTO
│   │   ├── RegisterRequest.java             # 注册请求DTO
│   │   └── UserInfoResponse.java            # 用户信息响应DTO
│   ├── entity/                              # 实体类
│   │   ├── User.java                        # 用户实体
│   │   └── FileInfo.java                    # 文件信息实体
│   ├── exception/                           # 异常处理
│   │   ├── BusinessException.java           # 业务异常
│   │   └── GlobalExceptionHandler.java      # 全局异常处理器
│   ├── mapper/                              # MyBatis映射器
│   │   ├── UserMapper.java                   # 用户映射器
│   │   └── FileInfoMapper.java               # 文件信息映射器
│   ├── security/                            # 安全相关
│   │   ├── JwtAccessDeniedHandler.java      # JWT访问拒绝处理器
│   │   ├── JwtAuthenticationEntryPoint.java # JWT认证入口点
│   │   └── SecurityConfig.java              # Spring Security配置
│   ├── service/                             # 服务层
│   │   ├── UserService.java                  # 用户服务
│   │   ├── EmailService.java                 # 邮件服务
│   │   ├── FileService.java                  # 文件服务
│   │   └── DifyService.java                  # Dify服务
│   └── util/                                # 工具类
│       └── JwtUtils.java                    # JWT工具类
├── src/main/resources/
│   ├── application.yml                      # 应用配置
│   ├── db/migration/                        # 数据库脚本
│   │   └── init.sql                         # 初始化脚本
│   ├── static/                              # 静态资源
│   └── templates/                           # 模板文件
└── src/test/                                # 测试代码
```

## 🛠️ 环境要求

- **JDK**: 21+
- **Maven**: 3.8+
- **MySQL**: 8.0+
- **Redis**: 6.0+

## 🚦 快速开始

### 1. 环境准备

```bash
# 安装 JDK 21
# 推荐使用 Eclipse Temurin 或 Amazon Corretto

# 启动 MySQL 服务
mysql -u root -p

# 启动 Redis 服务
redis-server
```

### 2. 数据库初始化

```bash
# 创建数据库并执行初始化脚本
mysql -u root -p < src/main/resources/db/migration/init.sql
```

### 3. 配置文件

复制并修改配置文件：

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vibecoding_dev
    username: root
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

### 4. 运行项目

```bash
# 使用 Maven 运行
./mvnw spring-boot:run

# 或者打包后运行
./mvnw clean package
java -jar target/vibecoding-backend-0.0.1-SNAPSHOT.jar
```

项目启动后访问：http://localhost:8080/api

### 5. 文件下载URL配置

系统支持根据不同环境配置不同的文件下载基础URL：

**开发环境配置** (`application.yml`):
```yaml
app:
  file:
    download-base-url: http://localhost:8080
    storage-path: filesource
    output-path: filetarget
```

**生产环境配置** (`application-prod.yml`):
```yaml
app:
  file:
    download-base-url: ${FILE_DOWNLOAD_BASE_URL:https://your-domain.com}
    storage-path: filesource
    output-path: filetarget
```

**环境变量配置**:
```bash
# 设置生产环境文件下载基础URL
export FILE_DOWNLOAD_BASE_URL=https://api.yourdomain.com
```

**配置说明**:
- `download-base-url`: 文件下载的基础URL，根据不同环境配置不同域名
- `storage-path`: 文件存储目录，默认为 `filesource`
- `output-path`: 文件输出目录，默认为 `filetarget`
- 如果未配置 `download-base-url`，系统将使用相对路径

### 6. 跨域配置

系统支持跨域请求配置：

**开发环境配置**:
```yaml
app:
  cors:
    allowed-origins: 
      - "*"  # 允许所有源
    allowed-methods:
      - GET
      - POST
      - PUT
      - DELETE
      - OPTIONS
    allowed-headers: "*"
    allow-credentials: true
```

**生产环境配置**:
```yaml
app:
  cors:
    allowed-origins: 
      - "*"  # 允许所有源
    allowed-methods:
      - GET
      - POST
      - PUT
      - DELETE
      - OPTIONS
    allowed-headers: "*"
    allow-credentials: true
```

**跨域配置说明**:
- `allowed-origins`: 允许的跨域源，`"*"` 表示允许所有源
- `allowed-methods`: 允许的HTTP方法
- `allowed-headers`: 允许的请求头，`"*"` 表示允许所有头
- `allow-credentials`: 是否允许发送Cookie和认证信息

## 📋 API 接口

### 认证相关
- `POST /api/auth/send-verification-code` - 发送邮箱验证码
- `POST /api/auth/login-or-register` - 统一的注册/登录接口
- `POST /api/auth/logout` - 用户登出

### 用户相关
- `GET /api/user/info` - 获取用户信息
- `PUT /api/user/info` - 更新用户信息
- `PUT /api/user/password` - 修改密码

### 邮箱验证码功能

#### 发送验证码
**接口**: `POST /api/auth/send-verification-code`

**请求参数**:
```json
{
  "email": "user@example.com"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": null,
  "timestamp": 1640995200000
}
```

#### 注册/登录
**接口**: `POST /api/auth/login-or-register`

**请求参数**:
```json
{
  "email": "user@example.com",
  "verificationCode": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "isNewUser": false,
    "user": {
      "id": 1,
      "username": "user",
      "email": "user@example.com",
      "nickname": "测试用户",
      "role": "USER",
      "status": 1
    }
  },
  "timestamp": 1640995200000
}
```

**功能说明**:
- 验证码为6位纯数字
- 验证码有效期为5分钟
- 验证码存储在Redis中，键格式：`email:verification:{email}`
- 使用163邮箱服务器发送邮件
- 如果用户不存在则自动注册，如果存在则登录
- 登录状态使用JWT Bearer Token实现

### 系统相关
- `GET /api/system/health` - 健康检查
- `GET /api/system/config` - 获取系统配置

### 文件相关

#### 文件上传
- **接口**: `POST /api/file/upload`
- **参数**: `files` (MultipartFile数组，最多10个文件)
- **支持格式**: docx、pdf
- **返回**: JSON数组，包含filename和file_url
- **示例**:
```bash
curl -X POST http://localhost:8080/api/file/upload \
  -H "Authorization: Bearer <token>" \
  -F "files=@document1.docx" \
  -F "files=@document2.pdf"
```

#### 文件下载
- **接口**: `GET /api/file/files/{path}/{filename}`
- **参数**: path (路径), filename (文件名)
- **认证**: 无需token验证
- **返回**: 文件流，浏览器自动下载
- **示例**:
```bash
curl -O http://localhost:8080/api/file/files/filesource/uuid-filename.docx
```

### 简历生成相关

#### 生成简历Word文档
- **接口**: `POST /api/resume/generate`
- **参数**: JSON格式
  - `resumeUrl` (简历URL，必填)
  - `user` (用户标识，默认: "2938922@qq.com")
  - `responseMode` (响应模式，默认: "streaming")
- **返回**: 生成的文件下载URL
- **示例**:
```bash
curl -X POST http://localhost:8080/api/resume/generate \
  -H "Content-Type: application/json" \
  -d '{
    "resumeUrl": "https://aijsz-prod-ai-image.oss-cn-shanghai.aliyuncs.com/1ef5e775-405d-447d-b3ec-1742850355a3.docx",
    "user": "2938922@qq.com",
    "responseMode": "streaming"
  }'
```

**简化参数示例**:
```bash
curl -X POST http://localhost:8080/api/resume/generate \
  -H "Content-Type: application/json" \
  -d '{"resumeUrl": "https://aijsz-prod-ai-image.oss-cn-shanghai.aliyuncs.com/1ef5e775-405d-447d-b3ec-1742850355a3.docx"}'
```

**响应示例**:
```json
{
  "code": 200,
  "message": "简历生成成功",
  "data": "http://localhost:8080/api/file/files/filetarget/resume_80eae4a028d1468baf292a4a460ad5df.docx",
  "timestamp": 1754243158837
}
```

#### 下载生成的简历文件
- **接口**: `GET /api/resume/download/{filename}`
- **参数**: filename (文件名)
- **认证**: 无需token验证
- **返回**: Word文档文件流
- **示例**:
```bash
curl -O http://localhost:8080/api/resume/download/resume_80eae4a028d1468baf292a4a460ad5df.docx
```

#### 简历生成健康检查
- **接口**: `GET /api/resume/health`
- **认证**: 无需token验证
- **返回**: 服务状态

**功能特性**:
- 自动解析简历信息并生成标准Word文档
- 使用POI-TL模板引擎，支持复杂的文档格式
- 自动创建输出目录和生成唯一文件名
- 支持下载生成的简历文件
- 完整的错误处理和日志记录

### Dify相关

#### 简历解析
- **接口**: `POST /api/dify/parse-resume`
- **参数**: JSON格式
  - `resumeUrl` (简历URL，必填)
  - `user` (用户标识，默认: "2938922@qq.com")
  - `responseMode` (响应模式，默认: "streaming")
- **认证**: 需要JWT token认证
- **返回**: 简历详细信息
- **示例**:
```bash
curl -X POST http://localhost:8080/api/dify/parse-resume \
  -H "Content-Type: application/json" \
  -d '{
    "resumeUrl": "https://aijsz-prod-ai-image.oss-cn-shanghai.aliyuncs.com/1ef5e775-405d-447d-b3ec-1742850355a3.docx",
    "user": "2938922@qq.com",
    "responseMode": "streaming"
  }'
```

**响应示例**:
```json
{
  "code": 200,
  "message": "简历解析成功",
  "data": {
    "code": 0,
    "name": "ZhouWu",
    "englishName": "",
    "experienceSummary": [
      {
        "company": "Cognizant",
        "position": "Full-stack developer",
        "startDate": "2021-07-01",
        "endDate": "2024-07-01"
      }
    ],
    "education": [...],
    "workYears": 15,
    "technologies": ["Nodejs（18.x）（3年）", "Python（3年）", ...],
    "personalAdvantage": "Senior full stack development expert...",
    "technicalSkills": {
      "tools": ["VSCode", "visual studio", "Git", "Jira"],
      "languages": ["nodejs", "python", "PHP", "C#", ...],
      "platforms": ["PC", "Mobile"],
      "databases": ["SQLite", "MongoDB", "MySQL", "sqlserver"],
      "operatingSystems": ["Windows", "MacOS", "linux"],
      "frameworks": ["VUE", "React", "Angular", "Mini Program", "Taro"]
    },
    "projectExperience": [...]
  },
  "timestamp": 1754240988886
}
```

#### 异步简历解析
- **接口**: `POST /api/dify/parse-resume-async`
- **参数**: JSON格式
  - `resumeUrl` (简历URL，必填)
  - `user` (用户标识，默认: "2938922@qq.com")
  - `responseMode` (响应模式，默认: "streaming")
- **认证**: 需要JWT token认证
- **返回**: 任务启动状态
- **示例**:
```bash
curl -X POST http://localhost:8080/api/dify/parse-resume-async \
  -H "Content-Type: application/json" \
  -d '{
    "resumeUrl": "https://aijsz-prod-ai-image.oss-cn-shanghai.aliyuncs.com/1ef5e775-405d-447d-b3ec-1742850355a3.docx",
    "user": "2938922@qq.com",
    "responseMode": "streaming"
  }'
```

#### Dify健康检查
- **接口**: `GET /api/dify/health`
- **认证**: 需要JWT token认证
- **返回**: 服务状态

### 报表相关

#### 获取用户一年内每天调用接口的累计次数
- **接口**: `GET /api/report/yearly-daily-calls`
- **认证**: 需要JWT token认证
- **参数**: 
  - `year` (可选): 年份，如2025，默认当前年份
- **返回**: 用户指定年份内每天调用接口的累计次数（只返回有调用记录的日期）
- **示例**:
```bash
# 获取当前年份的统计数据
curl -X GET http://localhost:8080/api/report/yearly-daily-calls \
  -H "Authorization: Bearer <your-jwt-token>"

# 获取指定年份的统计数据
curl -X GET "http://localhost:8080/api/report/yearly-daily-calls?year=2025" \
  -H "Authorization: Bearer <your-jwt-token>"
```

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
      },
      {
        "callDate": "2025-07-15",
        "totalCallCount": 2,
        "successCount": 2,
        "failCount": 0,
        "avgResponseTime": 1900
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

#### Dify配置说明

Dify服务配置在 `