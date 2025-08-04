package com.vibecoding.vibecoding_backend.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件信息实体类
 *
 * @author VibeCode Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_file_info")
public class FileInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名（UUID）
     */
    private String filename;

    /**
     * 真实文件名
     */
    private String fileRealName;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 目标路径
     */
    private String targetPath;

    /**
     * 是否已转换（0-否，1-是）
     */
    private Integer isTranslated;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 删除标记（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;
} 