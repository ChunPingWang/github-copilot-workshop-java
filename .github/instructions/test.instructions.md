---
applyTo: '**/test/**'
---

# 測試開發規範 (Testing Guidelines)

## 🎯 測試核心原則
- **可靠性**: 測試應該穩定且可重複執行
- **可讀性**: 測試程式碼應該清晰易懂
- **隔離性**: 每個測試應該獨立運行
- **快速性**: 單元測試應該快速執行
- **完整性**: 測試應該覆蓋所有重要的業務邏輯

## 📁 測試結構規範

### 測試目錄結構
```
src/
├── main/java/
└── test/java/
    ├── unit/                    # 單元測試
    │   ├── domain/              # 領域層單元測試
    │   │   ├── model/           # 領域實體測試
    │   │   ├── service/         # 領域服務測試
    │   │   └── valueobject/     # 值物件測試
    │   ├── adapter/             # 適配器層單元測試
    │   │   ├── inbound/         # 輸入適配器測試
    │   │   └── outbound/        # 輸出適配器測試
    │   └── application/         # 應用程式層單元測試
    ├── integration/             # 整合測試
    │   ├── adapter/             # 適配器整合測試
    │   │   ├── persistence/     # 持久化整合測試
    │   │   └── web/             # Web 層整合測試
    │   └── application/         # 應用服務整合測試
    └── e2e/                     # 端對端測試
        └── scenario/            # 業務場景測試
```

## 🏷️ 測試命名慣例

### 測試類別命名
- **單元測試**: `{ClassName}Test` (例: `TaskServiceTest`, `TaskTest`)
- **整合測試**: `{ClassName}IntegrationTest` (例: `TaskRepositoryIntegrationTest`)
- **端對端測試**: `{Feature}E2ETest` (例: `TaskManagementE2ETest`)

### 測試方法命名
使用 **Given-When-Then** 模式命名：
```java
// 格式: {methodName}_when_{condition}_then_{expectedResult}
@Test
void createTask_whenValidInput_thenReturnTask() { }

@Test
void findTaskById_whenTaskNotExists_thenThrowTaskNotFoundException() { }

@Test
void completeTask_whenTaskAlreadyCompleted_thenThrowBusinessRuleViolationException() { }
```

### 測試顯示名稱
使用 `@DisplayName` 提供中文描述：
```java
@Test
@DisplayName("當輸入有效的任務描述時，應該成功建立任務")
void createTask_whenValidInput_thenReturnTask() { }

@Test
@DisplayName("當任務不存在時，查詢應該拋出 TaskNotFoundException")
void findTaskById_whenTaskNotExists_thenThrowTaskNotFoundException() { }
```

## 🧪 測試品質要求

### 覆蓋率標準
- **整體程式碼覆蓋率**: 最低 80%
- **領域層覆蓋率**: 最低 90% (核心業務邏輯)
- **適配器層覆蓋率**: 最低 70%
- **應用程式層覆蓋率**: 最低 80%

### 測試結構標準
- **Given-When-Then 模式**: 所有測試必須遵循此結構
- **一個測試一個斷言**: 每個測試專注於一個特定行為
- **測試數據隔離**: 使用 Test Fixtures 或 Builder Pattern
- **Mock 使用規範**: 適當使用 Mock 避免外部依賴

## 🔧 單元測試規範

### 領域層單元測試
領域層測試應該完全獨立於外部框架：

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("任務領域服務測試")
class TaskDomainServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private ProjectRepository projectRepository;
    
    @InjectMocks
    private TaskDomainService taskDomainService;
    
    @Test
    @DisplayName("當輸入有效的建立任務命令時，應該成功建立任務")
    void execute_whenValidCreateTaskCommand_thenCreateTaskSuccessfully() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("學習六角形架構", null);
        Task expectedTask = Task.create("學習六角形架構", null);
        
        // When
        TaskId result = taskDomainService.execute(command);
        
        // Then
        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
        verifyNoMoreInteractions(taskRepository);
    }
    
    @Test
    @DisplayName("當任務描述為空時，應該拋出 IllegalArgumentException")
    void execute_whenEmptyDescription_thenThrowIllegalArgumentException() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("", null);
        
        // When & Then
        assertThatThrownBy(() -> taskDomainService.execute(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Task description cannot be empty");
        
        verifyNoInteractions(taskRepository);
    }
    
    @Test
    @DisplayName("當專案不存在時，應該拋出 ProjectNotFoundException")
    void execute_whenProjectNotExists_thenThrowProjectNotFoundException() {
        // Given
        ProjectId nonExistentProjectId = ProjectId.generate();
        CreateTaskCommand command = new CreateTaskCommand("測試任務", nonExistentProjectId);
        
        when(projectRepository.findById(nonExistentProjectId))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> taskDomainService.execute(command))
            .isInstanceOf(ProjectNotFoundException.class)
            .hasMessageContaining(nonExistentProjectId.toString());
        
        verify(projectRepository).findById(nonExistentProjectId);
        verifyNoInteractions(taskRepository);
    }
}
```

### 值物件測試
```java
@DisplayName("任務ID值物件測試")
class TaskIdTest {
    
