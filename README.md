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
│   │   └── RedisConfig.java                 # Redis配置
│   ├── controller/                          # 控制器层
│   │   ├── AuthController.java              # 认证控制器
│   │   ├── SystemController.java            # 系统控制器
│   │   ├── UserController.java              # 用户控制器
│   │   └── FileController.java              # 文件控制器
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
│   │   └── FileService.java                  # 文件服务
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

项目启动后访问：http://localhost:8081/api

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

## 🔧 开发指南

### 数据库设计

主要数据表：
- `sys_user`: 用户表
- `sys_config`: 系统配置表  
- `sys_file_info`: 文件信息表
- `sys_log`: 操作日志表

### JWT 认证

默认配置：
- 密钥：`vibecoding-jwt-secret-key-2024`
- 过期时间：24小时
- 请求头：`Authorization: Bearer <token>`

### 缓存策略

Redis 键命名规范：
- 用户token：`user:token:{userId}`
- 用户信息：`user:info:{userId}`
- 系统配置：`system:config:{key}`
- 邮箱验证码：`email:verification:{email}` (5分钟过期)

### 异常处理

统一响应格式：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {...},
  "timestamp": 1640995200000
}
```

## 🧪 测试

```bash
# 运行所有测试
./mvnw test

# 运行特定测试
./mvnw test -Dtest=UserControllerTest
```

## 📦 部署

### Docker 部署

```bash
# 构建镜像
docker build -t vibecoding-backend .

# 运行容器
docker run -p 8080:8080 vibecoding-backend
```

### 传统部署

```bash
# 打包
./mvnw clean package -Pprod

# 运行
java -jar -Dspring.profiles.active=prod target/vibecoding-backend-0.0.1-SNAPSHOT.jar
```

## 📋 默认账号

| 用户名 | 密码   | 角色  | 邮箱                    |
|--------|--------|-------|-------------------------|
| admin  | 123456 | ADMIN | admin@vibecoding.com    |
| user   | 123456 | USER  | user@vibecoding.com     |

## 🤝 贡献

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目地址：https://github.com/your-org/vibecoding-backend
- 问题反馈：https://github.com/your-org/vibecoding-backend/issues

---

**VibeCode Team** - 让编程更有活力！ 🚀