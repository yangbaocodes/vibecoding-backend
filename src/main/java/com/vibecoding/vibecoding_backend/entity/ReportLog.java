package com.vibecoding.vibecoding_backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报表日志实体
 *
 * @author VibeCode Team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_report_log")
public class ReportLog {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 接口名称
     */
    @TableField("interface_name")
    private String interfaceName;

    /**
     * 接口路径
     */
    @TableField("interface_path")
    private String interfacePath;

    /**
     * 请求方法
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 状态(1:成功 0:失败)
     */
    @TableField("status")
    private Integer status;

    /**
     * 响应时间(毫秒)
     */
    @TableField("response_time")
    private Long responseTime;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
} 