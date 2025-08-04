package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.annotation.ReportLog;
import com.vibecoding.vibecoding_backend.aspect.ReportLogAspect;
import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import com.vibecoding.vibecoding_backend.dto.ResumeParseRequest;
import com.vibecoding.vibecoding_backend.service.DifyService;
import com.vibecoding.vibecoding_backend.util.ResumeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

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

    /**
     * 解析简历并生成Word文档
     *
     * @param request 简历生成请求
     * @return 生成的文件下载URL
     */
    @PostMapping("/generate")
    @ReportLog("简历生成")
    public Result<String> generateResume(@RequestBody ResumeParseRequest request) {
        
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String errorMessage = null;
        
        try {
            log.info("收到简历生成请求: {}", request);

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
            
            if (resumeInfo == null) {
                return Result.error(500, "简历解析失败");
            }

            // 生成Word文档
            String downloadUrl = resumeGenerator.generateResume(resumeInfo);

            log.info("简历生成成功，下载URL: {}", downloadUrl);
            success = true;
            return Result.success("简历生成成功", downloadUrl);

        } catch (Exception e) {
            log.error("简历生成失败", e);
            errorMessage = e.getMessage();
            return Result.error(500, "简历生成失败: " + e.getMessage());
        } finally {
            // 记录报表日志
            reportLogAspect.recordLog("简历生成", startTime, success, errorMessage);
        }
    }

    /**
     * 下载生成的简历文件
     *
     * @param filename 文件名
     * @return 文件下载响应
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        try {
            String filePath = "filetarget/" + filename;
            File file = new File(filePath);
            
            if (!file.exists()) {
                log.error("文件不存在: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 