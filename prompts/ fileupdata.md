我需要实现文件上传模块
需要两个接口：
接口一：
接口名称：fileupload
方法：POST
参数：files
接口描述：用户可上传最多不超过10个文件，文件格式为docx、pdf，其余格式暂不支持，上传后存放在当前目录下的 filesource 文件夹内，如果文件夹不存在则创建。文件名以uuid存放，防止重复，需要向数据库插入一条对应数据，数据字段包含 filename-文件名（uuid文件名）,file_real_name-真实文件名，path-文件路径,createdtime-创建时间,createdby-创建人，target_path-目标路径，is_translated-是否转换（默认否）等信息，
接口返回：以json数组返回 filename-文件名，file_url-文件下载地址

接口二：
接口名称：files\{path}\{filename}
方法：GET
参数：filename
接口描述：用户可通过path和filename 找到相应的docx或pdf文件并下载，此接口不需要token验证，需要实现流览器下载协议。接口的url与【接口一】返回的file_url一致。