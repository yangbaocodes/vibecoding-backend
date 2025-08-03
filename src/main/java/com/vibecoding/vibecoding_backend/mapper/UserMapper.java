package com.vibecoding.vibecoding_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vibecoding.vibecoding_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper接口
 *
 * @author VibeCode Team
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 