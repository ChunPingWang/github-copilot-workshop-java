# Task 與 TaskList 開發規範

**適用範圍**: `**/*Task*.java`

本文件定義了 Task（任務）和 TaskList（任務清單）系統的核心業務邏輯、開發規範與實作指南。

## 🎯 業務領域定義

### 核心業務概念
- **任務 (Task)**: 系統的核心實體，代表需要完成的工作項目
- **任務狀態 (TaskStatus)**: 任務的生命週期狀態管理
- **任務清單 (TaskList)**: 任務的集合管理邏輯
- **專案關聯**: 每個任務必須屬於一個專案

### 核心業務規則

#### 任務創建規則
- 任務必須有非空的描述
- 任務必須關聯到一個存在的專案
- 新建立的任務預設狀態為 PENDING
- 任務描述不能為 null 或空字串
- 任務描述會自動去除前後空白

#### 任務狀態管理規則
- 任務狀態只能是 PENDING 或 COMPLETED
- 任務可以從 PENDING 變更為 COMPLETED
- 任務可以從 COMPLETED 變更為 PENDING（重新開啟）
- 已完成的任務不能重複標記為已完成
- 任務狀態變更必須透過領域方法進行，不允許直接設定

#### 任務識別規則
- 每個任務擁有唯一的 TaskId
- TaskId 必須是正整數
- TaskId 必須在系統內全域唯一
- 任務透過 ID 進行查找和操作
- TaskId 不能為 null 或小於等於 0

#### 專案關聯規則
- 每個任務必須屬於一個專案
- 專案必須在創建任務前存在
- 任務建立後不能變更所屬專案
- 找不到專案時無法建立任務

## 🔄 業務流程

### 任務創建流程
1. 驗證專案是否存在
2. 生成新的唯一任務 ID
3. 驗證任務描述符合規則
4. 建立任務實體，預設狀態為 PENDING
5. 將任務關聯到指定專案
6. 持久化任務資料

### 任務狀態變更流程
1. 根據任務 ID 查找任務
2. 驗證任務存在
3. 檢查任務所屬專案（如有指定）
4. 根據業務規則執行狀態變更
5. 持久化狀態變更

### 任務檢視流程
1. 載入所有專案
2. 為每個專案載入關聯的任務
3. 將任務按專案分組
4. 提供格式化的檢視資料

## 📋 業務操作

### 支援的任務操作
- **建立任務**: 在指定專案中建立新任務
- **標記完成**: 將任務狀態變更為已完成
- **標記未完成**: 將任務狀態變更為待處理
- **檢視任務**: 顯示所有專案及其任務
- **查找任務**: 根據 ID 查找特定任務

### 支援的專案操作  
- **建立專案**: 建立新專案容器
- **檢視專案**: 列出所有專案及其任務
- **專案驗證**: 確認專案存在性

### CLI 命令對應
- `show`: 顯示所有專案和任務
- `add project <name>`: 建立新專案
- `add task <project> <description>`: 在專案中建立任務
- `check <id>`: 標記任務為已完成
- `uncheck <id>`: 標記任務為待處理
- `help`: 顯示可用命令
- `quit`: 結束程式

## ⚠️ 業務異常情況

### 任務相關異常
- **TaskNotFoundException**: 找不到指定的任務
- **IllegalStateException**: 嘗試重複完成已完成的任務
- **IllegalArgumentException**: 任務描述為空或無效的任務 ID

### 專案相關異常
- **ProjectNotFoundException**: 找不到指定的專案
- **IllegalArgumentException**: 專案名稱為空

### 業務約束違反
- 嘗試在不存在的專案中建立任務
- 使用無效的任務 ID 進行操作
- 提供空的任務描述
- 重複標記任務為已完成

## 🎯 業務不變量

### 任務實體不變量
- 任務 ID 永遠不能為 null 或無效值
- 任務描述永遠不能為 null 或空字串
- 任務必須永遠屬於一個專案
- 任務狀態必須永遠是有效的列舉值

### 系統層級不變量
- 任務 ID 在系統中永遠唯一
- 每個任務永遠只能屬於一個專案
- 專案名稱在系統中永遠唯一
- 任務狀態變更永遠遵循業務規則

