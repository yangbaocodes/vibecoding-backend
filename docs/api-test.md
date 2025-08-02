# VibeCode Backend API 测试文档

## 认证相关接口

### 1. 发送邮箱验证码

**接口**: `POST /api/auth/send-verification-code`

**请求参数**:
```json
{
  "email": "test@example.com"
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

### 2. 统一的注册/登录接口

**接口**: `POST /api/auth/login-or-register`

**请求参数**:
```json
{
  "email": "test@example.com",
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
      "username": "test",
      "email": "test@example.com",
      "nickname": "用户1234567890",
      "role": "USER",
      "status": 1
    }
  },
  "timestamp": 1640995200000
}
```

### 3. 用户登出

**接口**: `POST /api/auth/logout`

**请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "登出成功",
  "data": null,
  "timestamp": 1640995200000
}
```

## 测试步骤

### 1. 发送验证码
```bash
curl -X POST http://localhost:8081/api/auth/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com"
  }'
```

### 2. 注册/登录
```bash
curl -X POST http://localhost:8081/api/auth/login-or-register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "verificationCode": "123456"
  }'
```

### 3. 使用Token访问受保护的接口
```bash
curl -X GET http://localhost:8081/api/user/info \
  -H "Authorization: Bearer <your-jwt-token>"
```

## 功能说明

1. **验证码登录**: 用户通过邮箱验证码进行登录，无需密码
2. **自动注册**: 如果用户不存在，系统会自动创建新用户
3. **JWT Token**: 登录成功后返回JWT Bearer Token
4. **用户信息**: 返回用户基本信息，包括ID、用户名、邮箱、昵称等
5. **状态标识**: `isNewUser`字段标识是否为新注册用户

## 注意事项

1. 验证码有效期为5分钟
2. 验证码为6位纯数字
3. 用户名根据邮箱自动生成
4. 昵称默认为"用户" + 时间戳
5. 新用户默认角色为"USER"
6. JWT Token有效期为24小时 