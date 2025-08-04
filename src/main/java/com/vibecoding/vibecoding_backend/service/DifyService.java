package com.vibecoding.vibecoding_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibecoding.vibecoding_backend.config.DifyConfig;
import com.vibecoding.vibecoding_backend.dto.DifyRequest;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * Dify服务类
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DifyService {

    private final DifyConfig difyConfig;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    /**
     * 解析简历信息
     *
     * @param resumeUrl 简历URL
     * @param user 用户标识
     * @param responseMode 响应模式
     * @return 简历信息
     */
    public ResumeInfoResponse parseResumeInfo(String resumeUrl, String user, String responseMode) {
        DifyRequest request = null;
        try {
            log.info("开始解析简历信息，URL: {}, 用户: {}, 响应模式: {}", resumeUrl, user, responseMode);

            // 构建请求
            request = DifyRequest.buildResumeRequest(resumeUrl, user, responseMode);
            log.info("构建的请求: {}", request);

            // 创建WebClient
            WebClient webClient = webClientBuilder
                    .baseUrl(difyConfig.getBaseUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + difyConfig.getBearerToken())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.USER_AGENT, "VibeCode-Backend/1.0")
                    .build();

            // 发送请求
            log.info("发送请求到Dify API: {} {}", difyConfig.getBaseUrl() + difyConfig.getWorkflowPath(), request);
            
            // 先获取原始响应
            String rawResponse = webClient.post()
                    .uri(difyConfig.getWorkflowPath())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(60000)) // 增加到60秒
                    .block();
            
            log.info("Dify API原始响应: {}", rawResponse);
            
            // 解析Dify API响应结构
            Map<String, Object> difyResponse = objectMapper.readValue(rawResponse, Map.class);
            
            // 获取data.outputs.text字段
            Map<String, Object> data = (Map<String, Object>) difyResponse.get("data");
            Map<String, Object> outputs = (Map<String, Object>) data.get("outputs");
            String textContent = (String) outputs.get("text");
            
            log.info("解析出的文本内容: {}", textContent);
            
            // 解析文本内容为ResumeInfoResponse
            ResumeInfoResponse response = objectMapper.readValue(textContent, ResumeInfoResponse.class);

            if (response != null) {
                log.info("简历解析成功，响应: {}", response);
                return response;
            } else {
                log.error("简历解析失败，响应为空");
                throw new RuntimeException("简历解析失败");
            }

        } catch (WebClientResponseException e) {
            log.error("Dify API调用失败，状态码: {}, 响应: {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (request != null) {
                log.error("请求体: {}", request);
            }
            throw new RuntimeException("Dify API调用失败: " + e.getMessage() + ", 响应: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("简历解析异常", e);
            throw new RuntimeException("简历解析异常: " + e.getMessage());
        }
    }

    /**
     * 异步解析简历信息
     *
     * @param resumeUrl 简历URL
     * @param user 用户标识
     * @param responseMode 响应模式
     * @return Mono<ResumeInfoResponse>
     */
    public Mono<ResumeInfoResponse> parseResumeInfoAsync(String resumeUrl, String user, String responseMode) {
        return Mono.fromCallable(() -> parseResumeInfo(resumeUrl, user, responseMode))
                .onErrorMap(throwable -> {
                    log.error("异步简历解析失败", throwable);
                    return new RuntimeException("异步简历解析失败: " + throwable.getMessage());
                });
    }

    /**
     * 验证简历URL
     *
     * @param resumeUrl 简历URL
     * @return 是否有效
     */
    public boolean validateResumeUrl(String resumeUrl) {
        if (resumeUrl == null || resumeUrl.trim().isEmpty()) {
            return false;
        }
        
        // 检查URL格式
        try {
            new java.net.URL(resumeUrl);
            return true;
        } catch (Exception e) {
            log.warn("无效的简历URL: {}", resumeUrl);
            return false;
        }
    }
} 