package com.vibecoding.vibecoding_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vibecoding.vibecoding_backend.entity.User;
import com.vibecoding.vibecoding_backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    private final PasswordEncoder passwordEncoder;

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
     * 更新用户信息
     *
     * @param user 用户信息
     */
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        log.info("更新用户信息: {}", user.getEmail());
    }

    /**
     * 修改用户密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 由于当前系统使用验证码登录，密码字段为空
        // 这里提供一个简单的密码验证逻辑
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return false;
            }
        }

        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());
        
        int result = userMapper.updateById(user);
        log.info("修改用户密码: {}", user.getEmail());
        return result > 0;
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
     * 根据邮箱获取用户ID
     *
     * @param email 邮箱
     * @return 用户ID
     */
    public Long getUserIdByEmail(String email) {
        User user = findByEmail(email);
        return user != null ? user.getId() : null;
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