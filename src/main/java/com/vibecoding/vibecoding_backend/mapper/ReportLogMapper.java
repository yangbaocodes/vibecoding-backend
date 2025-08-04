package com.vibecoding.vibecoding_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vibecoding.vibecoding_backend.entity.ReportLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 报表日志Mapper
 *
 * @author VibeCode Team
 */
@Mapper
public interface ReportLogMapper extends BaseMapper<ReportLog> {

    /**
     * 获取用户接口调用统计（按年）
     */
    @Select("SELECT " +
            "interface_name as interfaceName, " +
            "YEAR(created_time) as year, " +
            "COUNT(*) as callCount, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as successCount, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as failCount, " +
            "AVG(response_time) as avgResponseTime " +
            "FROM sys_report_log " +
            "WHERE user_id = #{userId} " +
            "GROUP BY interface_name, YEAR(created_time) " +
            "ORDER BY year DESC, callCount DESC")
    List<Map<String, Object>> getYearlyStatistics(@Param("userId") Long userId);

    /**
     * 获取用户接口调用统计（按月）
     */
    @Select("SELECT " +
            "interface_name as interfaceName, " +
            "YEAR(created_time) as year, " +
            "MONTH(created_time) as month, " +
            "DATE_FORMAT(created_time, '%Y-%m') as yearMonth, " +
            "COUNT(*) as callCount, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as successCount, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as failCount, " +
            "AVG(response_time) as avgResponseTime " +
            "FROM sys_report_log " +
            "WHERE user_id = #{userId} " +
            "GROUP BY interface_name, YEAR(created_time), MONTH(created_time) " +
            "ORDER BY year DESC, month DESC, callCount DESC")
    List<Map<String, Object>> getMonthlyStatistics(@Param("userId") Long userId);

    /**
     * 获取用户接口调用统计（按日）
     */
    @Select("SELECT " +
            "interface_name as interfaceName, " +
            "YEAR(created_time) as year, " +
            "MONTH(created_time) as month, " +
            "DAY(created_time) as day, " +
            "DATE(created_time) as date, " +
            "COUNT(*) as callCount, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as successCount, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as failCount, " +
            "AVG(response_time) as avgResponseTime " +
            "FROM sys_report_log " +
            "WHERE user_id = #{userId} " +
            "GROUP BY interface_name, YEAR(created_time), MONTH(created_time), DAY(created_time) " +
            "ORDER BY year DESC, month DESC, day DESC, callCount DESC")
    List<Map<String, Object>> getDailyStatistics(@Param("userId") Long userId);

    /**
     * 获取用户一年内每天调用接口的累计次数
     * 只返回有调用记录的日期，不返回365天的空数据
     */
    @Select("SELECT " +
            "DATE(created_time) as callDate, " +
            "COUNT(*) as totalCallCount, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as successCount, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as failCount, " +
            "AVG(response_time) as avgResponseTime " +
            "FROM sys_report_log " +
            "WHERE user_id = #{userId} " +
            "AND YEAR(created_time) = #{year} " +
            "GROUP BY DATE(created_time) " +
            "ORDER BY callDate DESC")
    List<Map<String, Object>> getYearlyDailyCallCount(@Param("userId") Long userId, @Param("year") Integer year);
} 