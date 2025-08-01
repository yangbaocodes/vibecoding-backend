# API 测试文档

## 邮箱验证码功能测试

### 1. 发送验证码

**请求**:
```bash
curl -X POST http://localhost:8081/api/user/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": null,
  "timestamp": 1640995200000
}
```

### 2. 参数验证测试

**无效邮箱格式**:
```bash
curl -X POST http://localhost:8081/api/user/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid-email"
  }'
```

**预期响应**:
```json
{
  "code": 400,
  "message": "邮箱格式不正确",
  "data": null,
  "timestamp": 1640995200000
}
```

**空邮箱参数**:
```bash
curl -X POST http://localhost:8081/api/user/send-verification-code \
  -H "Content-Type: application/json" \
  -d '{
    "email": ""
  }'
```

**预期响应**:
```json
{
  "code": 400,
  "message": "邮箱地址不能为空",
  "data": null,
  "timestamp": 1640995200000
}
```

## 功能特性

1. **验证码格式**: 6位纯数字
2. **有效期**: 5分钟
3. **存储方式**: Redis缓存
4. **邮件服务器**: 163邮箱 (smtp.163.com)
5. **发送邮箱**: duoqin@163.com

## 注意事项

- 验证码发送频率限制（建议实现）
- 邮箱格式验证
- Redis连接状态检查
- 邮件发送失败处理 