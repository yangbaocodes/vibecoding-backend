package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import com.vibecoding.vibecoding_backend.dto.ResumeParseRequest;
import com.vibecoding.vibecoding_backend.service.DifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;

/**
 * Dify控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/dify")
@RequiredArgsConstructor
@Validated
public class DifyController {

    private final DifyService difyService;

    /**
     * 解析简历信息
     *
     * @param request 简历解析请求
     * @return 简历信息
     */
    @PostMapping("/parse-resume")
    public Result<ResumeInfoResponse> parseResumeInfo(@RequestBody ResumeParseRequest request) {
        
        try {
            log.info("收到简历解析请求: {}", request);

            // 验证URL
            if (!difyService.validateResumeUrl(request.getResumeUrl())) {
                return Result.error(400, "无效的简历URL");
            }

            // 解析简历
            ResumeInfoResponse resumeInfo = difyService.parseResumeInfo(
                request.getResumeUrl(), 
                request.getUser(), 
                request.getResponseMode()
            );

            // 检查响应是否为空
            if (resumeInfo == null) {
                log.error("简历解析失败，响应为空");
                return Result.error(500, "简历解析失败：响应为空");
            }

            log.info("简历解析成功，姓名: {}", resumeInfo.getName() != null ? resumeInfo.getName() : "未知");
            return Result.success("简历解析成功", resumeInfo);

        } catch (Exception e) {
            log.error("简历解析失败", e);
            return Result.error(500, "简历解析失败: " + e.getMessage());
        }
    }

    /**
     * 异步解析简历信息
     *
     * @param request 简历解析请求
     * @return 简历信息
     */
    @PostMapping("/parse-resume-async")
    public Result<String> parseResumeInfoAsync(@RequestBody ResumeParseRequest request) {
        
        try {
            log.info("收到异步简历解析请求: {}", request);

            // 验证URL
            if (!difyService.validateResumeUrl(request.getResumeUrl())) {
                return Result.error(400, "无效的简历URL");
            }

            // 异步解析简历
            difyService.parseResumeInfoAsync(request.getResumeUrl(), request.getUser(), request.getResponseMode())
                    .subscribe(
                            resumeInfo -> log.info("异步简历解析成功，姓名: {}", resumeInfo.getName()),
                            error -> log.error("异步简历解析失败", error)
                    );

            return Result.success("异步简历解析任务已启动");

        } catch (Exception e) {
            log.error("启动异步简历解析失败", e);
            return Result.error(500, "启动异步简历解析失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("Dify服务正常");
    }
} 