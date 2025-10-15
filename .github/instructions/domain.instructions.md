---
applyTo: '**/domain/**'
---

# 領域層 (Domain Layer) 開發規範

## 🎯 領域層核心原則
- **業務邏輯純淨性**: 完全獨立於框架和外部技術
- **依賴方向**: 不依賴任何外部框架或基礎設施
- **可測試性**: 核心邏輯完全獨立於外部依賴進行測試
- **技術無關性**: 業務邏輯不依賴具體技術實作

## 📁 領域層套件結構
```
domain/
├── model/                 # 領域實體和值物件
│   ├── Task.java
│   ├── Project.java
│   ├── TaskId.java        # 值物件
│   └── TaskStatus.java    # 列舉
├── port/                  # 介面定義
│   ├── inbound/           # 輸入埠 (Use Cases)
│   │   ├── CreateTaskUseCase.java
│   │   ├── FindTaskUseCase.java
│   │   └── CompleteTaskUseCase.java
│   └── outbound/          # 輸出埠 (Repository 介面)
│       ├── TaskRepository.java
│       ├── ProjectRepository.java
│       └── NotificationService.java
├── service/               # 領域服務 (Use Case 實作)
│   ├── TaskDomainService.java
│   └── ProjectDomainService.java
└── exception/             # 領域異常
    ├── TaskNotFoundException.java
    └── BusinessRuleViolationException.java
```

## 🔌 輸入埠 (Inbound Ports) 設計規範
- **業務意圖表達**: 介面應該表達業務意圖，不包含技術細節
- **命名規範**: 使用業務術語 (如: `createTask`, `completeTask`)
- **參數設計**: 使用 Command 物件封裝參數
- **回傳值**: 回傳領域物件或值物件

### 範例實作
```java
// Use Case 介面 (輸入埠)
public interface CreateTaskUseCase {
    TaskId execute(CreateTaskCommand command);
}

public interface FindTaskUseCase {
    Optional<Task> findById(TaskId taskId);
    List<Task> findByProjectId(ProjectId projectId);
}
```

## 🔌 輸出埠 (Outbound Ports) 設計規範
- **領域語言**: 使用領域語言，避免技術實作細節
- **領域物件**: 回傳領域物件，不洩漏基礎設施概念
- **抽象介面**: 定義領域層對外部系統的需求

### 範例實作
```java
// Repository 介面 (輸出埠)
public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(TaskId id);
    List<Task> findByProjectId(ProjectId projectId);
    void deleteById(TaskId id);
}
```

## 🏗️ 領域服務實作規範
- **純業務邏輯**: 只包含業務邏輯，無外部框架依賴
- **依賴注入**: 透過建構子注入輸出埠
- **錯誤處理**: 使用領域異常處理業務規則違反
- **不變量維護**: 確保業務規則和約束

### 範例實作
```java
public class TaskDomainService implements CreateTaskUseCase, FindTaskUseCase {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskDomainService(TaskRepository taskRepository, 
                           ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }
    
    @Override
    public TaskId execute(CreateTaskCommand command) {
        // 驗證業務規則
        if (command.getDescription() == null || command.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        
        // 檢查專案是否存在
        if (command.getProjectId() != null) {
            projectRepository.findById(command.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(command.getProjectId()));
        }
        
        // 建立領域物件
        Task task = Task.create(command.getDescription(), command.getProjectId());
        taskRepository.save(task);
        return task.getId();
    }
}
```

## 🧱 領域模型設計規範
- **不可變性**: 優先使用不可變物件 (`final` 欄位)
- **值物件**: 使用值物件確保型別安全
- **業務邏輯**: 包含業務邏輯和不變量
- **自我驗證**: 物件建立時自我驗證

### 實體 (Entity) 範例
```java
public class Task {
    private final TaskId id;
    private final String description;
    private final TaskStatus status;
    private final ProjectId projectId;
    private final LocalDateTime createdAt;
    
    private Task(TaskId id, String description, TaskStatus status, 
                ProjectId projectId, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "Task ID cannot be null");
        this.description = validateDescription(description);
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
        this.projectId = projectId;
        this.createdAt = Objects.requireNonNull(createdAt, "Created time cannot be null");
    }
    
    public static Task create(String description, ProjectId projectId) {
        return new Task(
            TaskId.generate(),
            description,
            TaskStatus.TODO,
            projectId,
            LocalDateTime.now()
        );
    }
    
    public Task complete() {
        if (status == TaskStatus.DONE) {
            throw new BusinessRuleViolationException("Task is already completed");
        }
        return new Task(id, description, TaskStatus.DONE, projectId, createdAt);
    }
    
    private String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        return description.trim();
    }
}
```

### 值物件 (Value Object) 範例
```java
public class TaskId {
    private final UUID value;
    
    private TaskId(UUID value) {
        this.value = Objects.requireNonNull(value, "TaskId value cannot be null");
    }
    
    public static TaskId of(UUID value) {
        return new TaskId(value);
    }
    
    public static TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }
    
    public UUID getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(value, taskId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
```

## 🚨 領域異常設計
- **特定異常**: 為不同的業務規則違反建立特定異常類別
- **有意義訊息**: 提供清楚的錯誤訊息
- **不檢查異常**: 使用 RuntimeException 子類別

```java
public class TaskNotFoundException extends RuntimeException {
    private final TaskId taskId;
    
    public TaskNotFoundException(TaskId taskId) {
        super("Task not found with ID: " + taskId.getValue());
        this.taskId = taskId;
    }
    
    public TaskId getTaskId() {
        return taskId;
    }
}

public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
```

## 🧪 領域層測試策略
- **純單元測試**: 無外部依賴的測試
- **Mock 輸出埠**: 使用 Mock 模擬輸出埠
- **業務規則驗證**: 專注於業務邏輯正確性
- **Given-When-Then**: 使用清晰的測試結構

### 測試範例
```java
@ExtendWith(MockitoExtension.class)
class TaskDomainServiceTest {
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private ProjectRepository projectRepository;
    
    @InjectMocks
    private TaskDomainService taskDomainService;
    
    @Test
    void execute_whenValidCommand_thenCreateTask() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("學習六角形架構", null);
        
        // When
        TaskId result = taskDomainService.execute(command);
        
        // Then
        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }
    
    @Test
    void execute_whenEmptyDescription_thenThrowException() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("", null);
        
        // When & Then
        assertThatThrownBy(() -> taskDomainService.execute(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Task description cannot be empty");
    }
}
```

## ✅ 領域層檢查清單
- [ ] **領域純淨性**: 領域層不包含任何基礎設施或框架程式碼
- [ ] **埠介面設計**: 輸入埠表達業務意圖，輸出埠使用領域語言
- [ ] **業務邏輯封裝**: 業務規則和不變量在領域物件中
- [ ] **依賴方向**: 領域層不依賴外部框架或適配器
- [ ] **測試獨立性**: 領域邏輯可以無外部依賴進行測試
- [ ] **自我驗證**: 領域物件在建立時進行自我驗證
- [ ] **不可變性**: 優先使用不可變物件設計