---
applyTo: '**/application/**'
---

# 應用程式層 (Application Layer) 開發規範

## 🎯 應用程式層核心原則
- **協調器角色**: 協調不同 Use Case 之間的互動
- **事務邊界**: 定義和管理事務邊界
- **跨領域服務**: 提供跨多個領域的應用服務
- **工作流程編排**: 編排複雜的業務工作流程

## 📁 應用程式層套件結構
```
application/
├── service/               # 應用服務
│   ├── TaskApplicationService.java
│   ├── ProjectApplicationService.java
│   └── WorkflowApplicationService.java
├── command/               # 應用命令
│   ├── CreateProjectWithTasksCommand.java
│   ├── BulkTaskOperationCommand.java
│   └── TaskMigrationCommand.java
├── query/                 # 應用查詢
│   ├── TaskSummaryQuery.java
│   ├── ProjectReportQuery.java
│   └── DashboardQuery.java
├── dto/                   # 應用 DTO
│   ├── TaskSummaryDto.java
│   ├── ProjectReportDto.java
│   └── DashboardDto.java
└── event/                 # 應用事件
    ├── handler/           # 事件處理器
    │   ├── TaskCreatedEventHandler.java
    │   └── ProjectCompletedEventHandler.java
    └── publisher/         # 事件發布器
        └── ApplicationEventPublisher.java
```

## 🔧 應用服務設計規範

### 應用服務職責
- **事務管理**: 管理跨多個聚合的事務
- **Use Case 編排**: 編排多個領域服務的協作
- **數據轉換**: 處理應用層的 DTO 轉換
- **安全性檢查**: 執行應用層級的安全性驗證

#### TaskApplicationService 範例
```java
@Service
@Transactional
public class TaskApplicationService {
    private final CreateTaskUseCase createTaskUseCase;
    private final FindTaskUseCase findTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final FindProjectUseCase findProjectUseCase;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;
    
    public TaskApplicationService(CreateTaskUseCase createTaskUseCase,
                                FindTaskUseCase findTaskUseCase,
                                CompleteTaskUseCase completeTaskUseCase,
                                FindProjectUseCase findProjectUseCase,
                                NotificationService notificationService,
                                ApplicationEventPublisher eventPublisher) {
        this.createTaskUseCase = createTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.findProjectUseCase = findProjectUseCase;
        this.notificationService = notificationService;
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * 建立任務並發送通知
     */
    public TaskSummaryDto createTaskWithNotification(CreateTaskCommand command) {
        // 驗證專案權限
        if (command.getProjectId() != null) {
            Project project = findProjectUseCase.findById(command.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(command.getProjectId()));
            
            // 檢查使用者是否有權限在此專案中建立任務
            verifyProjectAccess(project, command.getUserId());
        }
        
        // 建立任務
        TaskId taskId = createTaskUseCase.execute(command);
        Task task = findTaskUseCase.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        // 發送通知
        notificationService.notifyTaskCreated(task);
        
        // 發布事件
        eventPublisher.publishEvent(new TaskCreatedEvent(task));
        
        return TaskSummaryDto.from(task);
    }
    
    /**
     * 批次完成任務
     */
    public BulkOperationResult completeTasks(BulkTaskOperationCommand command) {
        List<TaskId> successfulTasks = new ArrayList<>();
        List<TaskOperationError> errors = new ArrayList<>();
        
        for (TaskId taskId : command.getTaskIds()) {
            try {
                CompleteTaskCommand completeCommand = new CompleteTaskCommand(taskId, command.getUserId());
                completeTaskUseCase.execute(completeCommand);
                successfulTasks.add(taskId);
            } catch (Exception e) {
                errors.add(new TaskOperationError(taskId, e.getMessage()));
            }
        }
        
        return new BulkOperationResult(successfulTasks, errors);
    }
    
    private void verifyProjectAccess(Project project, UserId userId) {
        // 實作專案存取權限檢查邏輯
        if (!project.hasAccess(userId)) {
            throw new AccessDeniedException("User does not have access to project: " + project.getId());
        }
    }
}
```

