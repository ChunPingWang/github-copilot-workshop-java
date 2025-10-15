---
applyTo: '**/adapter/**'
---

# 適配器層 (Adapter Layer) 開發規範

## 🎯 適配器層核心原則
- **技術細節封裝**: 負責技術細節和外部系統整合
- **協議轉換**: 處理資料格式轉換 (DTO ↔ Domain)
- **依賴注入配置**: 實作依賴注入和組裝
- **框架整合**: 與 Spring Boot、JPA 等框架整合

## 📁 適配器層套件結構
```
adapter/
├── inbound/               # 輸入適配器
│   ├── web/               # Web 適配器 (REST API)
│   │   ├── TaskController.java
│   │   ├── ProjectController.java
│   │   ├── dto/           # Web DTOs
│   │   │   ├── CreateTaskRequest.java
│   │   │   ├── TaskResponse.java
│   │   │   └── ProjectResponse.java
│   │   └── mapper/        # DTO 轉換器
│   │       ├── TaskWebMapper.java
│   │       └── ProjectWebMapper.java
│   └── cli/               # 命令列適配器
│       └── TaskListCLI.java
└── outbound/              # 輸出適配器
    ├── persistence/       # 持久化適配器
    │   ├── TaskJpaAdapter.java
    │   ├── ProjectJpaAdapter.java
    │   ├── entity/        # JPA 實體
    │   │   ├── TaskEntity.java
    │   │   └── ProjectEntity.java
    │   ├── repository/    # JPA Repository
    │   │   ├── TaskJpaRepository.java
    │   │   └── ProjectJpaRepository.java
    │   └── mapper/        # 實體轉換器
    │       ├── TaskPersistenceMapper.java
    │       └── ProjectPersistenceMapper.java
    └── notification/      # 通知適配器
        └── EmailNotificationAdapter.java
```

## 🌐 輸入適配器 (Inbound Adapters) 設計規範

### Web 適配器 (REST API)
- **RESTful 設計**: 遵循 REST 原則設計 API
- **DTO 轉換**: 使用專用的 Request/Response DTO
- **錯誤處理**: 統一的錯誤處理和狀態碼
- **驗證**: 在適配器層進行輸入驗證

#### REST Controller 範例
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final FindTaskUseCase findTaskUseCase;
    private final TaskWebMapper mapper;
    
    public TaskController(CreateTaskUseCase createTaskUseCase,
                         FindTaskUseCase findTaskUseCase,
                         TaskWebMapper mapper) {
        this.createTaskUseCase = createTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
        this.mapper = mapper;
    }
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        CreateTaskCommand command = mapper.toCommand(request);
        TaskId taskId = createTaskUseCase.execute(command);
        
        Task task = findTaskUseCase.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        TaskResponse response = mapper.toResponse(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        TaskId taskId = TaskId.of(id);
        Task task = findTaskUseCase.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        TaskResponse response = mapper.toResponse(task);
        return ResponseEntity.ok(response);
    }
}
```

#### DTO 設計範例
```java
// Request DTO
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTaskRequest {
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
    
    private UUID projectId;
    
    // Constructors, getters, setters
    public CreateTaskRequest() {}
    
    public CreateTaskRequest(String description, UUID projectId) {
        this.description = description;
        this.projectId = projectId;
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
}

// Response DTO
public class TaskResponse {
    private UUID id;
    private String description;
    private String status;
    private UUID projectId;
    private LocalDateTime createdAt;
    
    // Constructors, getters, setters
    public TaskResponse() {}
    
    public TaskResponse(UUID id, String description, String status, 
                       UUID projectId, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }
    
    // Getters and setters...
}
```

#### Web Mapper 範例
```java
@Component
public class TaskWebMapper {
    
    public CreateTaskCommand toCommand(CreateTaskRequest request) {
        ProjectId projectId = request.getProjectId() != null 
            ? ProjectId.of(request.getProjectId()) 
            : null;
        return new CreateTaskCommand(request.getDescription(), projectId);
    }
    
    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
            task.getId().getValue(),
            task.getDescription(),
            task.getStatus().name(),
            task.getProjectId() != null ? task.getProjectId().getValue() : null,
            task.getCreatedAt()
        );
    }
}
```

### CLI 適配器
- **命令解析**: 解析命令列參數
- **輸出格式化**: 格式化輸出結果
- **錯誤處理**: 適當的錯誤訊息顯示

```java
@Component
public class TaskListCLI {
    private final CreateTaskUseCase createTaskUseCase;
    private final FindTaskUseCase findTaskUseCase;
    
    public TaskListCLI(CreateTaskUseCase createTaskUseCase, FindTaskUseCase findTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
    }
    
    public void addTask(String description) {
        try {
            CreateTaskCommand command = new CreateTaskCommand(description, null);
            TaskId taskId = createTaskUseCase.execute(command);
            System.out.println("Task created with ID: " + taskId.getValue());
        } catch (Exception e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
    }
    
    public void listTasks() {
        try {
            List<Task> tasks = findTaskUseCase.findAll();
            if (tasks.isEmpty()) {
                System.out.println("No tasks found.");
                return;
            }
            
            for (Task task : tasks) {
                System.out.printf("[%s] %s (%s)%n", 
                    task.getStatus().name(), 
                    task.getDescription(),
                    task.getId().getValue());
            }
        } catch (Exception e) {
            System.err.println("Error listing tasks: " + e.getMessage());
        }
    }
}
```

## 💾 輸出適配器 (Outbound Adapters) 設計規範

### 持久化適配器 (JPA)
- **Repository 介面實作**: 實作領域層定義的 Repository
- **實體映射**: JPA 實體與領域物件之間的轉換
- **事務管理**: 適當的事務邊界設定
- **查詢最佳化**: 效能最佳化的查詢設計

#### JPA Adapter 範例
```java
@Repository
@Transactional
public class TaskJpaAdapter implements TaskRepository {
    private final TaskJpaRepository jpaRepository;
    private final TaskPersistenceMapper mapper;
    