    @Test
    @DisplayName("當使用相同UUID建立TaskId時，應該相等")
    void equals_whenSameUUID_thenEqual() {
        // Given
        UUID uuid = UUID.randomUUID();
        TaskId taskId1 = TaskId.of(uuid);
        TaskId taskId2 = TaskId.of(uuid);
        
        // When & Then
        assertThat(taskId1).isEqualTo(taskId2);
        assertThat(taskId1.hashCode()).isEqualTo(taskId2.hashCode());
    }
    
    @Test
    @DisplayName("當TaskId為null時，建立應該拋出異常")
    void of_whenNullUUID_thenThrowException() {
        // When & Then
        assertThatThrownBy(() -> TaskId.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("TaskId value cannot be null");
    }
    
    @Test
    @DisplayName("產生的TaskId應該不為null且包含有效UUID")
    void generate_whenCalled_thenReturnValidTaskId() {
        // When
        TaskId taskId = TaskId.generate();
        
        // Then
        assertThat(taskId).isNotNull();
        assertThat(taskId.getValue()).isNotNull();
        assertThat(taskId.getValue()).isInstanceOf(UUID.class);
    }
}
```

### 適配器層單元測試
```java
@WebMvcTest(TaskController.class)
@DisplayName("任務控制器測試")
class TaskControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CreateTaskUseCase createTaskUseCase;
    
    @MockBean
    private FindTaskUseCase findTaskUseCase;
    
    @MockBean
    private TaskWebMapper mapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("當提供有效的建立任務請求時，應該回傳201狀態碼")
    void createTask_whenValidRequest_thenReturnCreated() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest("測試任務", null);
        CreateTaskCommand command = new CreateTaskCommand("測試任務", null);
        TaskId taskId = TaskId.generate();
        Task task = Task.create("測試任務", null);
        TaskResponse response = new TaskResponse(
            taskId.getValue(), "測試任務", "TODO", null, LocalDateTime.now()
        );
        
        when(mapper.toCommand(any(CreateTaskRequest.class))).thenReturn(command);
        when(createTaskUseCase.execute(command)).thenReturn(taskId);
        when(findTaskUseCase.findById(taskId)).thenReturn(Optional.of(task));
        when(mapper.toResponse(task)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.getValue().toString()))
                .andExpect(jsonPath("$.description").value("測試任務"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }
    
    @Test
    @DisplayName("當請求驗證失敗時，應該回傳400狀態碼")
    void createTask_whenInvalidRequest_thenReturnBadRequest() throws Exception {
        // Given
        CreateTaskRequest invalidRequest = new CreateTaskRequest("", null);
        
        // When & Then
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
```

## 🔗 整合測試規範

### 持久化整合測試
```java
@DataJpaTest
@DisplayName("任務JPA適配器整合測試")
class TaskJpaAdapterIntegrationTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private TaskJpaRepository jpaRepository;
    
    private TaskJpaAdapter taskJpaAdapter;
    private TaskPersistenceMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new TaskPersistenceMapper();
        taskJpaAdapter = new TaskJpaAdapter(jpaRepository, mapper);
    }
    
    @Test
    @DisplayName("當儲存有效任務時，應該成功持久化到資料庫")
    void save_whenValidTask_thenPersistToDatabase() {
        // Given
        Task task = Task.create("整合測試任務", null);
        
        // When
        taskJpaAdapter.save(task);
        entityManager.flush();
        
        // Then
        Optional<TaskEntity> savedEntity = jpaRepository.findById(task.getId().getValue());
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getDescription()).isEqualTo("整合測試任務");
        assertThat(savedEntity.get().getStatus()).isEqualTo(TaskStatusEntity.TODO);
    }
    
    @Test
    @DisplayName("當根據ID查詢存在的任務時，應該回傳對應的領域物件")
    void findById_whenTaskExists_thenReturnDomainObject() {
        // Given
        TaskEntity entity = new TaskEntity(
            UUID.randomUUID(), 
            "測試任務", 
            TaskStatusEntity.TODO, 
            null, 
            LocalDateTime.now()
        );
        entityManager.persistAndFlush(entity);
        
        // When
        Optional<Task> result = taskJpaAdapter.findById(TaskId.of(entity.getId()));
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("測試任務");
        assertThat(result.get().getStatus()).isEqualTo(TaskStatus.TODO);
    }
}
```

### 應用服務整合測試
```java
@SpringBootTest
@Transactional
@DisplayName("任務應用服務整合測試")
class TaskApplicationServiceIntegrationTest {
    
