package com.vibecoding.vibecoding_backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 简历转换历史实体类
 *
 * @author VibeCode Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("resume_history")
public class ResumeHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 转换前的文件名
     */
    private String sourceName;

    /**
     * 转换前的文件类型
     */
    private String sourceType;

    /**
     * 转换前的存储位置
     */
    private String sourcePath;

    /**
     * 转换后的文件名
     */
    private String targetName;

    /**
     * 转换后的文件类型
     */
    private String targetType;

    /**
     * 转换后的文件语言
     */
    private String targetLanguage;

    /**
     * 转换时间
     */
    private LocalDateTime translateTime;

    /**
     * 转换状态（0-失败，1-成功）
     */
    private Integer translateStatus;

    /**
     * 操作人
     */
    private String createdBy;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;
}
