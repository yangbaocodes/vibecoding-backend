# 用户配置功能使用示例

## 功能概述

用户配置功能支持用户个性化设置，包括文档类型、主题、语言等配置项。系统支持部分更新，用户只需传递需要修改的字段即可。

## 配置字段说明

| 字段名 | 类型 | 可选值 | 说明 |
|--------|------|--------|------|
| default_doc_type | String | docx, pdf, txt | 默认文档类型 |
| theme | String | light, dark, auto | 主题设置 |
| language | String | zh, en | 语言设置 |
| auto_save | Boolean | true, false | 自动保存 |
| file_preview | Boolean | true, false | 文件预览设置 |
| notification_enabled | Boolean | true, false | 通知设置 |

## API 接口

### 1. 获取允许的配置字段列表

```bash
GET /api/user/config/fields
Authorization: Bearer <your-jwt-token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "获取允许的配置字段成功",
  "data": [
    "default_doc_type",
    "theme", 
    "language",
    "auto_save",
    "file_preview",
    "notification_enabled"
  ]
}
```

### 2. 获取用户当前配置

```bash
GET /api/user/getConfig
Authorization: Bearer <your-jwt-token>
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "config": "{\"default_doc_type\":\"docx\",\"theme\":\"light\",\"auto_save\":true}"
  }
}
```

### 3. 更新用户配置（支持部分更新）

#### 示例1：只更新文档类型

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"default_doc_type\":\"pdf\"}"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "配置更新成功",
  "data": {
    "config": "{\"default_doc_type\":\"pdf\",\"theme\":\"light\",\"auto_save\":true}"
  }
}
```

#### 示例2：只更新主题

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"theme\":\"dark\"}"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "配置更新成功", 
  "data": {
    "config": "{\"default_doc_type\":\"pdf\",\"theme\":\"dark\",\"auto_save\":true}"
  }
}
```

#### 示例3：同时更新多个字段

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"theme\":\"dark\",\"language\":\"zh\",\"auto_save\":false}"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "配置更新成功",
  "data": {
    "config": "{\"default_doc_type\":\"pdf\",\"theme\":\"dark\",\"language\":\"zh\",\"auto_save\":false}"
  }
}
```

## 错误处理

### 1. 无效字段错误

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"invalid_field\":\"value\"}"
}
```

**响应示例**:
```json
{
  "code": 500,
  "message": "更新配置失败: 配置验证失败: 不允许的配置字段: invalid_field。允许的字段: default_doc_type, theme, language, auto_save, file_preview, notification_enabled",
  "data": null
}
```

### 2. 无效值错误

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"default_doc_type\":\"invalid_type\"}"
}
```

**响应示例**:
```json
{
  "code": 500,
  "message": "更新配置失败: 配置验证失败: default_doc_type 字段值必须是 docx、pdf 或 txt",
  "data": null
}
```

### 3. 类型错误

```bash
POST /api/user/updateConfig
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "config": "{\"auto_save\":\"true\"}"
}
```

**响应示例**:
```json
{
  "code": 500,
  "message": "更新配置失败: 配置验证失败: auto_save 字段必须是布尔类型",
  "data": null
}
```

## 最佳实践

1. **部分更新**: 只传递需要修改的字段，减少数据传输量
2. **字段验证**: 使用 `/api/user/config/fields` 接口获取允许的字段列表
3. **错误处理**: 根据错误信息调整请求参数
4. **配置合并**: 系统会自动合并现有配置和新配置，无需手动处理

## 默认配置

新用户首次获取配置时，系统会自动设置默认配置：

```json
{
  "default_doc_type": "docx"
}
```

## 注意事项

1. 所有配置字段都是可选的，用户可以根据需要设置
2. 系统会验证字段类型和值的合法性
3. 部分更新功能会自动保留现有配置中未修改的字段
4. 配置更新是原子操作，要么全部成功，要么全部失败 