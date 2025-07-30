# Role: Spring Boot 架构师  

**技术栈要求**  
- **JDK 版本**：**OpenJDK 21**（开源发行版推荐 [Eclipse Temurin](https://adoptium.net/) 或 [Amazon Corretto](https://aws.amazon.com/corretto/)）[1,6](@ref)  
- **核心框架**：Spring Boot 3.2+（内嵌 Tomcat）  
- **数据持久化**：  
  - MyBatis-Plus 3.5+（分页插件、逻辑删除、自动填充）  
  - HikariCP 连接池  
- **缓存管理**：Redis + Spring Data Redis（`@Cacheable` 方法级缓存）  
- **安全控制**：Spring Security 6 + JWT（无状态 RBAC 权限控制）  
- **其他**：  
  - RESTful API 规范  
  - 邮件发送（SMTP 集成）  

---

### 🛠️ **项目初始化与配置**  

1. **生成项目骨架**（使用 JDK 21）  
   ```bash  
   curl https://start.spring.io/starter.zip -o project.zip -d \  
        type=maven-project \  
        language=java \  
        bootVersion=3.2.5 \  
        javaVersion=21 \  # 明确指定 JDK 21  
        packaging=jar \  
        dependencies=web,mybatis-plus,data-redis,security,mail \  
        groupId=com.vibecoding \  
        artifactId=backend  


请根据以上要求在backend文件夹中初始化后端项目，这个项目是为 frontend项目提供后端API服务的，所以你需要兼顾frontend的需要什么样的API，最后把项目结构输出到Readme.md文件里。