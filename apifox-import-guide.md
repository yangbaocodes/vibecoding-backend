# Apifox API 导入指南

## 📋 导入步骤

### 1. 登录 Apifox
1. 访问 [Apifox官网](https://apifox.com)
2. 登录您的账户

### 2. 创建或选择项目
1. 在Apifox中创建新项目，项目名称设置为：`vibecoding`
2. 或者选择现有的 `vibecoding` 项目

### 3. 导入 OpenAPI 规范
1. 在项目中选择 **导入** 功能
2. 选择 **OpenAPI 3.0** 格式
3. 上传 `openapi-vibecoding.yaml` 文件
4. 点击 **导入** 按钮

### 4. 配置环境变量
导入后，建议配置以下环境变量：

#### 开发环境
```json
{
  "baseUrl": "http://localhost:8080/api",
  "token": "your-jwt-token-here"
}
```

#### 生产环境
```json
{
  "baseUrl": "https://vibecoding-api.aistudio.ltd/api",
  "token": "your-jwt-token-here"
}
```

## 🔧 API 接口分类

导入后，您将看到以下接口分类：

### 认证相关接口
- `POST /auth/send-verification-code` - 发送邮箱验证码
- `POST /auth/login-or-register` - 邮箱验证码登录/注册
- `POST /auth/logout` - 用户登出

### 用户相关接口
- `GET /user/info` - 获取用户信息

### 文件相关接口
- `POST /file/upload` - 文件上传
- `POST /file/files/downloads` - 批量文件下载
- `GET /file/files/{path}/{filename}` - 单个文件下载

### 简历相关接口
- `POST /resume/generate` - 简历生成
- `GET /resume/download/{filename}` - 下载生成的简历文件

### 系统相关接口
- `GET /system/health` - 系统健康检查
- `GET /system/config` - 获取系统配置
- `POST /system/create-file-table` - 创建文件信息表

### 报表相关接口
- `GET /report/yearly-daily-calls` - 获取用户一年内每天调用接口的累计次数

## 🔐 认证配置

### JWT Token 配置
1. 在Apifox中创建 **全局参数**
2. 参数名：`Authorization`
3. 参数值：`Bearer {{token}}`
4. 应用到需要认证的接口

### 获取 Token 步骤
1. 调用 `POST /auth/send-verification-code` 发送验证码
2. 调用 `POST /auth/login-or-register` 登录获取token
3. 将返回的token设置到环境变量中

## 📝 测试建议

### 1. 基础功能测试
1. **健康检查**: 先测试 `GET /system/health` 确认服务正常
2. **用户注册**: 测试邮箱验证码注册流程
3. **文件上传**: 测试文件上传功能
4. **简历转换**: 测试简历生成功能

### 2. 认证流程测试
```bash
# 1. 发送验证码
POST /auth/send-verification-code
{
  "email": "test@example.com"
}

# 2. 登录获取token
POST /auth/login-or-register
{
  "email": "test@example.com",
  "verificationCode": "123456"
}

# 3. 使用token访问其他接口
GET /user/info
Authorization: Bearer <token>
```

### 3. 文件处理测试
```bash
# 1. 上传文件
POST /file/upload
Content-Type: multipart/form-data
files: [选择文件]

# 2. 生成简历
POST /resume/generate
{
  "fileName": "uploaded-file-name.docx",
  "responseMode": "blocking"
}

# 3. 批量下载
POST /file/files/downloads
{
  "filenames": ["file1.docx", "file2.pdf"]
}
```

## 🚨 注意事项

### 1. 文件上传限制
- 支持格式：DOCX、PDF
- 文件大小限制：5MB
- 单次最多上传10个文件

### 2. 认证要求
- 大部分接口需要JWT token认证
- 文件下载接口无需认证
- 系统健康检查无需认证

### 3. 错误处理
- 所有接口都返回统一的 `Result` 格式
- 错误码：200成功，400参数错误，401未授权，404未找到，500服务器错误

### 4. 环境配置
- 开发环境：`http://localhost:8080/api`
- 生产环境：`https://vibecoding-api.aistudio.ltd/api`

## 📞 技术支持

如果在导入过程中遇到问题，请检查：

1. **文件格式**: 确保YAML文件格式正确
2. **网络连接**: 确保能够访问Apifox服务
3. **权限设置**: 确保有项目的导入权限
4. **环境配置**: 确保环境变量配置正确

## 🔄 更新API

当后端API有更新时：

1. 重新生成 `openapi-vibecoding.yaml` 文件
2. 在Apifox中选择 **重新导入**
3. 选择 **覆盖现有接口** 选项
4. 确认导入更新

---

**提示**: 建议在导入后立即测试几个关键接口，确保API文档与实际服务一致。 