    public TaskJpaAdapter(TaskJpaRepository jpaRepository, TaskPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public void save(Task task) {
        TaskEntity entity = mapper.toEntity(task);
        jpaRepository.save(entity);
    }
    
    @Override
    public Optional<Task> findById(TaskId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomain);
    }
    
    @Override
    public List<Task> findByProjectId(ProjectId projectId) {
        return jpaRepository.findByProjectId(projectId.getValue())
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(TaskId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
```

#### JPA Entity 範例
```java
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatusEntity status;
    
    @Column(name = "project_id")
    private UUID projectId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // JPA 需要無參建構子
    protected TaskEntity() {}
    
    public TaskEntity(UUID id, String description, TaskStatusEntity status, 
                     UUID projectId, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }
    
    // Getters and setters...
}

enum TaskStatusEntity {
    TODO, IN_PROGRESS, DONE
}
```

#### Persistence Mapper 範例
```java
@Component
public class TaskPersistenceMapper {
    
    public TaskEntity toEntity(Task task) {
        return new TaskEntity(
            task.getId().getValue(),
            task.getDescription(),
            toEntityStatus(task.getStatus()),
            task.getProjectId() != null ? task.getProjectId().getValue() : null,
            task.getCreatedAt()
        );
    }
    
    public Task toDomain(TaskEntity entity) {
        return Task.restore(
            TaskId.of(entity.getId()),
            entity.getDescription(),
            toDomainStatus(entity.getStatus()),
            entity.getProjectId() != null ? ProjectId.of(entity.getProjectId()) : null,
            entity.getCreatedAt()
        );
    }
    
    private TaskStatusEntity toEntityStatus(TaskStatus status) {
        return TaskStatusEntity.valueOf(status.name());
    }
    
    private TaskStatus toDomainStatus(TaskStatusEntity status) {
        return TaskStatus.valueOf(status.name());
    }
}
```

### 通知適配器
```java
@Component
public class EmailNotificationAdapter implements NotificationService {
    private final JavaMailSender mailSender;
    
    public EmailNotificationAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    public void notifyTaskCreated(Task task) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("user@example.com");
            message.setSubject("New Task Created");
            message.setText("Task '" + task.getDescription() + "' has been created.");
            
            mailSender.send(message);
        } catch (Exception e) {
            // 記錄錯誤但不影響主要業務流程
            log.error("Failed to send email notification for task: {}", task.getId(), e);
        }
    }
}
```

## 🔧 錯誤處理和異常映射
- **統一錯誤處理**: 使用 @ControllerAdvice 統一處理異常
- **適當狀態碼**: HTTP 狀態碼與業務異常的映射
- **錯誤訊息格式**: 統一的錯誤回應格式

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
            "TASK_NOT_FOUND",
            e.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolation(BusinessRuleViolationException e) {
        ErrorResponse error = new ErrorResponse(
            "BUSINESS_RULE_VIOLATION",
            e.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            message,
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

public class ErrorResponse {
    private String code;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    
    public ErrorResponse(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters...
}
```

## 🧪 適配器層測試策略

### Web 適配器測試
```java
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CreateTaskUseCase createTaskUseCase;
    
    @MockBean
    private FindTaskUseCase findTaskUseCase;
    
    @MockBean
    private TaskWebMapper mapper;
    
    @Test
    void createTask_whenValidRequest_thenReturnCreated() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest("Test task", null);
        CreateTaskCommand command = new CreateTaskCommand("Test task", null);
        TaskId taskId = TaskId.generate();
        Task task = Task.create("Test task", null);
        TaskResponse response = new TaskResponse(taskId.getValue(), "Test task", "TODO", null, LocalDateTime.now());
        
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
                .andExpect(jsonPath("$.description").value("Test task"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }
}
```

### 持久化適配器測試
```java
@DataJpaTest
class TaskJpaAdapterTest {
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
    void save_whenValidTask_thenPersistToDatabase() {
        // Given
        Task task = Task.create("Test task", null);
        
        // When
        taskJpaAdapter.save(task);
        entityManager.flush();
        
        // Then
        Optional<TaskEntity> savedEntity = jpaRepository.findById(task.getId().getValue());
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getDescription()).isEqualTo("Test task");
        assertThat(savedEntity.get().getStatus()).isEqualTo(TaskStatusEntity.TODO);
    }
}
```

## ✅ 適配器層檢查清單
- [ ] **技術細節隔離**: 技術細節完全封裝在適配器層
- [ ] **埠實作**: 正確實作領域層定義的埠介面
- [ ] **DTO 轉換**: 適當的 DTO 與領域物件轉換
- [ ] **錯誤處理**: 統一的錯誤處理和異常映射
- [ ] **驗證**: 輸入驗證在適配器層進行
- [ ] **事務邊界**: 適當的事務管理配置
- [ ] **測試覆蓋**: 充分的單元測試和整合測試
- [ ] **依賴方向**: 適配器依賴領域層，不被領域層依賴