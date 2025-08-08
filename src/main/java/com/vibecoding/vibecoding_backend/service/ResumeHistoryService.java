package com.vibecoding.vibecoding_backend.service;

import com.vibecoding.vibecoding_backend.entity.ResumeHistory;
import com.vibecoding.vibecoding_backend.mapper.ResumeHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 简历转换历史服务类
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeHistoryService {

    private final ResumeHistoryMapper resumeHistoryMapper;

    /**
     * 记录转换历史
     *
     * @param sourceName 转换前的文件名
     * @param sourceType 转换前的文件类型
     * @param sourcePath 转换前的存储位置
     * @param targetName 转换后的文件名
     * @param targetType 转换后的文件类型
     * @param targetLanguage 转换后的文件语言
     * @param translateStatus 转换状态（0-失败，1-成功）
     * @param createdBy 操作人
     * @param errorMessage 错误信息
     */
    public void recordHistory(String sourceName, String sourceType, String sourcePath,
                            String targetName, String targetType, String targetLanguage,
                            Integer translateStatus, String createdBy, String errorMessage) {
        try {
            ResumeHistory history = new ResumeHistory();
            history.setSourceName(sourceName);
            history.setSourceType(sourceType);
            history.setSourcePath(sourcePath);
            history.setTargetName(targetName);
            history.setTargetType(targetType);
            history.setTargetLanguage(targetLanguage);
            history.setTranslateTime(LocalDateTime.now());
            history.setTranslateStatus(translateStatus);
            history.setCreatedBy(createdBy);
            history.setErrorMessage(errorMessage);

            resumeHistoryMapper.insert(history);
            log.info("简历转换历史记录已保存: sourceName={}, status={}", sourceName, translateStatus);
        } catch (Exception e) {
            log.error("保存简历转换历史记录失败", e);
            // 不抛出异常，避免影响主流程
        }
    }
}
