package com.vibecoding.vibecoding_backend.service;

import com.vibecoding.vibecoding_backend.entity.ReportLog;
import com.vibecoding.vibecoding_backend.mapper.ReportLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报表日志服务
 *
 * @author VibeCode Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportLogService {

    private final ReportLogMapper reportLogMapper;

    /**
     * 异步保存日志
     */
    @Async
    public void saveLogAsync(ReportLog reportLog) {
        try {
            reportLogMapper.insert(reportLog);
            log.debug("报表日志保存成功: {}", reportLog.getInterfaceName());
        } catch (Exception e) {
            log.error("保存报表日志失败", e);
        }
    }

    /**
     * 获取用户年度统计
     */
    public List<Map<String, Object>> getYearlyStatistics(Long userId) {
        return reportLogMapper.getYearlyStatistics(userId);
    }

    /**
     * 获取用户月度统计
     */
    public List<Map<String, Object>> getMonthlyStatistics(Long userId) {
        return reportLogMapper.getMonthlyStatistics(userId);
    }

    /**
     * 获取用户日度统计
     */
    public List<Map<String, Object>> getDailyStatistics(Long userId) {
        return reportLogMapper.getDailyStatistics(userId);
    }

    /**
     * 获取用户一年内每天调用接口的累计次数
     * 只返回有调用记录的日期，不返回365天的空数据
     *
     * @param userId 用户ID
     * @param year 年份，如果为null则使用当前年份
     * @return 每天调用次数统计
     */
    public List<Map<String, Object>> getYearlyDailyCallCount(Long userId, Integer year) {
        // 如果年份为空，使用当前年份
        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }
        return reportLogMapper.getYearlyDailyCallCount(userId, year);
    }
} 