## 📊 業務資料模型

### 任務資料結構
- **識別符**: 唯一的正整數 ID
- **描述**: 非空的文字描述
- **狀態**: PENDING 或 COMPLETED
- **專案關聯**: 所屬專案的識別符

### 專案資料結構  
- **名稱**: 唯一的非空專案名稱
- **任務集合**: 屬於該專案的所有任務

### 狀態定義
- **PENDING**: 任務尚未完成，需要處理
- **COMPLETED**: 任務已經完成，無需進一步處理



## 🛠️ Task 開發規範

### 領域模型實作規範
- **不可變性設計**: Task 實體必須使用不可變物件設計
- **值物件使用**: TaskId、TaskStatus 必須實作為值物件
- **建構子驗證**: 所有物件建立時必須進行自我驗證
- **業務邏輯封裝**: 狀態變更邏輯必須封裝在領域物件內部

### 命名慣例
- **類別命名**: `Task`, `TaskList`, `TaskId`, `TaskStatus`, `TaskRepository`
- **方法命名**: `createTask()`, `completeTask()`, `findTaskById()`, `addToProject()`
- **異常命名**: `TaskNotFoundException`, `TaskAlreadyCompletedException`
- **常數命名**: `DEFAULT_TASK_STATUS`, `MAX_DESCRIPTION_LENGTH`

### 架構約束
- **純領域邏輯**: Task 相關類別不得依賴任何基礎設施框架
- **介面隔離**: 使用 Repository 介面與持久化層解耦
- **依賴注入**: 透過建構子注入外部依賴
- **異常處理**: 使用領域專用異常表達業務規則違反

## 💻 Task 實作範例

### Task 領域實體範例
```java
public class Task {
    private final TaskId id;
    private final String description;
    private final TaskStatus status;
    private final ProjectId projectId;
    private final LocalDateTime createdAt;
    private final LocalDateTime completedAt;
    
    private Task(TaskId id, String description, TaskStatus status, 
                ProjectId projectId, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = Objects.requireNonNull(id, "Task ID cannot be null");
        this.description = validateDescription(description);
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "Project ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created time cannot be null");
        this.completedAt = completedAt;
    }
    
    /**
     * 建立新任務
     */
    public static Task create(String description, ProjectId projectId) {
        return new Task(
            TaskId.generate(),
            description,
            TaskStatus.PENDING,
            projectId,
            LocalDateTime.now(),
            null
        );
    }
    
    /**
     * 標記任務為已完成
     */
    public Task complete() {
        if (status == TaskStatus.COMPLETED) {
            throw new TaskAlreadyCompletedException("Task " + id + " is already completed");
        }
        return new Task(id, description, TaskStatus.COMPLETED, projectId, createdAt, LocalDateTime.now());
    }
    
    /**
     * 重新開啟任務
     */
    public Task reopen() {
        if (status == TaskStatus.PENDING) {
            throw new IllegalStateException("Task " + id + " is already pending");
        }
        return new Task(id, description, TaskStatus.PENDING, projectId, createdAt, null);
    }
    
    private String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }
        return description.trim();
    }
    
    // Getters
    public TaskId getId() { return id; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public ProjectId getProjectId() { return projectId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Optional<LocalDateTime> getCompletedAt() { return Optional.ofNullable(completedAt); }
    
    public boolean isCompleted() { return status == TaskStatus.COMPLETED; }
    public boolean isPending() { return status == TaskStatus.PENDING; }
}
```

### TaskId 值物件範例
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
    
    @Override
    public String toString() {
        return "TaskId{" + value + "}";
    }
}
```

### TaskStatus 列舉範例
```java
public enum TaskStatus {
    PENDING("待處理"),
    COMPLETED("已完成");
    
