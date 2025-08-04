package com.vibecoding.vibecoding_backend.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.vibecoding.vibecoding_backend.config.FileConfig;
import com.vibecoding.vibecoding_backend.dto.ResumeInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 简历生成器 - 使用POI-TL将Dify解析的简历数据填充到Word模板中
 * 
 * 功能特点：
 * - 支持动态数组数据处理
 * - 自动格式化各种简历信息
 * - 使用try-with-resources确保资源正确释放
 * - 完整的错误处理和日志输出
 * - 自动创建输出目录
 * - 生成唯一文件名
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeGenerator {
    
    private final FileConfig fileConfig;
    
    // 配置常量
    private static final String TEMPLATE_FILE = "template/ResumeTemplate.docx";
    
    /**
     * 生成简历Word文档
     * 
     * @param resumeData Dify解析的简历数据
     * @return 生成的文件下载URL
     * @throws IOException 文件操作异常
     */
    public String generateResume(ResumeInfoResponse resumeData) throws IOException {
        try {
            // 1. 处理数据格式化
            Map<String, Object> processedData = processResumeData(convert(resumeData));
            
            // 2. 确保输出目录存在
            ensureOutputDirectory();
            
            // 3. 生成唯一文件名
            String filename = generateUniqueFilename();
            String outputPath = fileConfig.getOutputPath() + "/" + filename;
            
            // 4. 生成Word文档
            generateWordDocument(processedData, outputPath);
            
            // 5. 构建下载URL
            String downloadUrl = fileConfig.buildDownloadUrl(fileConfig.getOutputPath(), filename);
            
            log.info("简历生成成功！输出文件：{}，下载URL：{}", outputPath, downloadUrl);
            return downloadUrl;
            
        } catch (Exception e) {
            log.error("简历生成失败：{}", e.getMessage(), e);
            throw new IOException("简历生成失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 处理简历数据，格式化为模板需要的格式
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> processResumeData(Map<String, Object> rawData) {
        Map<String, Object> processedData = new HashMap<>(rawData);
        
        // 处理技术栈列表，转换为字符串
        if (rawData.containsKey("technologies") && rawData.get("technologies") instanceof List) {
            List<String> techList = (List<String>) rawData.get("technologies");
            processedData.put("technologiesText", String.join("、", techList));
        }
        
        // 处理工作经历
        if (rawData.containsKey("experienceSummary") && rawData.get("experienceSummary") instanceof List) {
            List<Map<String, Object>> experiences = (List<Map<String, Object>>) rawData.get("experienceSummary");
            processedData.put("experienceList", experiences);
        }
        
        // 处理教育经历
        if (rawData.containsKey("education") && rawData.get("education") instanceof List) {
            List<Map<String, Object>> educations = (List<Map<String, Object>>) rawData.get("education");
            processedData.put("educationList", educations);
        }
        
        // 处理技术技能
        if (rawData.containsKey("technicalSkills") && rawData.get("technicalSkills") instanceof Map) {
            Map<String, Object> skills = (Map<String, Object>) rawData.get("technicalSkills");
            processedData.put("skillsMap", skills);
            
            // 将各个技能类别转换为字符串
            for (Map.Entry<String, Object> entry : skills.entrySet()) {
                if (entry.getValue() instanceof List) {
                    List<String> skillList = (List<String>) entry.getValue();
                    processedData.put(entry.getKey() + "Text", String.join("、", skillList));
                }
            }
        }
        
        // 处理项目经验
        if (rawData.containsKey("projectExperience") && rawData.get("projectExperience") instanceof List) {
            List<Map<String, Object>> projectExps = (List<Map<String, Object>>) rawData.get("projectExperience");
            
            // 展平项目列表
            List<Map<String, Object>> allProjects = new ArrayList<>();
            for (Map<String, Object> orgExp : projectExps) {
                if (orgExp.containsKey("projects") && orgExp.get("projects") instanceof List) {
                    List<Map<String, Object>> projects = (List<Map<String, Object>>) orgExp.get("projects");
                    for (Map<String, Object> project : projects) {
                        // 添加组织信息到每个项目
                        project.put("organization", orgExp.get("organization"));
                        allProjects.add(project);
                    }
                }
            }
            processedData.put("projectList", allProjects);
        }
        
        log.info("数据处理完成，准备生成文档");
        return processedData;
    }
    
    /**
     * 确保输出目录存在
     */
    private void ensureOutputDirectory() throws IOException {
        Path outputPath = Paths.get(fileConfig.getOutputPath());
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            log.info("创建输出目录：{}", fileConfig.getOutputPath());
        }
    }
    
    /**
     * 生成唯一文件名
     */
    private String generateUniqueFilename() {
        return "resume_" + UUID.randomUUID().toString().replace("-", "") + ".docx";
    }
    
    /**
     * 使用POI-TL生成Word文档
     */
    private void generateWordDocument(Map<String, Object> data, String outputPath) throws IOException {
        File templateFile = new File(TEMPLATE_FILE);
        
        if (!templateFile.exists()) {
            throw new FileNotFoundException("模板文件不存在：" + TEMPLATE_FILE);
        }
        
        // 配置POI-TL
        Configure config = Configure.builder()
                .buildGramer("{{", "}}")
                .build();
        
        // 使用try-with-resources确保资源正确释放
        try (FileInputStream templateStream = new FileInputStream(templateFile);
             XWPFTemplate template = XWPFTemplate.compile(templateStream, config);
             FileOutputStream out = new FileOutputStream(outputPath)) {
            
            // 渲染模板
            template.render(data);
            
            // 写入输出文件
            template.write(out);
            
            log.info("Word文档生成成功！输出路径：{}", outputPath);
            
        } catch (Exception e) {
            throw new IOException("生成Word文档时发生错误：" + e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为Map
     */
    private static Map<String, Object> convert(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }
    
} 