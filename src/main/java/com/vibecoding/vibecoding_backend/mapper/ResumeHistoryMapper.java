package com.vibecoding.vibecoding_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vibecoding.vibecoding_backend.entity.ResumeHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 简历转换历史 Mapper 接口
 *
 * @author VibeCode Team
 */
@Mapper
public interface ResumeHistoryMapper extends BaseMapper<ResumeHistory> {
}
