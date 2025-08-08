
用户配置开发

我需要在用户数据表中新增一个 config 字段，类型为json ，用于保存用户相关配置，并且方便以后扩展。
为所有用户添加文档的默认类型为docx
示例：{"default_doc_type":"docx"}

并在UserController 中 新增两个接口具体如下：

接口一：/user/updateConfig
方法：POST
功能描述: 跟据auth中的用户标识更新用户配置。
content-type: application/json
请求内容： {config:'{"default_doc_type":"docx"}'}
返回：statusCode = 200，并返回所更新的配置信息json数据。

接口二：/user/getConfig
方法：GET
功能描述：跟据auth中的用户标识获取用户配置信息
content-type:application/json
参数：无
返回：状态200，返回用户配置json数据。

请跟据以上需求完善整个逻辑业务。


