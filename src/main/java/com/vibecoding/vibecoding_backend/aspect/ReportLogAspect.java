package com.vibecoding.vibecoding_backend.aspect;

import com.vibecoding.vibecoding_backend.service.ReportLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.vibecoding.vibecoding_backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 报表日志AOP切面
 *
 * @author VibeCode Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReportLogAspect {

    private final ReportLogService reportLogService;
    // @Autowired
    private final UserService userService;

    /**
     * 记录日志
     */
    public void recordLog(String interfaceName, long startTime, boolean success, String errorMessage) {
        try {
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            
            // 获取用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = "anonymous";
            Long userId = null;
            
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getName())) {
                email = authentication.getName();
                // 这里可以根据需要从UserService获取用户ID
                userId = userService.getUserIdByEmail(email);
                
            }

            // 构建日志实体
            com.vibecoding.vibecoding_backend.entity.ReportLog logEntity = new com.vibecoding.vibecoding_backend.entity.ReportLog();
            logEntity.setUserId(userId);
            logEntity.setEmail(email);
            logEntity.setInterfaceName(interfaceName);
            logEntity.setInterfacePath(request.getRequestURI());
            logEntity.setRequestMethod(request.getMethod());
            logEntity.setStatus(success ? 1 : 0);
            logEntity.setResponseTime(System.currentTimeMillis() - startTime);
            logEntity.setErrorMessage(errorMessage);
            logEntity.setIpAddress(getClientIpAddress(request));
            logEntity.setUserAgent(request.getHeader("User-Agent"));
            logEntity.setCreatedTime(LocalDateTime.now());
            logEntity.setUpdatedTime(LocalDateTime.now());

            // 异步保存日志
            reportLogService.saveLogAsync(logEntity);
            
        } catch (Exception e) {
            log.error("构建报表日志失败", e);
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 