### 跨領域協調服務
```java
@Service
@Transactional
public class WorkflowApplicationService {
    private final CreateProjectUseCase createProjectUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    
    /**
     * 建立專案並初始化預設任務
     */
    public ProjectSummaryDto createProjectWithDefaultTasks(CreateProjectWithTasksCommand command) {
        // 1. 建立專案
        ProjectId projectId = createProjectUseCase.execute(
            new CreateProjectCommand(command.getProjectName(), command.getDescription(), command.getOwnerId())
        );
        
        // 2. 建立預設任務
        List<TaskId> createdTasks = new ArrayList<>();
        for (String taskDescription : command.getDefaultTasks()) {
            CreateTaskCommand taskCommand = new CreateTaskCommand(taskDescription, projectId);
            TaskId taskId = createTaskUseCase.execute(taskCommand);
            createdTasks.add(taskId);
        }
        
        // 3. 發送歡迎通知
        notificationService.notifyProjectCreated(projectId, command.getOwnerId());
        
        // 4. 記錄報表數據
        reportingService.recordProjectCreation(projectId, createdTasks.size());
        
        return ProjectSummaryDto.builder()
            .projectId(projectId)
            .name(command.getProjectName())
            .taskCount(createdTasks.size())
            .build();
    }
}
```

## 📊 查詢服務設計

### 應用查詢服務
- **讀取最佳化**: 針對特定檢視需求最佳化
- **數據聚合**: 聚合來自多個領域的數據
- **快取策略**: 實作適當的快取策略
- **分頁支援**: 支援大數據集的分頁查詢

```java
@Service
@Transactional(readOnly = true)
public class TaskQueryService {
    private final FindTaskUseCase findTaskUseCase;
    private final FindProjectUseCase findProjectUseCase;
    private final CacheManager cacheManager;
    
    /**
     * 獲取任務摘要儀表板
     */
    @Cacheable(value = "dashboard", key = "#userId")
    public DashboardDto getDashboard(UserId userId) {
        // 獲取使用者的所有任務
        List<Task> tasks = findTaskUseCase.findByUserId(userId);
        
        // 計算統計數據
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isCompleted).count();
        long pendingTasks = totalTasks - completedTasks;
        
        // 按專案分組統計
        Map<ProjectId, Long> tasksByProject = tasks.stream()
            .filter(task -> task.getProjectId() != null)
            .collect(Collectors.groupingBy(Task::getProjectId, Collectors.counting()));
        
        // 獲取專案詳細資訊
        List<ProjectSummaryDto> projectSummaries = tasksByProject.entrySet().stream()
            .map(entry -> {
                Project project = findProjectUseCase.findById(entry.getKey())
                    .orElse(null);
                return project != null ? 
                    ProjectSummaryDto.from(project, entry.getValue()) : 
                    null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        return DashboardDto.builder()
            .totalTasks(totalTasks)
            .completedTasks(completedTasks)
            .pendingTasks(pendingTasks)
            .projects(projectSummaries)
            .build();
    }
    
    /**
     * 分頁查詢任務
     */
    public PagedResult<TaskSummaryDto> findTasks(TaskSearchCriteria criteria, Pageable pageable) {
        Page<Task> taskPage = findTaskUseCase.findByCriteria(criteria, pageable);
        
        List<TaskSummaryDto> taskDtos = taskPage.getContent().stream()
            .map(TaskSummaryDto::from)
            .collect(Collectors.toList());
        
        return PagedResult.<TaskSummaryDto>builder()
            .content(taskDtos)
            .totalElements(taskPage.getTotalElements())
            .totalPages(taskPage.getTotalPages())
            .currentPage(taskPage.getNumber())
            .pageSize(taskPage.getSize())
            .build();
    }
}
```

## 🎯 命令和 DTO 設計

### 應用命令
```java
public class CreateProjectWithTasksCommand {
    private final String projectName;
    private final String description;
    private final UserId ownerId;
    private final List<String> defaultTasks;
    
    public CreateProjectWithTasksCommand(String projectName, String description, 
                                       UserId ownerId, List<String> defaultTasks) {
        this.projectName = validateProjectName(projectName);
        this.description = description;
        this.ownerId = Objects.requireNonNull(ownerId, "Owner ID cannot be null");
        this.defaultTasks = new ArrayList<>(Objects.requireNonNull(defaultTasks, "Default tasks cannot be null"));
    }
    
    private String validateProjectName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        return projectName.trim();
    }
    
    // Getters...
}

public class BulkTaskOperationCommand {
    private final List<TaskId> taskIds;
    private final UserId userId;
    private final String reason;
    
    public BulkTaskOperationCommand(List<TaskId> taskIds, UserId userId, String reason) {
        this.taskIds = new ArrayList<>(Objects.requireNonNull(taskIds, "Task IDs cannot be null"));
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
        this.reason = reason;
        
        if (taskIds.isEmpty()) {
            throw new IllegalArgumentException("Task IDs cannot be empty");
        }
    }
    
    // Getters...
}
```

