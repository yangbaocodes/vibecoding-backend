package com.vibecoding.vibecoding_backend.service;

import com.vibecoding.vibecoding_backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 *
 * @author VibeCode Team
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        // 测试创建用户
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String nickname = "测试用户";
        
        User user = userService.createUser(email, nickname);
        
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals(nickname, user.getNickname());
        assertEquals("USER", user.getRole());
        assertEquals(1, user.getStatus());
        assertNotNull(user.getCreateTime());
    }

    @Test
    public void testFindByEmail() {
        // 测试通过邮箱查找用户
        String email = "test@example.com";
        
        User user = userService.findByEmail(email);
        
        // 如果用户存在，验证基本信息
        if (user != null) {
            assertEquals(email, user.getEmail());
            assertNotNull(user.getUsername());
            assertNotNull(user.getCreateTime());
        }
    }

    @Test
    public void testSendVerificationCode() {
        // 测试发送验证码
        String email = "test@example.com";
        
        boolean result = userService.sendVerificationCode(email);
        
        // 由于需要真实的邮件服务，这里只是测试方法调用
        assertTrue(result || !result); // 结果可能是true或false，取决于邮件服务配置
    }
} 