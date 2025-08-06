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
import reactor.util.retry.Retry;

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
     * @param targetLanguage 目标语言
     * @param targetFileType 目标文件类型
     * @return ResumeInfoResponse
     */
    public ResumeInfoResponse parseResumeInfo(String resumeUrl, String user, String responseMode, 
                                           String targetLanguage, String targetFileType) {
        DifyRequest request = null;
        try {
            log.info("开始解析简历信息，URL: {}, 用户: {}, 响应模式: {}, 目标语言: {}, 目标文件类型: {}", 
                resumeUrl, user, responseMode, targetLanguage, targetFileType);

            // 构建请求
            request = DifyRequest.buildResumeRequest(resumeUrl, user, responseMode, targetLanguage, targetFileType);
            log.info("构建的请求: {}", request);

            // 创建WebClient，配置超时
            WebClient webClient = webClientBuilder
                    .baseUrl(difyConfig.getBaseUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + difyConfig.getBearerToken())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.USER_AGENT, "VibeCode-Backend/1.0")
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(10 * 1024 * 1024)) // 10MB
                    .build();

            // 发送请求
            log.info("发送请求到Dify API: {} {}", difyConfig.getBaseUrl() + difyConfig.getWorkflowPath(), request);
            log.info("超时配置 - 连接超时: {}ms, 读取超时: {}ms", difyConfig.getConnectTimeout(), difyConfig.getReadTimeout());
            
            // 使用配置文件中的超时时间，并增加一些冗余时间
            long timeoutMs = Math.max(difyConfig.getReadTimeout() * 2, 90000); // 至少90秒，或者读取超时的2倍
            log.info("WebClient超时设置为: {}ms", timeoutMs);
            
            // 先获取原始响应，添加重试机制
            String rawResponse = webClient.post()
                    .uri(difyConfig.getWorkflowPath())
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(timeoutMs))
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))
                            .filter(throwable -> {
                                // 只对超时异常进行重试，避免对业务异常进行重试
                                boolean shouldRetry = throwable instanceof java.util.concurrent.TimeoutException ||
                                    (throwable.getMessage() != null && throwable.getMessage().contains("timeout"));
                                if (shouldRetry) {
                                    log.warn("Dify API调用超时，将进行重试: {}", throwable.getMessage());
                                }
                                return shouldRetry;
                            })
                            .doBeforeRetry(retrySignal -> 
                                log.info("正在重试Dify API调用，重试次数: {}", retrySignal.totalRetries() + 1)))
                    .doOnSubscribe(subscription -> log.info("开始调用Dify API，请求时间: {}", System.currentTimeMillis()))
                    .doOnNext(response -> log.info("收到Dify API响应，响应时间: {}", System.currentTimeMillis()))
                    .doOnError(error -> log.error("Dify API调用出错: {}", error.getMessage()))
                    .block();
            
            log.info("Dify API原始响应: {}", rawResponse);
            
            // 解析Dify API响应结构
            @SuppressWarnings("unchecked")
            Map<String, Object> difyResponse = objectMapper.readValue(rawResponse, Map.class);
            
            // 获取data.outputs.text字段
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) difyResponse.get("data");
            @SuppressWarnings("unchecked")
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
        } catch (RuntimeException e) {
            // 检查是否是超时异常
            if (e.getCause() instanceof java.util.concurrent.TimeoutException) {
                log.error("Dify API响应超时，超时时间: {}ms, 请求URL: {}", 
                    Math.max(difyConfig.getReadTimeout() * 2, 90000), 
                    difyConfig.getBaseUrl() + difyConfig.getWorkflowPath());
                if (request != null) {
                    log.error("超时时的请求体: {}", request);
                }
                throw new RuntimeException("Dify服务响应超时，请稍后重试或联系管理员");
            }
            // 检查异常消息是否包含超时信息
            if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                log.error("Dify API调用超时，超时时间: {}ms, 请求URL: {}", 
                    Math.max(difyConfig.getReadTimeout() * 2, 90000), 
                    difyConfig.getBaseUrl() + difyConfig.getWorkflowPath());
                if (request != null) {
                    log.error("超时时的请求体: {}", request);
                }
                throw new RuntimeException("Dify API调用超时，请检查网络连接和服务状态。如果问题持续，请稍后重试。");
            }
            // 检查是否是Reactor的异常
            if (e.getClass().getName().contains("ReactiveException")) {
                log.error("Reactive异常", e);
                throw new RuntimeException("简历解析异常: " + e.getMessage());
            }
            // 重新抛出其他RuntimeException
            throw e;
        } catch (Exception e) {
            log.error("简历解析异常，异常类型: {}, 异常消息: {}", e.getClass().getSimpleName(), e.getMessage(), e);
            if (request != null) {
                log.error("异常时的请求体: {}", request);
            }
            
            // 检查是否是超时相关的异常
            if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                throw new RuntimeException("简历解析超时，请稍后重试");
            }
            
            throw new RuntimeException("简历解析异常: " + e.getMessage());
        }
    }

    /**
     * 异步解析简历信息
     *
     * @param resumeUrl 简历URL
     * @param user 用户标识
     * @param responseMode 响应模式
     * @param targetLanguage 目标语言
     * @param targetFileType 目标文件类型
     * @return Mono<ResumeInfoResponse>
     */
    public Mono<ResumeInfoResponse> parseResumeInfoAsync(String resumeUrl, String user, String responseMode, 
                                                        String targetLanguage, String targetFileType) {
        return Mono.fromCallable(() -> parseResumeInfo(resumeUrl, user, responseMode, targetLanguage, targetFileType))
                .onErrorMap(throwable -> {
                    log.error("异步简历解析失败", throwable);
                    return new RuntimeException("异步简历解析失败: " + throwable.getMessage());
                });
    }

    /**
     * 验证简历文件非空
     *
     * @param fileName 简历文件
     * @return 是否有效
     */
    public boolean validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        return true;
    }
} 