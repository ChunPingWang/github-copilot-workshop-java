---
applyTo: '**/configuration/**'
---

# 配置層 (Configuration Layer) 開發規範

## 🎯 配置層核心原則
- **依賴注入組裝**: 組裝和配置所有元件的依賴關係
- **框架整合**: 整合 Spring Boot 等框架配置
- **環境配置**: 管理不同環境的配置差異
- **Bean 生命週期**: 管理應用程式元件的生命週期

## 📁 配置層套件結構
```
config/
├── ApplicationConfig.java        # 主要應用程式配置
├── DatabaseConfig.java          # 資料庫配置
├── SecurityConfig.java          # 安全性配置
├── WebConfig.java               # Web 層配置
├── PersistenceConfig.java       # JPA/持久化配置
└── properties/                  # 配置屬性類別
    ├── DatabaseProperties.java
    └── ApplicationProperties.java
```

## 🔧 應用程式配置設計規範

### 主要配置類別
- **清晰的依賴關係**: 明確定義各層之間的依賴關係
- **介面導向**: 優先注入介面而非實作類別
- **條件配置**: 使用 @ConditionalOnProperty 等條件註解
- **設定檔分離**: 將配置邏輯與業務邏輯分離

#### ApplicationConfig 範例
```java
@Configuration
@EnableConfigurationProperties({ApplicationProperties.class, DatabaseProperties.class})
public class ApplicationConfig {
    
    // Domain Services (Use Case implementations)
    @Bean
    public CreateTaskUseCase createTaskUseCase(TaskRepository taskRepository, 
                                             ProjectRepository projectRepository) {
        return new TaskDomainService(taskRepository, projectRepository);
    }
    
    @Bean
    public FindTaskUseCase findTaskUseCase(TaskRepository taskRepository) {
        return new TaskDomainService(taskRepository, null);
    }
    
    @Bean
    public CompleteTaskUseCase completeTaskUseCase(TaskRepository taskRepository) {
        return new TaskDomainService(taskRepository, null);
    }
    
    // Mappers
    @Bean
    public TaskWebMapper taskWebMapper() {
        return new TaskWebMapper();
    }
    
    @Bean
    public TaskPersistenceMapper taskPersistenceMapper() {
        return new TaskPersistenceMapper();
    }
    
    // CLI Components (conditional)
    @Bean
    @ConditionalOnProperty(name = "app.cli.enabled", havingValue = "true", matchIfMissing = true)
    public TaskListCLI taskListCLI(CreateTaskUseCase createTaskUseCase, 
                                  FindTaskUseCase findTaskUseCase) {
        return new TaskListCLI(createTaskUseCase, findTaskUseCase);
    }
}
```

### 資料庫配置
```java
@Configuration
@EnableJpaRepositories(basePackages = "com.codurance.training.tasks.adapter.outbound.persistence.repository")
@EntityScan(basePackages = "com.codurance.training.tasks.adapter.outbound.persistence.entity")
public class DatabaseConfig {
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource dataSource(DatabaseProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setDriverClassName(properties.getDriverClassName());
        
        // Connection pool settings
        config.setMaximumPoolSize(properties.getMaxPoolSize());
        config.setMinimumIdle(properties.getMinIdle());
        config.setConnectionTimeout(properties.getConnectionTimeout());
        config.setIdleTimeout(properties.getIdleTimeout());
        
        return new HikariDataSource(config);
    }
    
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.codurance.training.tasks.adapter.outbound.persistence.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);
        factory.setJpaVendorAdapter(vendorAdapter);
        
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        jpaProperties.put("hibernate.format_sql", true);
        factory.setJpaProperties(jpaProperties);
        
        return factory;
    }
}
```

### Web 配置
```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
```

## 📋 配置屬性管理

### 配置屬性類別設計
- **型別安全**: 使用強型別配置類別
- **驗證**: 加入適當的驗證註解
- **文檔**: 提供清楚的屬性說明
- **預設值**: 設定合理的預設值

