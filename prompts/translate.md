修改 /resume/generate 接口
原来转换成功后保存到 fileinfo 表的功能请帮我去掉。

需要一个新增一个转换历史表：resume_history ，
通过 /resume/generate 生成的文件 都对应一条转换记录，
 每个文件转换都记录一下，转换前的文件名:source_name，转换前的文件类型:source_type，转换前的存储位置:source_path，转换后的文件名:targe_name，转换后的文件类型:target_type，转后后的文件语言:targe_language，转换时间:translate_time，转换状态:translate_status(0失败，1成功)，操作人：createdby等



