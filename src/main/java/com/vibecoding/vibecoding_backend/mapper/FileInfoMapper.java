package com.vibecoding.vibecoding_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vibecoding.vibecoding_backend.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件信息Mapper接口
 *
 * @author VibeCode Team
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
} 