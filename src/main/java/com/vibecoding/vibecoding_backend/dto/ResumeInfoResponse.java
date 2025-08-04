package com.vibecoding.vibecoding_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 简历信息响应DTO
 *
 * @author VibeCode Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeInfoResponse {

    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 姓名
     */
    private String name;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 工作经验总结
     */
    private List<ExperienceSummary> experienceSummary;

    /**
     * 教育背景
     */
    private List<Education> education;

    /**
     * 工作年限
     */
    private Integer workYears;

    /**
     * 技术栈
     */
    private List<String> technologies;

    /**
     * 个人优势
     */
    private String personalAdvantage;

    /**
     * 技术技能
     */
    private TechnicalSkills technicalSkills;

    /**
     * 项目经验
     */
    private List<ProjectExperience> projectExperience;

    /**
     * 证书和培训
     */
    private List<Certification> certificationsAndTraining;

    /**
     * 专业成就
     */
    private List<Achievement> professionalAchievements;

    /**
     * 工作经验总结
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceSummary {
        private String company;
        private String position;
        private String startDate;
        private String endDate;
    }

    /**
     * 教育背景
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Education {
        private String type;
        private String school;
        private String major;
        private String startDate;
        private String endDate;
    }

    /**
     * 技术技能
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicalSkills {
        private List<String> tools;
        private List<String> languages;
        private List<String> platforms;
        private List<String> databases;
        private List<String> operatingSystems;
        private List<String> frameworks;
    }

    /**
     * 项目经验
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectExperience {
        private String organization;
        private List<Project> projects;
    }

    /**
     * 项目
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {
        private String projectTitle;
        private String client;
        private String startDate;
        private String endDate;
        private String operatingSystem;
        private String toolOrTechnology;
        private String projectDescription;
        private String projectRole;
        private String projectResponsibilities;
    }

    /**
     * 证书
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certification {
        private String name;
        private String issuer;
        private String issueDate;
    }

    /**
     * 成就
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Achievement {
        private String name;
        private String issuer;
        private String issueDate;
    }
} 