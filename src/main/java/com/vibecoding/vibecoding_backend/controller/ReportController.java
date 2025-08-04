package com.vibecoding.vibecoding_backend.controller;

import com.vibecoding.vibecoding_backend.common.Result;
import com.vibecoding.vibecoding_backend.service.ReportLogService;
import com.vibecoding.vibecoding_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表控制器
 *
 * @author VibeCode Team
 */
@Slf4j
@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportLogService reportLogService;
    private final UserService userService;

    /**
     * 获取用户报表统计
     *
     * @param authentication 认证信息
     * @return 报表统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return Result.error(401, "用户未认证");
            }

            String email = authentication.getName();
            Long userId = userService.getUserIdByEmail(email);

            if (userId == null) {
                return Result.error(404, "用户不存在");
            }

            // 获取统计数据
            List<Map<String, Object>> yearlyStats = reportLogService.getYearlyStatistics(userId);
            List<Map<String, Object>> monthlyStats = reportLogService.getMonthlyStatistics(userId);
            List<Map<String, Object>> dailyStats = reportLogService.getDailyStatistics(userId);

            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("userEmail", email);
            result.put("userId", userId);
            result.put("yearlyStatistics", yearlyStats);
            result.put("monthlyStatistics", monthlyStats);
            result.put("dailyStatistics", dailyStats);

            log.info("用户 {} 获取报表统计成功", email);
            return Result.success("获取报表统计成功", result);

        } catch (Exception e) {
            log.error("获取报表统计失败", e);
            return Result.error(500, "获取报表统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户一年内每天调用接口的累计次数
     * 只返回有调用记录的日期，不返回365天的空数据
     *
     * @param authentication 认证信息
     * @param year 年份参数（可选），默认当前年份
     * @return 每天调用次数统计
     */
    @GetMapping("/yearly-daily-calls")
    public Result<Map<String, Object>> getYearlyDailyCallCount(
            Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer year) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return Result.error(401, "用户未认证");
            }

            String email = authentication.getName();
            Long userId = userService.getUserIdByEmail(email);

            if (userId == null) {
                return Result.error(404, "用户不存在");
            }

            // 如果年份为空，使用当前年份
            if (year == null) {
                year = java.time.LocalDate.now().getYear();
            }

            // 获取指定年份每天调用次数统计
            List<Map<String, Object>> dailyCallCount = reportLogService.getYearlyDailyCallCount(userId, year);

            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("userEmail", email);
            result.put("userId", userId);
            result.put("year", year);
            result.put("totalDays", dailyCallCount.size());
            result.put("dailyCallCount", dailyCallCount);

            log.info("用户 {} 获取 {} 年每天调用次数统计成功，共 {} 天有调用记录", email, year, dailyCallCount.size());
            return Result.success("获取" + year + "年每天调用次数统计成功", result);

        } catch (Exception e) {
            log.error("获取每年调用次数统计失败", e);
            return Result.error(500, "获取每年调用次数统计失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("报表服务正常");
    }
} 