package com.vibecoding.vibecoding_backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 报表日志装饰器注解
 * 用于标记需要记录报表日志的接口
 *
 * @author VibeCode Team
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportLog {

    /**
     * 接口名称
     */
    String value() default "";

    /**
     * 接口描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean logRequest() default false;

    /**
     * 是否记录响应结果
     */
    boolean logResponse() default false;
} 