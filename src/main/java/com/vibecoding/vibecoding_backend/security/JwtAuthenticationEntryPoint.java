package com.vibecoding.vibecoding_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT认证入口点
 * 当用户访问需要认证的资源但未提供有效凭据时触发
 *
 * @author VibeCode Team
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        
        log.warn("用户访问受保护资源但未提供有效认证信息: {}", authException.getMessage());
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        Result<Void> result = Result.error(ResultCode.UNAUTHORIZED);
        String jsonResult = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResult);
    }
}