    @Autowired
    private TaskApplicationService taskApplicationService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Test
    @DisplayName("當建立任務並發送通知時，應該成功完成整個流程")
    void createTaskWithNotification_whenValidCommand_thenCompleteWorkflow() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("整合測試任務", null);
        
        // When
        TaskSummaryDto result = taskApplicationService.createTaskWithNotification(command);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("整合測試任務");
        
        // 驗證任務已儲存
        Optional<Task> savedTask = taskRepository.findById(TaskId.of(result.getId()));
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getDescription()).isEqualTo("整合測試任務");
    }
}
```

## 🎭 端對端測試規範

### 業務場景測試
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("任務管理端對端測試")
class TaskManagementE2ETest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    private int port;
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Test
    @DisplayName("完整的任務生命週期：建立 -> 查詢 -> 完成")
    void taskLifecycle_createFindComplete_shouldWorkEndToEnd() {
        String baseUrl = "http://localhost:" + port + "/api/tasks";
        
        // Given - 建立任務請求
        CreateTaskRequest createRequest = new CreateTaskRequest("E2E測試任務", null);
        
        // When - 建立任務
        ResponseEntity<TaskResponse> createResponse = restTemplate.postForEntity(
            baseUrl, createRequest, TaskResponse.class
        );
        
        // Then - 驗證建立成功
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        UUID taskId = createResponse.getBody().getId();
        
        // When - 查詢任務
        ResponseEntity<TaskResponse> getResponse = restTemplate.getForEntity(
            baseUrl + "/" + taskId, TaskResponse.class
        );
        
        // Then - 驗證查詢成功
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getDescription()).isEqualTo("E2E測試任務");
        assertThat(getResponse.getBody().getStatus()).isEqualTo("TODO");
        
        // When - 完成任務
        ResponseEntity<TaskResponse> completeResponse = restTemplate.exchange(
            baseUrl + "/" + taskId + "/complete",
            HttpMethod.PUT,
            null,
            TaskResponse.class
        );
        
        // Then - 驗證完成成功
        assertThat(completeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(completeResponse.getBody().getStatus()).isEqualTo("DONE");
    }
}
```

## 🛠️ 測試工具和最佳實務

### Test Fixtures 和 Builder Pattern
```java
public class TaskTestDataBuilder {
    private String description = "預設任務描述";
    private TaskStatus status = TaskStatus.TODO;
    private ProjectId projectId = null;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public static TaskTestDataBuilder aTask() {
        return new TaskTestDataBuilder();
    }
    
    public TaskTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public TaskTestDataBuilder withStatus(TaskStatus status) {
        this.status = status;
        return this;
    }
    
    public TaskTestDataBuilder withProjectId(ProjectId projectId) {
        this.projectId = projectId;
        return this;
    }
    
    public TaskTestDataBuilder completed() {
        this.status = TaskStatus.DONE;
        return this;
    }
    
    public Task build() {
        return Task.restore(
            TaskId.generate(),
            description,
            status,
            projectId,
            createdAt
        );
    }
}

// 使用範例
@Test
void findCompletedTasks_whenMultipleTasksExist_thenReturnOnlyCompleted() {
    // Given
    Task todoTask = TaskTestDataBuilder.aTask()
        .withDescription("待辦任務")
        .build();
    
    Task completedTask = TaskTestDataBuilder.aTask()
        .withDescription("已完成任務")
        .completed()
        .build();
    
    // When & Then
    // 測試邏輯...
}
```

