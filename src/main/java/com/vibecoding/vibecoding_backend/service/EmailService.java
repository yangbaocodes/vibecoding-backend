package com.vibecoding.vibecoding_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱服务类
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String VERIFICATION_CODE_PREFIX = "email:verification:";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRE_MINUTES = 5;

    /**
     * 发送验证码到指定邮箱
     *
     * @param email 邮箱地址
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String email) {
        try {
            // 生成6位数字验证码
            String verificationCode = generateVerificationCode();
            
            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("duoqin@163.com");
            message.setTo(email);
            message.setSubject("VibeCode - 邮箱验证码");
            message.setText("您的验证码是：" + verificationCode + "\n\n验证码有效期为5分钟，请及时使用。\n\n如果这不是您的操作，请忽略此邮件。");
            
            mailSender.send(message);
            
            // 将验证码存储到Redis，设置5分钟过期
            String redisKey = VERIFICATION_CODE_PREFIX + email;
            redisTemplate.opsForValue().set(redisKey, verificationCode, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            log.info("验证码已发送到邮箱: {}, 验证码: {}", email, verificationCode);
            return true;
            
        } catch (Exception e) {
            log.error("发送验证码失败，邮箱: {}, 错误: {}", email, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱地址
     * @param code  验证码
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code) {
        try {
            String redisKey = VERIFICATION_CODE_PREFIX + email;
            String storedCode = redisTemplate.opsForValue().get(redisKey);
            
            if (storedCode != null && storedCode.equals(code)) {
                // 验证成功后删除验证码
                redisTemplate.delete(redisKey);
                log.info("邮箱验证码验证成功: {}", email);
                return true;
            }
            
            log.warn("邮箱验证码验证失败: {}, 输入码: {}, 存储码: {}", email, code, storedCode);
            return false;
            
        } catch (Exception e) {
            log.error("验证邮箱验证码时发生错误，邮箱: {}, 错误: {}", email, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
} 