    private final String displayName;
    
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

### TaskList 聚合範例
```java
public class TaskList {
    private final ProjectId projectId;
    private final List<Task> tasks;
    
    public TaskList(ProjectId projectId) {
        this.projectId = Objects.requireNonNull(projectId, "Project ID cannot be null");
        this.tasks = new ArrayList<>();
    }
    
    /**
     * 新增任務到清單
     */
    public TaskList addTask(String description) {
        Task newTask = Task.create(description, projectId);
        TaskList newTaskList = new TaskList(projectId);
        newTaskList.tasks.addAll(this.tasks);
        newTaskList.tasks.add(newTask);
        return newTaskList;
    }
    
    /**
     * 完成指定任務
     */
    public TaskList completeTask(TaskId taskId) {
        TaskList newTaskList = new TaskList(projectId);
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                newTaskList.tasks.add(task.complete());
            } else {
                newTaskList.tasks.add(task);
            }
        }
        
        if (newTaskList.tasks.size() == tasks.size()) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }
        
        return newTaskList;
    }
    
    /**
     * 獲取所有待處理任務
     */
    public List<Task> getPendingTasks() {
        return tasks.stream()
            .filter(Task::isPending)
            .collect(Collectors.toUnmodifiableList());
    }
    
    /**
     * 獲取所有已完成任務
     */
    public List<Task> getCompletedTasks() {
        return tasks.stream()
            .filter(Task::isCompleted)
            .collect(Collectors.toUnmodifiableList());
    }
    
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
    
    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }
}
```

### Repository 介面範例
```java
public interface TaskRepository {
    void save(Task task);
    Optional<Task> findById(TaskId id);
    List<Task> findByProjectId(ProjectId projectId);
    List<Task> findByStatus(TaskStatus status);
    void deleteById(TaskId id);
}
```

### 領域服務範例
```java
public class TaskDomainService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskDomainService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = Objects.requireNonNull(taskRepository);
        this.projectRepository = Objects.requireNonNull(projectRepository);
    }
    
    /**
     * 建立新任務
     */
    public TaskId createTask(String description, ProjectId projectId) {
        // 驗證專案存在
        projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("Project not found: " + projectId));
        
        // 建立任務
        Task task = Task.create(description, projectId);
        taskRepository.save(task);
        
        return task.getId();
    }
    
    /**
     * 完成任務
     */
    public void completeTask(TaskId taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found: " + taskId));
        
        Task completedTask = task.complete();
        taskRepository.save(completedTask);
    }
}
```

## 🧪 Task 測試策略

### 單元測試規範
- **測試命名**: 使用 Given-When-Then 模式
- **測試隔離**: 每個測試獨立運行，使用 Mock 隔離外部依賴
- **邊界測試**: 測試所有邊界條件和異常情況
- **不變量驗證**: 驗證業務不變量在所有操作後保持正確

### Task 領域實體測試範例
```java
@DisplayName("Task 領域實體測試")
class TaskTest {
    
    @Test
    @DisplayName("當建立有效任務時，應該設定正確的初始狀態")
    void create_whenValidInput_thenSetCorrectInitialState() {
        // Given
        String description = "學習六角形架構";
        ProjectId projectId = ProjectId.generate();
        
        // When
        Task task = Task.create(description, projectId);
        
        // Then
        assertThat(task.getId()).isNotNull();
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getProjectId()).isEqualTo(projectId);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getCompletedAt()).isEmpty();
        assertThat(task.isPending()).isTrue();
        assertThat(task.isCompleted()).isFalse();
    }
    
    @Test
    @DisplayName("當任務描述為空時，建立應該拋出異常")
    void create_whenEmptyDescription_thenThrowException() {
        // Given
        String emptyDescription = "";
        ProjectId projectId = ProjectId.generate();
        
        // When & Then
        assertThatThrownBy(() -> Task.create(emptyDescription, projectId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Task description cannot be empty");
    }
    
    @Test
    @DisplayName("當完成待處理任務時，應該正確變更狀態")
    void complete_whenPendingTask_thenChangeStatusToCompleted() {
        // Given
        Task pendingTask = Task.create("測試任務", ProjectId.generate());
        
        // When
        Task completedTask = pendingTask.complete();
        
        // Then
        assertThat(completedTask.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(completedTask.isCompleted()).isTrue();
        assertThat(completedTask.isPending()).isFalse();
        assertThat(completedTask.getCompletedAt()).isPresent();
        
        // 驗證不變量：其他屬性保持不變
        assertThat(completedTask.getId()).isEqualTo(pendingTask.getId());
        assertThat(completedTask.getDescription()).isEqualTo(pendingTask.getDescription());
        assertThat(completedTask.getProjectId()).isEqualTo(pendingTask.getProjectId());
        assertThat(completedTask.getCreatedAt()).isEqualTo(pendingTask.getCreatedAt());
    }
    
    @Test
    @DisplayName("當重複完成已完成任務時，應該拋出異常")
    void complete_whenAlreadyCompleted_thenThrowException() {
        // Given
        Task task = Task.create("測試任務", ProjectId.generate());
        Task completedTask = task.complete();
        
        // When & Then
        assertThatThrownBy(() -> completedTask.complete())
            .isInstanceOf(TaskAlreadyCompletedException.class)
            .hasMessageContaining("is already completed");
    }
}
```

### TaskList 聚合測試範例
```java
@DisplayName("TaskList 聚合測試")
class TaskListTest {
    
    private ProjectId projectId;
    private TaskList taskList;
    
    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        taskList = new TaskList(projectId);
    }
    
    @Test
    @DisplayName("當新增任務時，任務清單應該包含新任務")
    void addTask_whenValidDescription_thenContainNewTask() {
        // Given
        String description = "新任務";
        
        // When
        TaskList updatedTaskList = taskList.addTask(description);
        
        // Then
        assertThat(updatedTaskList.getTaskCount()).isEqualTo(1);
        assertThat(updatedTaskList.getAllTasks()).hasSize(1);
        assertThat(updatedTaskList.getAllTasks().get(0).getDescription()).isEqualTo(description);
        assertThat(updatedTaskList.getAllTasks().get(0).isPending()).isTrue();
    }
    
    @Test
    @DisplayName("當完成任務時，任務狀態應該變更為已完成")
    void completeTask_whenValidTaskId_thenTaskStatusShouldBeCompleted() {
        // Given
        TaskList taskListWithTask = taskList.addTask("測試任務");
        TaskId taskId = taskListWithTask.getAllTasks().get(0).getId();
        
        // When
        TaskList updatedTaskList = taskListWithTask.completeTask(taskId);
        
        // Then
        assertThat(updatedTaskList.getCompletedTaskCount()).isEqualTo(1);
        assertThat(updatedTaskList.getPendingTasks()).isEmpty();
        assertThat(updatedTaskList.getCompletedTasks()).hasSize(1);
        assertThat(updatedTaskList.getCompletedTasks().get(0).isCompleted()).isTrue();
    }
}
```

### 領域服務測試範例
```java
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskDomainService 測試")
class TaskDomainServiceTest {
    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private ProjectRepository projectRepository;
    
    @InjectMocks
    private TaskDomainService taskDomainService;
    
    @Test
    @DisplayName("當專案存在且輸入有效時，應該成功建立任務")
    void createTask_whenProjectExistsAndValidInput_thenCreateTaskSuccessfully() {
        // Given
        String description = "測試任務";
        ProjectId projectId = ProjectId.generate();
        Project project = Project.create("測試專案");
        
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        
        // When
        TaskId result = taskDomainService.createTask(description, projectId);
        
        // Then
        assertThat(result).isNotNull();
        verify(projectRepository).findById(projectId);
        verify(taskRepository).save(any(Task.class));
    }
    
    @Test
    @DisplayName("當專案不存在時，建立任務應該拋出異常")
    void createTask_whenProjectNotExists_thenThrowProjectNotFoundException() {
        // Given
        String description = "測試任務";
        ProjectId nonExistentProjectId = ProjectId.generate();
        
        when(projectRepository.findById(nonExistentProjectId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> taskDomainService.createTask(description, nonExistentProjectId))
            .isInstanceOf(ProjectNotFoundException.class)
            .hasMessageContaining("Project not found");
        
        verify(projectRepository).findById(nonExistentProjectId);
        verifyNoInteractions(taskRepository);
    }
}
```

## ✅ Task 開發工作清單

### 📝 領域模型開發檢查清單
- [ ] **Task 實體設計**
  - [ ] 實作不可變物件設計
  - [ ] 加入建構子驗證邏輯
  - [ ] 實作 `create()` 靜態工廠方法
  - [ ] 實作 `complete()` 和 `reopen()` 業務方法
  - [ ] 實作 `equals()`, `hashCode()`, `toString()` 方法
  - [ ] 加入適當的 getter 方法

- [ ] **TaskId 值物件設計**
  - [ ] 實作不可變值物件
  - [ ] 提供 `of()` 和 `generate()` 靜態方法
  - [ ] 實作正確的 `equals()` 和 `hashCode()`
  - [ ] 加入 null 值驗證

- [ ] **TaskStatus 列舉設計**
  - [ ] 定義 PENDING 和 COMPLETED 狀態
  - [ ] 加入 `displayName` 屬性
  - [ ] 提供人性化的顯示名稱

- [ ] **TaskList 聚合設計**
  - [ ] 實作任務集合管理邏輯
  - [ ] 提供 `addTask()` 方法
  - [ ] 提供 `completeTask()` 方法
  - [ ] 實作查詢方法 (`getPendingTasks()`, `getCompletedTasks()`)

### 🏗️ 架構合規檢查清單
- [ ] **依賴方向檢查**
  - [ ] Task 相關類別不依賴任何基礎設施框架
  - [ ] 使用 Repository 介面與持久化層解耦
  - [ ] 領域邏輯完全獨立於外部技術

- [ ] **介面設計檢查**
  - [ ] TaskRepository 介面使用領域語言
  - [ ] 方法回傳領域物件而非基礎設施物件
  - [ ] 介面簽名表達業務意圖

- [ ] **異常處理檢查**
  - [ ] 定義領域特定異常類別
  - [ ] 異常訊息提供清楚的業務含義
  - [ ] 使用 RuntimeException 子類別

### 🧪 測試完整性檢查清單
- [ ] **單元測試覆蓋**
  - [ ] Task 實體所有公開方法有測試
  - [ ] TaskId 值物件所有方法有測試
  - [ ] TaskList 聚合所有業務邏輯有測試
  - [ ] TaskDomainService 所有用例有測試

- [ ] **邊界條件測試**
  - [ ] 測試 null 值輸入情況
  - [ ] 測試空字串和空白字串情況
  - [ ] 測試業務規則違反情況
  - [ ] 測試狀態轉換的所有路徑

- [ ] **測試品質檢查**
  - [ ] 使用 Given-When-Then 測試結構
  - [ ] 測試名稱清楚表達測試意圖
  - [ ] 使用 @DisplayName 提供中文說明
  - [ ] Mock 使用適當，避免過度 Mock

### 📋 程式碼品質檢查清單
- [ ] **命名規範檢查**
  - [ ] 類別名稱使用 PascalCase
  - [ ] 方法名稱使用 camelCase 且表達業務意圖
  - [ ] 變數名稱具有描述性
  - [ ] 常數使用 SCREAMING_SNAKE_CASE

- [ ] **程式碼風格檢查**
  - [ ] 遵循 Google Java Style Guide
  - [ ] 適當的註解和 JavaDoc
  - [ ] 方法長度合理 (建議不超過 20 行)
  - [ ] 類別職責單一且清晰

- [ ] **業務邏輯檢查**
  - [ ] 所有業務規則在領域物件中實作
  - [ ] 狀態變更透過業務方法進行
  - [ ] 不變量在所有操作後保持正確
  - [ ] 業務異常適當地表達規則違反

### 🚀 整合檢查清單
- [ ] **適配器整合**
  - [ ] JPA 適配器正確實作 TaskRepository
  - [ ] Web 適配器正確處理 Task 相關請求
  - [ ] CLI 適配器正確呼叫 Task 用例

- [ ] **應用服務整合**
  - [ ] 應用服務正確編排 Task 相關用例
  - [ ] 事務邊界適當設定
  - [ ] 事件發布機制正確運作

- [ ] **配置檢查**
  - [ ] 依賴注入配置正確
  - [ ] Bean 生命週期管理適當
  - [ ] 不同環境配置正確分離

---

**開發重點**: Task 系統的開發必須嚴格遵循六角形架構原則，確保業務邏輯的純淨性和可測試性。每個開發階段都應該參考本工作清單，確保程式碼品質和架構合規性。
