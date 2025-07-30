package com.vibecoding.vibecoding_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT访问拒绝处理器
 * 当用户拥有有效凭据但权限不足时触发
 *
 * @author VibeCode Team
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, 
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException {
        
        log.warn("用户访问资源但权限不足: {}", accessDeniedException.getMessage());
        
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        Result<Void> result = Result.error(ResultCode.FORBIDDEN);
        String jsonResult = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResult);
    }
}