package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.dto.BatchDownloadRequest;
import com.vibecoding.vibecoding_backend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.List;

/**
 * 文件控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 文件上传接口
     *
     * @param files 文件列表
     * @param authentication 认证信息
     * @return 上传结果
     */
    @PostMapping("/upload")
    public Result<List<FileUploadResponse>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) {
        
        try {
            // 获取当前用户
            String createdBy = authentication != null ? authentication.getName() : "anonymous";
            
            log.info("文件上传请求: 用户={}, 文件数量={}", createdBy, files.length);
            
            // 上传文件
            List<FileService.FileUploadResult> results = fileService.uploadFiles(files, createdBy);
            
            // 构建返回数据
            List<FileUploadResponse> responseList = results.stream()
                    .map(result -> {
                        FileUploadResponse response = new FileUploadResponse();
                        response.setFilename(result.getFilename());
                        response.setFileUrl("");
                        return response;
                    })
                    .toList();
            
            log.info("文件上传成功: 用户={}, 成功数量={}", createdBy, 
                    results.stream().filter(FileService.FileUploadResult::isSuccess).count());
            
            return Result.success(responseList);
            
        } catch (IllegalArgumentException e) {
            log.warn("文件上传参数错误: {}", e.getMessage());
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error(500, "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 批量下载接口
     *
     * @param request 批量下载请求
     * @param authentication 认证信息
     * @return 文件资源
     */
    @PostMapping("/files/downloads")
    public ResponseEntity<Resource> batchDownload(
            @Valid @RequestBody BatchDownloadRequest request,
            Authentication authentication) {
        
        try {
            // 获取当前用户
            String username = authentication != null ? authentication.getName() : null;
            if (username == null) {
                return ResponseEntity.status(401).build();
            }
            
            log.info("批量下载请求: 用户={}, 文件数量={}", username, request.getFilenames().size());
            
            // 创建批量下载文件
            Path filePath = fileService.createBatchDownloadFile(request.getFilenames(), username);
            
            // 创建资源
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("批量下载文件不存在或不可读: {}", filePath);
                return ResponseEntity.notFound().build();
            }
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            String filename = filePath.getFileName().toString();
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            
            // 根据文件类型设置Content-Type
            String contentType = getContentType(filename);
            headers.setContentType(MediaType.parseMediaType(contentType));
            
            log.info("批量下载成功: {}", filePath);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
                    
        } catch (IllegalArgumentException e) {
            log.warn("批量下载参数错误: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (MalformedURLException e) {
            log.error("批量下载文件URL错误", e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("批量下载失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 文件下载接口
     *
     * @param path 路径
     * @param filename 文件名
     * @return 文件资源
     */
    @GetMapping("/files/{path}/{filename}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String path,
            @PathVariable String filename) {
        
        try {
            log.info("文件下载请求: path={}, filename={}", path, filename);
            
            // 获取文件路径
            Path filePath = fileService.downloadFile(path, filename);
            
            // 创建资源
            Resource resource = new UrlResource(filePath.toUri());
            
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("文件不存在或不可读: {}", filePath);
                return ResponseEntity.notFound().build();
            }
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            
            // 根据文件类型设置Content-Type
            String contentType = getContentType(filename);
            headers.setContentType(MediaType.parseMediaType(contentType));
            
            log.info("文件下载成功: {}", filePath);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
                    
        } catch (IllegalArgumentException e) {
            log.warn("文件下载参数错误: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (MalformedURLException e) {
            log.error("文件URL错误", e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取文件Content-Type
     *
     * @param filename 文件名
     * @return Content-Type
     */
    private String getContentType(String filename) {
        if (filename.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        } else if (filename.toLowerCase().endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else {
            return "application/octet-stream";
        }
    }

    /**
     * 文件上传响应类
     */
    public static class FileUploadResponse {
        private String filename;
        private String fileUrl;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
} 