### 自訂斷言
```java
public class TaskAssertions {
    
    public static TaskAssert assertThat(Task actual) {
        return new TaskAssert(actual);
    }
    
    public static class TaskAssert extends AbstractAssert<TaskAssert, Task> {
        
        public TaskAssert(Task actual) {
            super(actual, TaskAssert.class);
        }
        
        public TaskAssert hasDescription(String expectedDescription) {
            isNotNull();
            if (!Objects.equals(actual.getDescription(), expectedDescription)) {
                failWithMessage("Expected task description to be <%s> but was <%s>", 
                    expectedDescription, actual.getDescription());
            }
            return this;
        }
        
        public TaskAssert isCompleted() {
            isNotNull();
            if (actual.getStatus() != TaskStatus.DONE) {
                failWithMessage("Expected task to be completed but was <%s>", actual.getStatus());
            }
            return this;
        }
        
        public TaskAssert isPending() {
            isNotNull();
            if (actual.getStatus() == TaskStatus.DONE) {
                failWithMessage("Expected task to be pending but was completed");
            }
            return this;
        }
    }
}

// 使用範例
@Test
void completeTask_whenValidTask_thenTaskShouldBeCompleted() {
    // Given
    Task task = TaskTestDataBuilder.aTask().build();
    
    // When
    Task completedTask = task.complete();
    
    // Then
    TaskAssertions.assertThat(completedTask)
        .isCompleted()
        .hasDescription("預設任務描述");
}
```

## 🚀 測試執行策略

### 測試分層執行
```gradle
// build.gradle 測試配置
test {
    useJUnitPlatform()
    
    // 測試分組
    systemProperty 'junit.jupiter.testgroup.unit', 'unit'
    systemProperty 'junit.jupiter.testgroup.integration', 'integration'
    systemProperty 'junit.jupiter.testgroup.e2e', 'e2e'
    
    // 並行執行
    maxParallelForks = Runtime.runtime.availableProcessors().intdiv(2) ?: 1
    
    // 記憶體設定
    minHeapSize = "512m"
    maxHeapSize = "2048m"
}

// 單元測試任務
task unitTest(type: Test) {
    useJUnitPlatform {
        includeTags 'unit'
    }
}

// 整合測試任務
task integrationTest(type: Test) {
    useJUnitPlatform {
        includeTags 'integration'
    }
}

// 端對端測試任務
task e2eTest(type: Test) {
    useJUnitPlatform {
        includeTags 'e2e'
    }
}
```

### 測試標籤和分組
```java
// 測試標籤使用
@Tag("unit")
@Tag("domain")
class TaskDomainServiceTest { }

@Tag("integration")
@Tag("persistence")
class TaskJpaAdapterIntegrationTest { }

@Tag("e2e")
@Tag("web")
class TaskManagementE2ETest { }
```

## ✅ 測試檢查清單

### 測試撰寫檢查清單
- [ ] **命名規範**: 遵循 Given-When-Then 命名慣例
- [ ] **測試結構**: 使用 Given-When-Then 結構
- [ ] **單一職責**: 每個測試專注於一個行為
- [ ] **獨立性**: 測試之間互不依賴
- [ ] **可讀性**: 測試程式碼清晰易懂
- [ ] **斷言明確**: 使用具體的斷言訊息
- [ ] **Mock 適當**: 適當使用 Mock 避免外部依賴
- [ ] **測試數據**: 使用 Builder Pattern 或 Fixtures

### 測試覆蓋率檢查清單
- [ ] **業務邏輯**: 所有重要業務邏輯都有測試覆蓋
- [ ] **邊界條件**: 測試邊界值和異常情況
- [ ] **錯誤處理**: 測試異常處理路徑
- [ ] **整合點**: 測試系統整合點
- [ ] **API 合約**: 測試 API 輸入輸出合約

### 測試維護檢查清單
- [ ] **測試更新**: 程式碼變更時同步更新測試
- [ ] **重構安全**: 重構時確保測試仍然有效
- [ ] **性能檢查**: 測試執行時間合理
- [ ] **持續集成**: 測試在 CI/CD 管道中正常運行
- [ ] **測試文檔**: 複雜測試場景有適當文檔說明

## 📊 測試報告和監控

### 覆蓋率報告配置
```gradle
// Jacoco 覆蓋率配置
jacoco {
    toolVersion = "0.8.8"
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        csv.required = false
        html.required = true
        html.outputLocation = layout.buildDirectory.dir('reports/jacoco')
    }
    
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/*Application*',
                '**/*Config*',
                '**/entity/**',
                '**/dto/**'
            ])
        }))
    }
}

jacocoTestCoverageVerification {
    dependsOn jacocoTestReport
    violationRules {
        rule {
            limit {
                minimum = 0.80
            }
        }
        rule {
            element = 'CLASS'
            includes = ['com.codurance.training.tasks.domain.*']
            limit {
                minimum = 0.90
            }
        }
    }
}
```

---

**記住**: 好的測試不僅驗證程式碼的正確性，更是活的文檔，描述系統的預期行為。測試應該簡潔、明確、可維護。
