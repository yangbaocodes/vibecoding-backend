package com.vibecoding.vibecoding_backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 邮箱服务测试类
 *
 * @author VibeCode Team
 */
@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testGenerateVerificationCode() {
        // 测试验证码生成
        String code1 = emailService.generateVerificationCode();
        String code2 = emailService.generateVerificationCode();
        
        assertNotNull(code1);
        assertNotNull(code2);
        assertEquals(6, code1.length());
        assertEquals(6, code2.length());
        assertTrue(code1.matches("\\d{6}"));
        assertTrue(code2.matches("\\d{6}"));
        assertNotEquals(code1, code2); // 两次生成的验证码应该不同
    }

    @Test
    public void testVerifyCode() {
        String email = "test@example.com";
        String code = "123456";
        
        // 先存储验证码到Redis
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, code, 5, java.util.concurrent.TimeUnit.MINUTES);
        
        // 测试验证成功
        assertTrue(emailService.verifyCode(email, code));
        
        // 验证码应该被删除
        assertNull(redisTemplate.opsForValue().get(redisKey));
        
        // 测试验证失败
        assertFalse(emailService.verifyCode(email, "654321"));
    }
} 