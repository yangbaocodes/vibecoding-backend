package com.vibecoding.vibecoding_backend.security;

import com.vibecoding.vibecoding_backend.entity.User;
import com.vibecoding.vibecoding_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 自定义UserDetailsService实现
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            // 根据邮箱查找用户
            User user = userService.findByEmail(email);
            
            if (user == null) {
                log.warn("用户不存在: {}", email);
                throw new UsernameNotFoundException("用户不存在: " + email);
            }
            
            // 检查用户状态
            if (user.getStatus() != 1) {
                log.warn("用户状态异常: {}, status={}", email, user.getStatus());
                throw new UsernameNotFoundException("用户状态异常: " + email);
            }
            
            // 创建UserDetails对象
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password("") // JWT认证不需要密码
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(user.getStatus() != 1)
                    .build();
                    
        } catch (Exception e) {
            log.error("加载用户详情失败: {}", email, e);
            throw new UsernameNotFoundException("加载用户详情失败: " + email, e);
        }
    }
} 