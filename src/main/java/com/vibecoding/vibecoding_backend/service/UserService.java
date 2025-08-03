package com.vibecoding.vibecoding_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vibecoding.vibecoding_backend.entity.User;
import com.vibecoding.vibecoding_backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务类
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final EmailService emailService;

    /**
     * 通过邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public User findByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        queryWrapper.eq("deleted", 0);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 创建新用户
     *
     * @param email    邮箱
     * @param nickname 昵称
     * @return 用户信息
     */
    public User createUser(String email, String nickname) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(generateUsername(email));
        user.setNickname(nickname != null ? nickname : "用户" + System.currentTimeMillis());
        user.setRole("USER");
        user.setStatus(1);
        user.setPassword(""); // 设置空密码，因为使用验证码登录
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0);

        userMapper.insert(user);
        log.info("创建新用户: {}", user.getEmail());
        return user;
    }

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     */
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     */
    public boolean verifyEmailCode(String email, String code) {
        return emailService.verifyCode(email, code);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String email) {
        return emailService.sendVerificationCode(email);
    }

    /**
     * 根据邮箱生成用户名
     *
     * @param email 邮箱
     * @return 用户名
     */
    private String generateUsername(String email) {
        String baseUsername = email.substring(0, email.indexOf("@"));
        String username = baseUsername;
        int suffix = 1;
        
        // 检查用户名是否已存在，如果存在则添加后缀
        while (isUsernameExists(username)) {
            username = baseUsername + suffix;
            suffix++;
        }
        
        return username;
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    private boolean isUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("deleted", 0);
        return userMapper.selectCount(queryWrapper) > 0;
    }
} 