#### ApplicationProperties 範例
```java
@ConfigurationProperties(prefix = "app")
@Validated
public class ApplicationProperties {
    
    /**
     * 應用程式名稱
     */
    @NotBlank
    private String name = "Task Management System";
    
    /**
     * 應用程式版本
     */
    @NotBlank
    private String version = "1.0.0";
    
    /**
     * CLI 介面配置
     */
    private Cli cli = new Cli();
    
    /**
     * 通知配置
     */
    private Notification notification = new Notification();
    
    public static class Cli {
        /**
         * 是否啟用 CLI 介面
         */
        private boolean enabled = true;
        
        /**
         * CLI 提示符號
         */
        private String prompt = "task> ";
        
        // Getters and setters
    }
    
    public static class Notification {
        /**
         * 是否啟用通知功能
         */
        private boolean enabled = false;
        
        /**
         * 電子郵件通知配置
         */
        private Email email = new Email();
        
        public static class Email {
            /**
             * SMTP 伺服器主機
             */
            private String host = "localhost";
            
            /**
             * SMTP 伺服器連接埠
             */
            @Min(1)
            @Max(65535)
            private int port = 587;
            
            /**
             * 發送者電子郵件地址
             */
            @Email
            private String from = "noreply@example.com";
            
            // Getters and setters
        }
        
        // Getters and setters
    }
    
    // Getters and setters
}
```

#### DatabaseProperties 範例
```java
@ConfigurationProperties(prefix = "app.database")
@Validated
public class DatabaseProperties {
    
    /**
     * 資料庫連線 URL
     */
    @NotBlank
    private String url = "jdbc:h2:mem:testdb";
    
    /**
     * 資料庫使用者名稱
     */
    private String username = "sa";
    
    /**
     * 資料庫密碼
     */
    private String password = "";
    
    /**
     * JDBC 驅動程式類別名稱
     */
    @NotBlank
    private String driverClassName = "org.h2.Driver";
    
    /**
     * 連線池最大大小
     */
    @Min(1)
    @Max(100)
    private int maxPoolSize = 10;
    
    /**
     * 連線池最小閒置連線數
     */
    @Min(0)
    private int minIdle = 2;
    
    /**
     * 連線逾時時間 (毫秒)
     */
    @Min(1000)
    private long connectionTimeout = 30000;
    
    /**
     * 閒置連線逾時時間 (毫秒)
     */
    @Min(60000)
    private long idleTimeout = 600000;
    
    // Getters and setters
}
```

## 🔐 安全性配置 (選用)
```java
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "true")
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tasks/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/tasks/**").authenticated()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().sameOrigin())
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## 🧪 測試配置

### 測試專用配置
```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public TaskRepository taskRepository() {
        return new InMemoryTaskRepository();
    }
    
    @Bean
    @Primary
    public ProjectRepository projectRepository() {
        return new InMemoryProjectRepository();
    }
    
    @Bean
    @Primary
    public NotificationService notificationService() {
        return Mockito.mock(NotificationService.class);
    }
}
```

### 測試屬性配置
```yaml
# application-test.yml
app:
  name: "Task Management System - Test"
  cli:
    enabled: false
  notification:
    enabled: false
  database:
    url: "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    username: "sa"
    password: ""
    max-pool-size: 5

spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true
```

## 🏗️ Profile 配置管理

### 開發環境配置
```yaml
# application-dev.yml
app:
  database:
    url: "jdbc:h2:file:./data/devdb"
    max-pool-size: 5

spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true

logging:
  level:
    com.codurance.training.tasks: DEBUG
    org.springframework.web: DEBUG
```

### 生產環境配置
```yaml
# application-prod.yml
app:
  database:
    url: "${DATABASE_URL:jdbc:postgresql://localhost:5432/taskdb}"
    username: "${DATABASE_USERNAME:taskuser}"
    password: "${DATABASE_PASSWORD:taskpass}"
    driver-class-name: "org.postgresql.Driver"
    max-pool-size: 20
  notification:
    enabled: true
    email:
      host: "${SMTP_HOST:smtp.gmail.com}"
      port: 587
      from: "${EMAIL_FROM:noreply@company.com}"

spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

logging:
  level:
    com.codurance.training.tasks: INFO
  file:
    name: "logs/application.log"
```

## ✅ 配置層檢查清單
- [ ] **依賴注入**: 正確配置所有元件的依賴關係
- [ ] **介面導向**: 優先注入介面而非實作類別
- [ ] **環境分離**: 適當的環境配置分離
- [ ] **屬性驗證**: 配置屬性包含適當的驗證
- [ ] **預設值**: 設定合理的預設值
- [ ] **條件配置**: 使用條件註解避免不必要的 Bean 建立
- [ ] **生命週期管理**: 適當的 Bean 生命週期管理
- [ ] **安全性**: 敏感資訊使用環境變數或加密存儲