### 應用 DTO
```java
@Builder
public class TaskSummaryDto {
    private final UUID id;
    private final String description;
    private final String status;
    private final UUID projectId;
    private final String projectName;
    private final LocalDateTime createdAt;
    private final LocalDateTime completedAt;
    
    public static TaskSummaryDto from(Task task) {
        return TaskSummaryDto.builder()
            .id(task.getId().getValue())
            .description(task.getDescription())
            .status(task.getStatus().name())
            .projectId(task.getProjectId() != null ? task.getProjectId().getValue() : null)
            .createdAt(task.getCreatedAt())
            .completedAt(task.getCompletedAt())
            .build();
    }
    
    // Getters...
}

@Builder
public class DashboardDto {
    private final long totalTasks;
    private final long completedTasks;
    private final long pendingTasks;
    private final List<ProjectSummaryDto> projects;
    private final LocalDateTime lastUpdated;
    
    // Getters...
}
```

## 📡 事件處理

### 事件處理器
```java
@Component
public class TaskCreatedEventHandler {
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    
    @EventListener
    @Async
    public void handleTaskCreated(TaskCreatedEvent event) {
        Task task = event.getTask();
        
        // 發送通知給專案成員
        if (task.getProjectId() != null) {
            notificationService.notifyProjectMembers(task.getProjectId(), 
                "New task created: " + task.getDescription());
        }
        
        // 更新報表統計
        reportingService.incrementTaskCount(task.getProjectId());
    }
}

@Component
public class ProjectCompletedEventHandler {
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    
    @EventListener
    public void handleProjectCompleted(ProjectCompletedEvent event) {
        Project project = event.getProject();
        
        // 發送完成通知
        notificationService.notifyProjectCompletion(project);
        
        // 生成完成報告
        reportingService.generateCompletionReport(project.getId());
    }
}
```

## 🧪 應用層測試策略

### 應用服務測試
```java
@ExtendWith(MockitoExtension.class)
class TaskApplicationServiceTest {
    @Mock
    private CreateTaskUseCase createTaskUseCase;
    @Mock
    private FindTaskUseCase findTaskUseCase;
    @Mock
    private FindProjectUseCase findProjectUseCase;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    @InjectMocks
    private TaskApplicationService taskApplicationService;
    
    @Test
    void createTaskWithNotification_whenValidCommand_thenCreateTaskAndNotify() {
        // Given
        ProjectId projectId = ProjectId.generate();
        CreateTaskCommand command = new CreateTaskCommand("Test task", projectId);
        TaskId taskId = TaskId.generate();
        Task task = Task.create("Test task", projectId);
        Project project = Project.create("Test Project", "Description", UserId.generate());
        
        when(findProjectUseCase.findById(projectId)).thenReturn(Optional.of(project));
        when(createTaskUseCase.execute(command)).thenReturn(taskId);
        when(findTaskUseCase.findById(taskId)).thenReturn(Optional.of(task));
        
        // When
        TaskSummaryDto result = taskApplicationService.createTaskWithNotification(command);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Test task");
        verify(notificationService).notifyTaskCreated(task);
        verify(eventPublisher).publishEvent(any(TaskCreatedEvent.class));
    }
}
```

### 整合測試
```java
@SpringBootTest
@Transactional
class WorkflowApplicationServiceIntegrationTest {
    @Autowired
    private WorkflowApplicationService workflowApplicationService;
    
    @Test
    void createProjectWithDefaultTasks_whenValidCommand_thenCreateProjectAndTasks() {
        // Given
        CreateProjectWithTasksCommand command = new CreateProjectWithTasksCommand(
            "Test Project",
            "Test Description",
            UserId.generate(),
            Arrays.asList("Task 1", "Task 2", "Task 3")
        );
        
        // When
        ProjectSummaryDto result = workflowApplicationService.createProjectWithDefaultTasks(command);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Project");
        assertThat(result.getTaskCount()).isEqualTo(3);
    }
}
```

## ✅ 應用層檢查清單
- [ ] **事務邊界**: 適當的事務邊界設定
- [ ] **Use Case 編排**: 正確編排多個領域服務
- [ ] **錯誤處理**: 統一的錯誤處理策略
- [ ] **安全性檢查**: 應用層級的安全性驗證
- [ ] **事件發布**: 適當的領域事件發布
- [ ] **數據轉換**: 正確的 DTO 轉換邏輯
- [ ] **效能最佳化**: 查詢和快取策略最佳化
- [ ] **測試覆蓋**: 充分的單元測試和整合測試