package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.annotation.ReportLog;
import com.vibecoding.vibecoding_backend.aspect.ReportLogAspect;
import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import com.vibecoding.vibecoding_backend.dto.ResumeParseRequest;
import com.vibecoding.vibecoding_backend.entity.FileInfo;
import com.vibecoding.vibecoding_backend.mapper.FileInfoMapper;
import com.vibecoding.vibecoding_backend.service.DifyService;
import com.vibecoding.vibecoding_backend.util.ResumeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.vibecoding.vibecoding_backend.config.FileConfig;

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
    private final FileInfoMapper fileInfoMapper;
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
    public Result<String> generateResume(@RequestBody ResumeParseRequest request, Authentication authentication) {
        
        long startTime = System.currentTimeMillis();
        boolean success = false;
        String errorMessage = null;
        
        try {
            log.info("收到简历生成请求: {}", request);

            // 验证URL
            if (!difyService.validateFileName(request.getFileName())) {
                return Result.error(400, "无效的简历文件");
            }

            String resumeUrl = fileConfig.buildDownloadUrl(fileConfig.getStoragePath(), request.getFileName());

            // 解析简历
            ResumeInfoResponse resumeInfo = difyService.parseResumeInfo(
                resumeUrl, 
                authentication.getName(), 
                request.getResponseMode()
            );
            
            if (resumeInfo == null) {
                return Result.error(500, "简历解析失败");
            }

            // 生成Word文档
            ResumeGenerator.ResumeGenerationResult result = resumeGenerator.generateResume(resumeInfo);
            
            result.setFilename(request.getFileName());
            // 保存文件信息到数据库
            saveFileInfoToDatabase(result, authentication);

            success = true;
            return Result.success("简历生成成功",request.getFileName());

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
     * 保存文件信息到数据库
     */
    private void saveFileInfoToDatabase(ResumeGenerator.ResumeGenerationResult result, Authentication authentication) {
        try {
            // 根据filename查找现有记录
            FileInfo existingFile = fileInfoMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FileInfo>()
                    .eq("filename", result.getFilename())
            );
            
            if (existingFile != null) {
                // 更新现有记录
                existingFile.setTargetPath(result.getTargetPath());
                existingFile.setIsTranslated(1); // 已转换
                
                fileInfoMapper.updateById(existingFile);
                log.info("文件信息已更新到数据库: {}", result.getFilename());
            } else {
                log.warn("未找到对应的文件记录: {}", result.getFilename());
            }
            
        } catch (Exception e) {
            log.error("更新文件信息到数据库失败", e);
            // 不抛出异常，避免影响简历生成流程
        }
    }


} 