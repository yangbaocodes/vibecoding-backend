package com.vibecoding.vibecoding_backend.service;

import com.vibecoding.vibecoding_backend.config.FileConfig;
import com.vibecoding.vibecoding_backend.entity.FileInfo;
import com.vibecoding.vibecoding_backend.mapper.FileInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件服务类
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileInfoMapper fileInfoMapper;
    private final FileConfig fileConfig;

    /**
     * 支持的文件类型
     */
    private static final List<String> SUPPORTED_TYPES = List.of("docx", "pdf");

    /**
     * 最大文件数量
     */
    private static final int MAX_FILES = 10;


    /**
     * 上传文件
     *
     * @param files 文件列表
     * @param createdBy 创建人
     * @return 上传结果列表
     */
    public List<FileUploadResult> uploadFiles(MultipartFile[] files, String createdBy) {
        List<FileUploadResult> results = new ArrayList<>();

        // 检查文件数量
        if (files.length > MAX_FILES) {
            throw new IllegalArgumentException("最多只能上传" + MAX_FILES + "个文件");
        }

        // 确保存储目录存在
        createStorageDirectory();

        for (MultipartFile file : files) {
            try {
                FileUploadResult result = uploadSingleFile(file, createdBy);
                results.add(result);
            } catch (Exception e) {
                log.error("文件上传失败: {}", file.getOriginalFilename(), e);
                FileUploadResult errorResult = new FileUploadResult();
                errorResult.setFilename("");
                errorResult.setFileUrl("");
                errorResult.setSuccess(false);
                errorResult.setMessage("文件上传失败: " + e.getMessage());
                results.add(errorResult);
            }
        }

        return results;
    }

    /**
     * 上传单个文件
     *
     * @param file 文件
     * @param createdBy 创建人
     * @return 上传结果
     */
    private FileUploadResult uploadSingleFile(MultipartFile file, String createdBy) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);

        // 检查文件类型
        if (!SUPPORTED_TYPES.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型: " + fileExtension + "，仅支持: " + SUPPORTED_TYPES);
        }

        // 生成UUID文件名
        String uuidFilename = UUID.randomUUID().toString() + "." + fileExtension;
        String sourcePath = fileConfig.getStoragePath() + "/" + uuidFilename;
        Path filePath = Paths.get(sourcePath);

        // 保存文件
        Files.write(filePath, file.getBytes());

        // 保存到数据库
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(uuidFilename);
        fileInfo.setFileRealName(originalFilename);
        fileInfo.setPath(sourcePath);
        fileInfo.setCreatedBy(createdBy);
        fileInfo.setTargetPath("");
        fileInfo.setIsTranslated(0); // 默认未转换
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(fileExtension);
        fileInfo.setCreatedTime(LocalDateTime.now());

        fileInfoMapper.insert(fileInfo);

        // 构建下载URL
        String downloadUrl = fileConfig.buildDownloadUrl(fileConfig.getStoragePath(), uuidFilename);

        // 返回结果
        FileUploadResult result = new FileUploadResult();
        result.setFilename(uuidFilename);
        result.setFileUrl(downloadUrl);
        result.setSuccess(true);
        result.setMessage("文件上传成功");

        log.info("文件上传成功: {}, 存储路径: {}", originalFilename, sourcePath);
        return result;
    }

    /**
     * 下载文件
     *
     * @param path 路径
     * @param filename 文件名
     * @return 文件路径
     */
    public Path downloadFile(String path, String filename) {
        Path filePath = Paths.get(path, filename);
        
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }

        // 检查文件类型
        String fileExtension = getFileExtension(filename);
        if (!SUPPORTED_TYPES.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型: " + fileExtension);
        }

        return filePath;
    }

    /**
     * 创建批量下载文件
     *
     * @param filenames 文件名列表
     * @param username 用户名
     * @return 文件路径
     */
    public Path createBatchDownloadFile(List<String> filenames, String username) {
        if (filenames == null || filenames.isEmpty()) {
            throw new IllegalArgumentException("文件名列表不能为空");
        }

        if (filenames.size() > MAX_FILES) {
            throw new IllegalArgumentException("单次最多下载" + MAX_FILES + "个文件");
        }

        // 查询用户上传的文件信息
        List<FileInfo> userFiles = fileInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FileInfo>()
                        .eq("created_by", username)
                        .in("filename", filenames)
                        .eq("is_translated", 1) // 只下载已转换的文件
        );

        if (userFiles.isEmpty()) {
            throw new IllegalArgumentException("未找到可下载的文件");
        }

        // 检查是否所有请求的文件都存在
        if (userFiles.size() != filenames.size()) {
            log.warn("部分文件不存在或未转换，请求文件数: {}, 找到文件数: {}", filenames.size(), userFiles.size());
            // 记录未找到的文件
            List<String> foundFilenames = userFiles.stream()
                    .map(FileInfo::getFilename)
                    .toList();
            List<String> notFoundFiles = filenames.stream()
                    .filter(filename -> !foundFilenames.contains(filename))
                    .toList();
            log.warn("未找到的文件: {}", notFoundFiles);
        }

        try {
            if (userFiles.size() == 1) {
                // 单个文件直接返回文件路径
                FileInfo fileInfo = userFiles.get(0);
                String filePath = fileInfo.getTargetPath();
                Path path = Paths.get(filePath);
                
                if (!Files.exists(path)) {
                    throw new IllegalArgumentException("文件不存在: " + filePath);
                }
                
                return path;
            } else {
                // 多个文件压缩后返回
                String zipFilename = "batch_download_" + System.currentTimeMillis() + ".zip";
                String zipPath = fileConfig.getOutputPath() + "/" + zipFilename;
                
                createZipFile(userFiles, zipPath);
                
                Path path = Paths.get(zipPath);
                if (!Files.exists(path)) {
                    throw new RuntimeException("压缩文件创建失败: " + zipPath);
                }
                
                return path;
            }
        } catch (Exception e) {
            log.error("批量下载失败", e);
            throw new RuntimeException("批量下载失败: " + e.getMessage());
        }
    }

    /**
     * 创建压缩文件
     *
     * @param fileInfos 文件信息列表
     * @param zipPath 压缩文件路径
     * @throws IOException IO异常
     */
    private void createZipFile(List<FileInfo> fileInfos, String zipPath) throws IOException {
        File zipFile = new File(zipPath);
        
        // 确保目录存在
        zipFile.getParentFile().mkdirs();
        
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (FileInfo fileInfo : fileInfos) {
                String targetPath = fileInfo.getTargetPath();
                File targetFile = new File(targetPath);
                
                if (!targetFile.exists()) {
                    log.warn("源文件不存在: {}", targetPath);
                    continue;
                }
                
                try (FileInputStream fis = new FileInputStream(targetFile)) {
                    // 获取原文件名（不含扩展名）
                    String originalName = fileInfo.getFileRealName();
                    String nameWithoutExtension = originalName;
                    if (originalName.contains(".")) {
                        nameWithoutExtension = originalName.substring(0, originalName.lastIndexOf("."));
                    }
                    // 确保新文件名后缀为docx
                    String newFileName = "new_" + nameWithoutExtension + ".docx";
                    ZipEntry zipEntry = new ZipEntry(newFileName);
                    zipOut.putNextEntry(zipEntry);
                    
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }
                }
            }
        }
        
        log.info("压缩文件创建成功: {}", zipPath);
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 创建存储目录
     */
    private void createStorageDirectory() {
        Path storagePath = Paths.get(fileConfig.getStoragePath());
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
                log.info("创建文件存储目录: {}", storagePath.toAbsolutePath());
            } catch (IOException e) {
                log.error("创建文件存储目录失败", e);
                throw new RuntimeException("创建文件存储目录失败", e);
            }
        }
    }

    /**
     * 文件上传结果
     */
    public static class FileUploadResult {
        private String filename;
        private String fileUrl;
        private boolean success;
        private String message;

        // Getters and Setters
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

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
} 