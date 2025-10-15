# GitHub Copilot 開發規範

本專案使用 GitHub Copilot 進行開發輔助，請遵循以下開發規範以確保程式碼品質和一致性。

## 🛠️ 技術堆疊

### 開發環境
- **Java 版本**: JDK 17
- **套件管理**: Gradle 8.x
- **測試框架**: JUnit 5, Mockito
- **程式碼風格**: Google Java Style Guide

## 📝 程式碼規範

### 命名慣例
- **類別名稱**: PascalCase (例: `TaskService`, `TaskRepository`)
- **方法名稱**: camelCase (例: `createTask`, `findTaskById`)
- **變數名稱**: camelCase (例: `taskList`, `userId`)
- **常數名稱**: SCREAMING_SNAKE_CASE (例: `MAX_TASKS_PER_PROJECT`)
- **套件名稱**: 全小寫，使用點號分隔 (例: `com.codurance.training.tasks`)

### 類別設計原則
- 優先使用不可變物件 (`final` 欄位)
- 實作適當的 `equals()`, `hashCode()`, 和 `toString()` 方法
- 使用建構子注入而非欄位注入
- 遵循單一職責原則 (SRP)

### 方法設計
- 方法名稱應明確表達其功能
- 方法參數數量建議不超過 3 個
- 使用 Optional 處理可能為 null 的回傳值
- 公開方法必須包含 JavaDoc 註解

## 🧪 測試規範

### 測試結構
```
src/
├── main/java/
└── test/java/
    ├── unit/        # 單元測試
    ├── integration/ # 整合測試
    └── e2e/         # 端對端測試
```

### 測試命名慣例
- **測試類別**: `{ClassName}Test` (例: `TaskServiceTest`)
- **測試方法**: `{methodName}_when_{condition}_then_{expectedResult}`
  ```java
  @Test
  void createTask_whenValidInput_thenReturnTask() { }
  
  @Test
  void findTaskById_whenTaskNotExists_thenThrowException() { }
  ```

### 測試品質要求
- **程式碼覆蓋率**: 最低 80%
- **測試架構**: Given-When-Then 模式
- **Mock 使用**: 使用 Mockito 進行相依性模擬
- **測試資料**: 使用 Test Fixtures 或 Builder Pattern

### 測試範例
```java
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskService taskService;
    
    @Test
    void createTask_whenValidInput_thenReturnTask() {
        // Given
        CreateTaskRequest request = new CreateTaskRequest("學習 Java");
        Task expectedTask = new Task(1L, "學習 Java", false);
        when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);
        
        // When
        Task actualTask = taskService.createTask(request);
        
        // Then
        assertThat(actualTask).isNotNull();
        assertThat(actualTask.getDescription()).isEqualTo("學習 Java");
        assertThat(actualTask.isDone()).isFalse();
    }
}
```

## 🏗️ 架構模式

### 套件結構
```
com.codurance.training.tasks/
├── controller/     # REST API 控制器
├── service/        # 業務邏輯服務
├── repository/     # 資料存取層
├── model/          # 實體模型
├── dto/            # 資料傳輸物件
├── config/         # 配置類別
└── exception/      # 自訂例外
```

### 依賴注入
- 使用 Constructor Injection
- 避免使用 `@Autowired` 標註在欄位上
- 介面與實作分離

### 例外處理
- 使用自訂例外類別
- 實作全域例外處理器
- 提供有意義的錯誤訊息

## 📦 Gradle 配置規範

### 依賴管理
```gradle
dependencies {
    // Spring Boot
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    
    // 測試
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.junit.jupiter:junit-jupiter'
    testImplementation 'org.mockito:mockito-core'
    testImplementation 'org.mockito:mockito-junit-jupiter'
    
    // 工具
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

### 插件配置
```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.1.x'
    id 'io.spring.dependency-management' version '1.1.x'
    id 'jacoco'
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

## 🤖 GitHub Copilot 使用指南

### 有效提示 (Prompts)
- **生成測試**: `Generate unit tests for this class using JUnit 5 and Mockito`
- **重構程式碼**: `Refactor this method to follow single responsibility principle`
- **產生 JavaDoc**: `Add comprehensive JavaDoc comments to this method`
- **建立 Builder**: `Create a builder pattern for this class`

### 程式碼生成最佳實務
1. **提供上下文**: 在註解中描述意圖和需求
2. **使用範例**: 提供期望的程式碼樣式範例
3. **段落式開發**: 一次專注於一個小功能
4. **驗證生成結果**: 檢查 Copilot 生成的程式碼是否符合規範

### 避免的做法
- 不要盲目接受所有 Copilot 建議
- 避免生成過於複雜的單一方法
- 不要跳過程式碼審查步驟
- 避免忽略測試程式碼的品質

## 📋 程式碼檢查清單

### 提交前檢查
- [ ] 程式碼通過所有測試
- [ ] 測試覆蓋率達到 80% 以上
- [ ] 無編譯警告或錯誤
- [ ] 遵循命名慣例
- [ ] 包含適當的 JavaDoc 註解
- [ ] 無重複程式碼 (DRY 原則)
- [ ] 遵循 SOLID 原則

### 程式碼審查重點
- 商業邏輯正確性
- 錯誤處理完整性
- 效能考量
- 安全性檢查
- 可讀性和維護性

## 📚 參考資源

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)

---

**記住**: 程式碼是給人讀的，偶爾給機器執行。保持程式碼清晰、簡潔、可維護是我們的首要目標。