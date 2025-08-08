package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.annotation.ReportLog;
import com.vibecoding.vibecoding_backend.aspect.ReportLogAspect;
import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import com.vibecoding.vibecoding_backend.dto.ResumeParseRequest;
import com.vibecoding.vibecoding_backend.service.DifyService;
import com.vibecoding.vibecoding_backend.service.ResumeHistoryService;
import com.vibecoding.vibecoding_backend.util.ResumeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.vibecoding.vibecoding_backend.config.FileConfig;

import jakarta.validation.Valid;

/**
 * 简历控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final DifyService difyService;
    private final ResumeGenerator resumeGenerator;
    private final ReportLogAspect reportLogAspect;
    private final ResumeHistoryService resumeHistoryService;
    private final FileConfig fileConfig;
    
    /**
     * 解析简历并生成Word文档
     *
     * @param request 简历生成请求
     * @param authentication 认证信息
     * @return 生成的文件下载URL
     */
    @PostMapping("/generate")
    @ReportLog("简历生成")
    public Result<String> generateResume(@Valid @RequestBody ResumeParseRequest request, Authentication authentication) {
        
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String errorMessage = null;
        
        try {
            log.info("收到简历生成请求: {}, targetLanguage: {}, targetFileType: {}", 
                request, request.getTargetLanguage(), request.getTargetFileType());

            // 验证URL
            if (!difyService.validateFileName(request.getFileName())) {
                return Result.error(400, "无效的简历文件");
            }

            // 验证目标语言
            if (!"en".equals(request.getTargetLanguage()) && !"zh".equals(request.getTargetLanguage())) {
                return Result.error(400, "请求参数targetLanguage只能是 English 或 Chinese");
            }

            // 验证目标文件类型
            if (request.getTargetFileType() != null && 
                !"word".equals(request.getTargetFileType()) && 
                !"ppt".equals(request.getTargetFileType())) {
                return Result.error(400, "请求参数targetFileType只能是 word 或 ppt");
            }

            String resumeUrl = fileConfig.buildDownloadUrl(fileConfig.getStoragePath(), request.getFileName());

            // 解析简历
            ResumeInfoResponse resumeInfo = difyService.parseResumeInfo(
                resumeUrl, 
                authentication.getName(), 
                request.getResponseMode(),
                request.getTargetLanguage(),
                request.getTargetFileType()
            );
            
            if (resumeInfo == null) {
                return Result.error(500, "简历解析失败");
            }

            // 生成Word文档
            ResumeGenerator.ResumeGenerationResult result = resumeGenerator.generateResume(resumeInfo);
            
            result.setFilename(request.getFileName());
            
            // 记录转换历史
            recordResumeHistory(request, result, authentication.getName(), null);

            success = true;
            return Result.success("简历生成成功",request.getFileName());

        } catch (Exception e) {
            log.error("简历生成失败", e);
            errorMessage = e.getMessage();
            
            // 记录转换失败历史
            recordResumeHistory(request, null, authentication.getName(), e.getMessage());
            
            return Result.error(500, "简历生成失败: " + e.getMessage());
        } finally {
            // 记录报表日志
            reportLogAspect.recordLog("简历生成", startTime, success, errorMessage);
        }
    }
    
    /**
     * 记录简历转换历史
     */
    private void recordResumeHistory(ResumeParseRequest request, ResumeGenerator.ResumeGenerationResult result, String createdBy, String errorMessage) {
        try {
            String sourceName = request.getFileName();
            String sourceType = getFileTypeFromFileName(sourceName);
            String sourcePath = fileConfig.buildDownloadUrl(fileConfig.getStoragePath(), sourceName);
            String targetName = result != null ? result.getFilename() : null;
            String targetType = request.getTargetFileType() != null ? request.getTargetFileType() : "word";
            String targetLanguage = request.getTargetLanguage();
            Integer translateStatus = result != null ? 1 : 0; // 1-成功，0-失败
            
            resumeHistoryService.recordHistory(
                sourceName, sourceType, sourcePath,
                targetName, targetType, targetLanguage,
                translateStatus, createdBy, errorMessage
            );
        } catch (Exception e) {
            log.error("记录简历转换历史失败", e);
            // 不抛出异常，避免影响主流程
        }
    }
    
    /**
     * 从文件名获取文件类型
     */
    private String getFileTypeFromFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "unknown";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        
        return "unknown";
    }


} 