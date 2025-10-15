# GitHub Copilot Workshop - Java Edition

歡迎來到 GitHub Copilot Java 工作坊！本工作坊將透過實際的程式開發練習，帶領您深入了解 GitHub Copilot 在 Java 開發中的強大功能，並建立現代化的軟體開發最佳實務。

## 🎯 工作坊目標

透過 TaskList Kata 這個經典的程式練習，我們將從簡單的任務管理應用開始，逐步演進為具備企業級特性的多租戶 Web 應用程式。在這個過程中，您將學會如何有效運用 GitHub Copilot 來提升開發效率與程式碼品質。

## 📚 課程大綱

### 1. GitHub Copilot 基礎介紹

#### (1) **輸入框區域**
- **📎 Add Context...** 按鈕：位於左上角，用於添加上下文信息到對話中

#### (2) **AI 模型選擇器**

##### 代理模式選擇 (Agent ▼)：
- **Agent**：標準的 GitHub Copilot 代理模式，提供一般性的程式開發協助
- **Edit**：編輯模式，專門用於直接修改和編輯程式碼，會直接在檔案中進行變更
- **Ask**：問答模式，純粹的對話模式，不會直接修改程式碼，適合諮詢和學習

##### 模式使用場景：
- **Agent**：平衡的綜合模式，可以回答問題也可以執行操作
- **Edit**：當你需要直接修改檔案內容時使用（重構、修復、新增功能）
- **Ask**：當你只是想了解概念、獲得建議或學習時使用（不修改檔案）
- **@workspace**：當需要理解整個專案結構或跨檔案操作時使用
- **@vscode**：當需要 VS Code 編輯器功能協助時使用

##### AI 模型選擇 (Claude Sonnet 4 ▼)：
- **Claude Sonnet 4**：Anthropic 的最新模型，擅長複雜推理和長文本理解
- **Claude Sonnet 3.5**：平衡效能與速度的選擇，適合一般開發任務
- **GPT-4o**：OpenAI 的多模態模型，支援文字、圖片、音訊處理
- **GPT-4o mini**：輕量化版本，回應速度更快，適合簡單查詢
- **GPT-4 Turbo**：高效能模型，適合複雜的程式設計任務
- **o1-preview**：OpenAI 的推理模型，擅長邏輯思考和問題解決
- **o1-mini**：推理模型的輕量版，適合快速邏輯分析

##### 模型選擇建議：
- **複雜重構/架構設計**：Claude Sonnet 4 或 o1-preview
- **一般程式開發**：Claude Sonnet 3.5 或 GPT-4 Turbo
- **快速問答/簡單修復**：GPT-4o mini 或 Claude Sonnet 3.5
- **多媒體處理**：GPT-4o（支援圖片分析）
- **邏輯推理/演算法**：o1-preview 或 o1-mini
- **工作區相關操作**：搭配 @workspace 代理使用任何模型

#### 3. **提示文字**
- **"Add context (#), extensions (@), commands (/)"**：說明了三種添加內容的方式
  - `#` - 添加文件或代碼上下文
  - `@` - 引用擴展功能
  - `/` - 執行特定命令

##### Add Context (#) 範例：
```
# 引用特定檔案
#file:src/main.py

# 引用資料夾
#folder:src/components

# 引用選取的程式碼片段
#selection

# 引用目前開啟的檔案
#editor

# 引用終端機內容
#terminalLastCommand
#terminalSelection

# 引用 Git 相關
#git
#gitChangedFiles
```

##### Extensions (@) 範例：
```
# GitHub Copilot 相關
@github - GitHub 整合功能
@workspace - 工作區相關操作

# 開發工具
@vscode - VS Code 功能
@terminal - 終端機操作

# 程式語言支援
@python - Python 相關功能
@javascript - JavaScript 相關功能
@java - Java 相關功能
@typescript - TypeScript 相關功能
```

##### Commands (/) 範例：
```
# 程式碼相關
/explain - 解釋選取的程式碼
/fix - 修復程式碼問題
/optimize - 優化程式碼

# 測試相關
/tests - 產生測試程式碼
/test - 執行測試

# 文件相關
/doc - 產生文件
/docs - 產生說明文件

# 專案相關
/new - 建立新專案
/newNotebook - 建立新的 Jupyter Notebook

# Git 相關
/git - Git 操作相關命令

# 自訂 Prompts（如果有設定）
/git_init - Git 初始化 prompt
```

##### 實際使用範例：
```
# 結合使用範例
#file:package.json @workspace /explain
# 意思：引用 package.json 檔案，使用工作區功能，解釋內容

#selection @github /fix
# 意思：引用選取的程式碼，使用 GitHub 功能，修復問題

#folder:src @python /tests
# 意思：引用 src 資料夾，使用 Python 功能，產生測試
```

#### 4. **右側操作按鈕**
- **🔧 設置圖標**：可能用於配置或設置選項
- **▷ 發送按鈕**：用於發送消息或執行命令
- **▼ 下拉菜單**：可能包含更多選項或功能

#### 5. **整體設計特點**
- 深色主題界面
- 圓角邊框設計
- 現代化的 UI 風格
- 清晰的功能分區

這個界面是 GitHub Copilot 在 VS Code 中的聊天功能界面，讓用戶可以與 AI 助手進行交互，獲取編程幫助和代碼建議。

### Prompt：堅持不要自己寫 Prompt

#### 新增初始化 git 的 prompt

1. **VS Code 右上角，齒輪處點下新增**

   ![截圖 2025-10-05 晚上11.38.40](https://hackmd.io/_uploads/H1red1-pll.png)

2. **正上方是點下新增一個 prompt 檔**

   ![截圖 2025-10-05 晚上11.39.01](https://hackmd.io/_uploads/rJIzuyWplx.png)

3. **點選你要存放的位置，選擇第一個，存放在 .github/prompts 下**

   *另外一個選項，使用者資料夾後面再說*

   ![截圖 2025-10-05 晚上11.39.12](https://hackmd.io/_uploads/B1Emu1WTll.png)

4. **空的 prompt 檔產生**

   ![截圖 2025-10-05 晚上11.40.02](https://hackmd.io/_uploads/HJy4u1Z6xx.png)

5. **查看檔案儲存位置**

   ![截圖 2025-10-05 晚上11.58.51](https://hackmd.io/_uploads/HJ-p_JWTel.png)

6. **魔法打敗魔法，自己不想寫，請它寫**

   ```
   你是一個擅長使用Git 的專家，撰寫 git init 的 promot，並用繁體中文輸出
   ```

7. **結果內容如下圖**

   ![截圖 2025-10-06 凌晨12.00.30](https://hackmd.io/_uploads/H1SXFkbpee.png)

8. **試試看，在右下角視窗輸入 / ，就會出現你剛剛寫的 git_init**

   ![截圖 2025-10-05 晚上11.44.19](https://hackmd.io/_uploads/Sy5NOJZpge.png)


### 2. TaskList Kata 基礎實作

#### 專案結構分析

```
github-copilot-workshop-java/
├── README.md                 # 工作坊指南文件
└── src/
    ├── main/java/com/codurance/training/tasks/
    │   ├── Task.java         # 任務實體類別
    │   └── TaskList.java     # 主程式與任務管理邏輯
    └── test/java/            # 測試目錄（待建立）
```

#### 核心元件介紹

##### Task.java - 任務實體類別
```java
public final class Task {
    private final long id;           // 唯一識別碼（不可變）
    private final String description; // 任務描述（不可變）
    private boolean done;            // 完成狀態（可變）
}
```

**設計特點：**
- 使用 `final` 關鍵字確保類別不可被繼承
- ID 和描述為不可變屬性，符合實體物件設計原則
- 僅完成狀態可以修改，反映任務狀態變化的業務需求

##### TaskList.java - 主程式與業務邏輯
這是一個命令列應用程式，實作了完整的任務管理系統：

**資料結構：**
```java
private final Map<String, List<Task>> tasks = new LinkedHashMap<>();
```
- 使用 `LinkedHashMap` 保持專案插入順序
- 每個專案對應一個任務清單

**支援指令：**

- **專案管理**
   - `add project <專案名稱>` - 建立新專案
   ```
   > add project 學習計畫
   ```

- **任務管理**
   - `add task <專案名稱> <任務描述>` - 新增任務到指定專案
   ```
   > add task 學習計畫 完成 Java 基礎學習
   ```
   - `check <任務ID>` - 標記任務為已完成
   ```
   > check 1
   ```
   - `uncheck <任務ID>` - 標記任務為未完成
   ```
   > uncheck 1
   ```

- **檢視與說明**
   - `show` - 顯示所有專案和任務狀態
   ```
   學習計畫
       [x] 1: 完成 Java 基礎學習
       [ ] 2: 學習 Spring Boot
   ```
   - `help` - 顯示所有可用指令
   - `quit` - 退出程式

#### 程式執行流程

- **啟動程式**
   ```bash
   java com.codurance.training.tasks.TaskList
   ```

- **互動式命令列界面**
   - 程式持續等待使用者輸入
   - 解析並執行指令
   - 顯示執行結果或錯誤訊息

- **指令處理機制**
   - 使用 `split()` 方法解析指令與參數
   - `switch` 語句路由到對應的處理方法
   - 錯誤處理與使用者友善的回饋訊息

#### 目前實作的優點

- ✅ **清晰的職責分離**：Task 專注於資料，TaskList 處理業務邏輯
- ✅ **直觀的使用者介面**：命令列操作簡單易懂
- ✅ **良好的錯誤處理**：提供清楚的錯誤訊息
- ✅ **擴充性考量**：ID 生成機制支援大量任務

#### 待改進的地方

- ❌ **缺少建構管理工具**：需要 Maven 或 Gradle
- ❌ **無單元測試**：缺乏測試覆蓋率
- ❌ **記憶體儲存**：資料不會持久化
- ❌ **單一職責原則**：TaskList 類別承擔太多責任
- ❌ **硬編碼依賴**：直接使用 System.in/out

### 3. 建立基本的開發規範

- **程式碼規範**

- **Copilot 協助規範制定**

```
#createDirectory .github
#createFile .github/copilot-instructions.md
建立開發規範, 寫入.github/copilot-instructions.md 
條件如下：
開發語言版本： JDK 17 
測試框架：JUnit 5, Mockito
套件管理：Gradle
```

### 4. 測試案例建立

- **Copilot 測試生成**
```
加入單元測試，包含正反向測試條件
```

### 5. 重構成六角形架構或三層式架構

- **架構模式選擇**
  
  #### 六角形架構（Hexagonal Architecture）
  ```
  產生六角形架構的說明與圖解，寫入 README.md的'5. 重構成六角形架構或三層式架構'下面的六角形架構下
  ```
  六角形架構，也稱為**埠與介面卡架構（Ports and Adapters）**，是由 Alistair Cockburn 提出的軟體設計模式。這種架構模式的核心理念是將業務邏輯與外部依賴完全隔離，使應用程式更易於測試、維護和擴展。
  
  ##### 🏗️ 架構圖解
  
  ```
                    ┌─────────────────────────────────────┐
                    │          外部世界 (External)          │
                    └─────────────────────────────────────┘
                                        │
                    ┌─────────────────────────────────────┐
                    │              UI Layer               │
                    │    ┌─────────────────────────────┐  │
                    │    │     Primary Adapters        │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │   CLI   │ │  REST API   │ │  │
                    │    │  │ Adapter │ │   Adapter   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        │
                    ┌─────────────────────────────────────┐
                    │             Application             │
                    │    ┌─────────────────────────────┐  │
                    │    │        Primary Ports        │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Service │ │   Service   │ │  │
                    │    │  │  Port   │ │    Port     │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │         Domain Core         │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Entity  │ │   Entity    │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Service │ │   Service   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │       Secondary Ports       │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │Repository│ │ Repository  │ │  │
                    │    │  │  Port   │ │    Port     │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        │
                    ┌─────────────────────────────────────┐
                    │          Infrastructure             │
                    │    ┌─────────────────────────────┐  │
                    │    │      Secondary Adapters     │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │   JPA   │ │   Memory    │ │  │
                    │    │  │ Adapter │ │   Adapter   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        │
                    ┌─────────────────────────────────────┐
                    │            External                 │
                    │   ┌─────────────┐ ┌───────────────┐ │
                    │   │  Database   │ │  File System  │ │
                    │   └─────────────┘ └───────────────┘ │
                    └─────────────────────────────────────┘
  ```
  
  ##### 🔧 核心概念說明
  
  **1. Domain Core（領域核心）**
  - **Business Entities**: `Task`, `Project` 等業務實體
  - **Domain Services**: 核心業務邏輯，不依賴外部系統
  - **Value Objects**: 不可變的值物件
  - **Domain Events**: 領域事件（如任務完成事件）
  
  **2. Ports（埠）**
  - **Primary Ports**: 驅動埠，定義應用程式對外提供的介面
    ```java
    public interface TaskService {
        Task createTask(CreateTaskCommand command);
        void markTaskAsCompleted(TaskId taskId);
        List<Task> findTasksByProject(ProjectId projectId);
    }
    ```
  - **Secondary Ports**: 被驅動埠，定義應用程式對外部系統的需求
    ```java
    public interface TaskRepository {
        void save(Task task);
        Optional<Task> findById(TaskId id);
        List<Task> findByProjectId(ProjectId projectId);
    }
    ```
  
  **3. Adapters（介面卡）**
  - **Primary Adapters**: 將外部請求轉換為內部呼叫
    - CLI Adapter: 處理命令列輸入
    - REST API Adapter: 處理 HTTP 請求
    - Web UI Adapter: 處理網頁介面
  
  - **Secondary Adapters**: 實作 Secondary Ports，連接外部系統
    - JPA Repository Adapter: 資料庫操作
    - File System Adapter: 檔案操作
    - Email Adapter: 郵件發送
  
  ##### 📦 TaskList 專案套件結構
  
  ```
  com.codurance.training.tasks/
  ├── domain/                    # 領域核心
  │   ├── model/                 # 領域模型
  │   │   ├── Task.java         # 任務實體
  │   │   ├── Project.java      # 專案實體
  │   │   ├── TaskId.java       # 任務 ID 值物件
  │   │   └── ProjectId.java    # 專案 ID 值物件
  │   ├── service/              # 領域服務
  │   │   ├── TaskService.java  # 任務領域服務
  │   │   └── ProjectService.java # 專案領域服務
  │   └── repository/           # Repository 介面（Secondary Ports）
  │       ├── TaskRepository.java
  │       └── ProjectRepository.java
  │
  ├── application/              # 應用服務層
  │   ├── port/                 # Primary Ports
  │   │   ├── TaskManagementUseCase.java
  │   │   └── ProjectManagementUseCase.java
  │   ├── service/              # 應用服務實作
  │   │   ├── TaskApplicationService.java
  │   │   └── ProjectApplicationService.java
  │   └── command/              # 命令物件
  │       ├── CreateTaskCommand.java
  │       └── CreateProjectCommand.java
  │
  ├── adapter/                  # 介面卡層
  │   ├── primary/              # Primary Adapters
  │   │   ├── cli/              # 命令列介面卡
  │   │   │   └── CLIAdapter.java
  │   │   └── rest/             # REST API 介面卡
  │   │       ├── TaskController.java
  │   │       └── ProjectController.java
  │   └── secondary/            # Secondary Adapters
  │       ├── persistence/      # 持久化介面卡
  │       │   ├── JpaTaskRepository.java
  │       │   └── MemoryTaskRepository.java
  │       └── notification/     # 通知介面卡
  │           └── EmailNotificationAdapter.java
  │
  └── configuration/            # 配置與依賴注入
      └── ApplicationConfiguration.java
  ```
  
  ##### 🎯 六角形架構的優勢
  
  **1. 可測試性（Testability）**
  - 業務邏輯與外部依賴完全分離
  - 可以使用 Mock 物件輕鬆進行單元測試
  - 測試不依賴資料庫或外部服務
  
  **2. 可維護性（Maintainability）**
  - 清晰的層次結構和職責分離
  - 變更外部系統不影響核心業務邏輯
  - 易於理解和修改
  
  **3. 可擴展性（Scalability）**
  - 新增功能只需實作新的 Port 和 Adapter
  - 支援多種介面（CLI、REST、GraphQL）
  - 易於整合新的外部系統
  
  **4. 技術無關性（Technology Agnostic）**
  - 核心業務邏輯不依賴特定技術
  - 可以輕鬆切換資料庫、框架或協議
  - 延遲技術決策到最後時刻
  
  ##### 🚀 實作步驟
  
  **步驟 1: 識別核心領域**
  ```java
  // 領域實體
  public class Task {
      private final TaskId id;
      private final String description;
      private TaskStatus status;
      
      // 業務方法
      public void markAsCompleted() {
          this.status = TaskStatus.COMPLETED;
          // 可能發布領域事件
      }
  }
  ```
  
  **步驟 2: 定義 Ports**
  ```java
  // Primary Port
  public interface TaskManagementUseCase {
      TaskDto createTask(CreateTaskCommand command);
      void completeTask(TaskId taskId);
  }
  
  // Secondary Port
  public interface TaskRepository {
      void save(Task task);
      Optional<Task> findById(TaskId id);
  }
  ```
  
  **步驟 3: 實作應用服務**
  ```java
  @Service
  public class TaskApplicationService implements TaskManagementUseCase {
      private final TaskRepository taskRepository;
      
      @Override
      public TaskDto createTask(CreateTaskCommand command) {
          Task task = new Task(command.getDescription());
          taskRepository.save(task);
          return TaskDto.from(task);
      }
  }
  ```
  
  **步驟 4: 建立 Adapters**
  ```java
  // Primary Adapter
  @RestController
  public class TaskController {
      private final TaskManagementUseCase taskUseCase;
      
      @PostMapping("/tasks")
      public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest request) {
          CreateTaskCommand command = new CreateTaskCommand(request.getDescription());
          TaskDto task = taskUseCase.createTask(command);
          return ResponseEntity.ok(task);
      }
  }
  
  // Secondary Adapter
  @Repository
  public class JpaTaskRepository implements TaskRepository {
      private final TaskJpaRepository jpaRepository;
      
      @Override
      public void save(Task task) {
          TaskEntity entity = TaskEntity.from(task);
          jpaRepository.save(entity);
      }
  }
  ```
  
  ##### 📝 Copilot 輔助重構提示
  
  ```
  將現有的 TaskList 類別重構為六角形架構：
  1. 提取 Task 實體的業務邏輯
  2. 定義 TaskRepository 介面
  3. 建立 TaskService 應用服務
  4. 分離 CLI 介面卡
  5. 實作記憶體版本的 Repository
  ```
  
  - 三層式架構（Three-tier Architecture）
  ```
  產生三層式架構的說明與圖解，寫入 README.md的'5. 重構成六角形架構或三層式架構'下面的三層式架構下
  ```
  ```
  修正三層式架構的展示層與資料存取層，需向業務邏輯層相依，同時修正範例程式
  ```
  三層式架構是軟體工程中最經典且廣泛使用的架構模式之一。它將應用程式分為三個邏輯層次：**展示層（Presentation Layer）**、**業務邏輯層（Business Logic Layer）** 和 **資料存取層（Data Access Layer）**。這種分層方式促進了關注點分離、提高了程式碼的可維護性和可擴展性。
  
  ##### 🏗️ 架構圖解
  
  ```
                    ┌─────────────────────────────────────┐
                    │              客戶端                  │
                    │   ┌─────────┐ ┌─────────┐ ┌────────┐ │
                    │   │   Web   │ │  Mobile │ │  CLI   │ │
                    │   │ Browser │ │   App   │ │ Client │ │
                    │   └─────────┘ └─────────┘ └────────┘ │
                    └─────────────────────────────────────┘
                                        │ HTTP/API 請求
                                        ▼
                    ┌─────────────────────────────────────┐
                    │           展示層 (Presentation)      │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │         Controllers         │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │Controller│ │ Controller  │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │        Request/Response     │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │Request  │ │ Response    │ │  │
                    │    │  │  DTOs   │ │   DTOs      │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │         Validators          │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Input  │ │   Output    │ │  │
                    │    │  │Validator│ │  Formatter  │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        │ 依賴業務邏輯層
                                        │ (單向依賴)
                                        ▼
                    ┌─────────────────────────────────────┐
                    │        業務邏輯層 (Business Logic)   │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │      Service Interfaces     │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Service │ │   Service   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │       Domain Models         │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Entity  │ │   Entity    │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │      Business Rules         │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │ Manager │ │   Manager   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │    Repository Interfaces    │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │Repository│ │ Repository  │ │  │
                    │    │  │Interface │ │ Interface   │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        ▲ 被資料存取層依賴
                                        │ (實作介面)
                                        │
                    ┌─────────────────────────────────────┐
                    │         資料存取層 (Data Access)     │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │    Repository Implementations│  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │  Task   │ │   Project   │ │  │
                    │    │  │Repository│ │ Repository  │ │  │
                    │    │  │  Impl    │ │    Impl     │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │         Data Entities       │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │ Task    │ │ Project     │ │  │
                    │    │  │ Entity  │ │ Entity      │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    │                                     │
                    │    ┌─────────────────────────────┐  │
                    │    │       Data Mappers          │  │
                    │    │  ┌─────────┐ ┌─────────────┐ │  │
                    │    │  │ Domain  │ │   Entity    │ │  │
                    │    │  │ Mapper  │ │   Mapper    │ │  │
                    │    │  └─────────┘ └─────────────┘ │  │
                    │    └─────────────────────────────┘  │
                    └─────────────────────────────────────┘
                                        │ 資料庫操作
                                        ▼
                    ┌─────────────────────────────────────┐
                    │              資料庫                  │
                    │   ┌─────────┐ ┌─────────┐ ┌────────┐ │
                    │   │  MySQL  │ │PostgreSQL│ │  H2    │ │
                    │   │Database │ │ Database │ │Database │ │
                    │   └─────────┘ └─────────┘ └────────┘ │
                    └─────────────────────────────────────┘
  ```
  
  ##### � 架構特色

  | 層級 | 職責 | 技術實作 | 依賴方向 |
  |------|------|----------|----------|
  | **展示層** | HTTP請求處理、輸入驗證、回應格式化 | Spring MVC, REST Controllers | ↓ 依賴業務邏輯層 |
  | **業務邏輯層** | 商業規則、領域邏輯、流程控制 | Service Classes, Domain Models | ↓ 定義資料介面 |
  | **資料存取層** | 資料持久化、查詢操作、資料轉換 | JPA, MyBatis, JDBC | ↑ 實作介面，向上依賴 |

  **依賴原則**：
  - ✅ **展示層** → **業務邏輯層**：展示層依賴並使用業務邏輯層的服務
  - ✅ **業務邏輯層** → **資料存取層介面**：業務邏輯層定義資料操作介面
  - ✅ **資料存取層** → **業務邏輯層介面**：資料存取層實作業務邏輯層定義的介面
  - ❌ **展示層** ↔ **資料存取層**：這兩層不應該直接相依
  
  ##### �📋 各層職責說明
  
  **1. 展示層（Presentation Layer）**
  
  展示層負責處理使用者介面和使用者互動，是使用者與系統的接觸點。
  
  **主要職責：**
  - 接收和驗證使用者輸入
  - 格式化和展示輸出資料
  - 處理使用者介面邏輯
  - 路由和控制請求流程
  
  **包含元件：**
  - **Controllers**: 處理 HTTP 請求，協調業務邏輯層
  - **DTOs (Data Transfer Objects)**: 資料傳輸物件，用於層間資料交換
  - **Validators**: 輸入驗證和資料格式檢查
  - **Formatters**: 輸出格式化和資料轉換
  
  ```java
  // Controller 範例（只依賴業務邏輯層）
  @RestController
  @RequestMapping("/api/tasks")
  public class TaskController {
      private final TaskService taskService;  // 只依賴業務邏輯層服務
      
      // 建構子注入，不直接依賴資料存取層
      public TaskController(TaskService taskService) {
          this.taskService = taskService;
      }
      
      @PostMapping
      public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
          // 委託給業務邏輯層處理
          TaskDTO task = taskService.createTask(request.toCommand());
          return ResponseEntity.status(HttpStatus.CREATED).body(task);
      }
      
      @GetMapping("/{id}")
      public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
          // 委託給業務邏輯層處理
          TaskDTO task = taskService.findTaskById(id);
          return ResponseEntity.ok(task);
      }
      
      @PutMapping("/{id}/complete")
      public ResponseEntity<Void> completeTask(@PathVariable Long id) {
          // 委託給業務邏輯層處理
          taskService.completeTask(id);
          return ResponseEntity.ok().build();
      }
  }
  
  // DTO 範例（資料傳輸物件）
  public class TaskDTO {
      private Long id;
      private String description;
      private boolean completed;
      private LocalDateTime createdAt;
      
      // 靜態工廠方法，避免在 Controller 中直接操作 Domain Model
      public static TaskDTO from(Task domainTask) {
          return new TaskDTO(
              domainTask.getId(),
              domainTask.getDescription(),
              domainTask.isCompleted(),
              domainTask.getCreatedAt()
          );
      }
      
      // constructors, getters, setters
  }
  ```
  
  **2. 業務邏輯層（Business Logic Layer）**
  
  業務邏輯層包含應用程式的核心業務規則和邏輯，是系統的核心。
  
  **主要職責：**
  - 實作業務規則和邏輯
  - 協調資料存取層操作
  - 處理業務異常和驗證
  - 管理交易和一致性
  
  **包含元件：**
  - **Services**: 業務服務，實作具體的業務邏輯
  - **Domain Models**: 領域模型，包含業務實體和行為
  - **Business Rules**: 業務規則引擎和驗證邏輯
  - **Repository Interfaces**: 定義資料存取介面（由資料存取層實作）
  
  ```java
  // Repository Interface（由業務邏輯層定義）
  public interface TaskRepository {
      Task save(Task task);
      Optional<Task> findById(Long id);
      List<Task> findByProjectId(Long projectId);
      void deleteById(Long id);
  }
  
  // Service 範例（依賴介面而非實作）
  @Service
  @Transactional
  public class TaskService {
      private final TaskRepository taskRepository;      // 依賴介面
      private final ProjectRepository projectRepository; // 依賴介面
      
      // 建構子注入（依賴注入容器會提供實作）
      public TaskService(TaskRepository taskRepository, 
                        ProjectRepository projectRepository) {
          this.taskRepository = taskRepository;
          this.projectRepository = projectRepository;
      }
      
      public TaskDTO createTask(CreateTaskCommand command) {
          // 業務邏輯驗證
          Project project = projectRepository.findById(command.getProjectId())
              .orElseThrow(() -> new ProjectNotFoundException(command.getProjectId()));
          
          // 業務規則檢查
          if (project.isCompleted()) {
              throw new BusinessException("Cannot add tasks to completed project");
          }
          
          // 建立實體
          Task task = Task.builder()
              .description(command.getDescription())
              .project(project)
              .status(TaskStatus.PENDING)
              .createdAt(LocalDateTime.now())
              .build();
          
          // 儲存實體
          Task savedTask = taskRepository.save(task);
          
          // 轉換為 DTO
          return TaskDTO.from(savedTask);
      }
      
      public void completeTask(Long taskId) {
          Task task = taskRepository.findById(taskId)
              .orElseThrow(() -> new TaskNotFoundException(taskId));
          
          task.markAsCompleted();
          taskRepository.save(task);
          
          // 可能觸發其他業務邏輯
          if (task.getProject().allTasksCompleted()) {
              projectService.markProjectAsCompleted(task.getProject().getId());
          }
      }
  }
  
  // Domain Model 範例
  @Entity
  public class Task {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      
      @Column(nullable = false)
      private String description;
      
      @Enumerated(EnumType.STRING)
      private TaskStatus status;
      
      @ManyToOne(fetch = FetchType.LAZY)
      private Project project;
      
      @CreationTimestamp
      private LocalDateTime createdAt;
      
      // 業務方法
      public void markAsCompleted() {
          if (this.status == TaskStatus.COMPLETED) {
              throw new BusinessException("Task is already completed");
          }
          this.status = TaskStatus.COMPLETED;
      }
      
      public boolean isCompleted() {
          return this.status == TaskStatus.COMPLETED;
      }
  }
  ```
  
  **3. 資料存取層（Data Access Layer）**
  
  資料存取層負責與資料儲存系統的互動，提供資料持久化功能。
  
  **主要職責：**
  - 與資料庫互動
  - 資料持久化和檢索
  - 查詢最佳化
  - 資料一致性維護
  
  **包含元件：**
  - **Repository Implementations**: 實作業務邏輯層定義的 Repository 介面
  - **DAOs (Data Access Objects)**: 具體的資料存取實作
  - **Mappers**: 實體與資料庫記錄之間的轉換
  - **Connection Management**: 資料庫連線管理
  
  ```java
  // Repository Implementation（實作業務邏輯層定義的介面）
  @Repository
  public class JpaTaskRepository implements TaskRepository {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Override
      public Task save(Task task) {
          if (task.getId() == null) {
              entityManager.persist(task);
              return task;
          } else {
              return entityManager.merge(task);
          }
      }
      
      @Override
      public Optional<Task> findById(Long id) {
          Task task = entityManager.find(Task.class, id);
          return Optional.ofNullable(task);
      }
      
      @Override
      public List<Task> findByProjectId(Long projectId) {
          return entityManager
              .createQuery("SELECT t FROM Task t WHERE t.project.id = :projectId", Task.class)
              .setParameter("projectId", projectId)
              .getResultList();
      }
      
      @Override
      public void deleteById(Long id) {
          Task task = entityManager.find(Task.class, id);
          if (task != null) {
              entityManager.remove(task);
          }
      }
  }
  
  // 或使用 Spring Data JPA 自動實作
  @Repository
  public interface SpringDataTaskRepository extends JpaRepository<TaskEntity, Long>, TaskRepository {
      // Spring Data JPA 會自動實作基本的 CRUD 操作
      // 只需要定義額外的查詢方法
  @Repository
  public class TaskRepositoryImpl implements TaskRepositoryCustom {
      
      @PersistenceContext
      private EntityManager entityManager;
      
      @Override
      public List<Task> findTasksWithComplexCriteria(TaskSearchCriteria criteria) {
          CriteriaBuilder cb = entityManager.getCriteriaBuilder();
          CriteriaQuery<Task> query = cb.createQuery(Task.class);
          Root<Task> task = query.from(Task.class);
          
          List<Predicate> predicates = new ArrayList<>();
          
          if (criteria.getProjectId() != null) {
              predicates.add(cb.equal(task.get("project").get("id"), criteria.getProjectId()));
          }
          
          if (criteria.getStatus() != null) {
              predicates.add(cb.equal(task.get("status"), criteria.getStatus()));
          }
          
          query.where(predicates.toArray(new Predicate[0]));
          return entityManager.createQuery(query).getResultList();
      }
  }
  ```
  
  ##### ⚙️ 依賴注入配置
  
  正確的三層式架構需要透過依賴注入來實現層間的解耦：
  
  ```java
  // Spring Boot 應用程式配置
  @SpringBootApplication
  public class TaskListApplication {
      public static void main(String[] args) {
          SpringApplication.run(TaskListApplication.class, args);
      }
  }
  
  // 依賴注入配置（Spring Boot 自動配置）
  @Configuration
  public class ApplicationConfig {
      
      // 業務邏輯層依賴資料存取層介面（由 Spring 自動注入實作）
      @Bean
      public TaskService taskService(TaskRepository taskRepository, 
                                    ProjectRepository projectRepository) {
          return new TaskService(taskRepository, projectRepository);
      }
      
      // 展示層依賴業務邏輯層（由 Spring 自動注入）
      @Bean
      public TaskController taskController(TaskService taskService) {
          return new TaskController(taskService);
      }
  }
  
  // 或使用註解方式（推薦）
  @Service
  public class TaskService {
      private final TaskRepository taskRepository;  // 介面依賴
      
      // Spring 會自動注入 TaskRepository 的實作
      public TaskService(TaskRepository taskRepository) {
          this.taskRepository = taskRepository;
      }
  }
  
  @RestController
  public class TaskController {
      private final TaskService taskService;  // 業務邏輯層依賴
      
      // Spring 會自動注入 TaskService
      public TaskController(TaskService taskService) {
          this.taskService = taskService;
      }
  }
  ```
  
  **依賴注入的好處**：
  - ✅ **解耦合**：層間透過介面依賴，不依賴具體實作
  - ✅ **可測試性**：容易進行單元測試和模擬（Mock）
  - ✅ **可維護性**：修改實作不影響其他層
  - ✅ **可擴展性**：容易替換不同的實作（如資料庫切換）
  
  ##### 📦 TaskList 專案套件結構
  
  ```
  com.codurance.training.tasks/
  ├── presentation/              # 展示層
  │   ├── controller/            # REST Controllers
  │   │   ├── TaskController.java
  │   │   └── ProjectController.java
  │   ├── dto/                   # 資料傳輸物件
  │   │   ├── request/           # 請求 DTOs
  │   │   │   ├── CreateTaskRequest.java
  │   │   │   └── UpdateTaskRequest.java
  │   │   └── response/          # 回應 DTOs
  │   │       ├── TaskDTO.java
  │   │       └── ProjectDTO.java
  │   ├── validator/             # 輸入驗證
  │   │   ├── TaskValidator.java
  │   │   └── ProjectValidator.java
  │   └── exception/             # 異常處理
  │       ├── GlobalExceptionHandler.java
  │       └── ApiErrorResponse.java
  │
  ├── business/                  # 業務邏輯層
  │   ├── service/               # 業務服務
  │   │   ├── TaskService.java
  │   │   ├── ProjectService.java
  │   │   └── TaskManagementService.java
  │   ├── model/                 # 領域模型
  │   │   ├── Task.java
  │   │   ├── Project.java
  │   │   └── TaskStatus.java
  │   ├── rule/                  # 業務規則
  │   │   ├── TaskBusinessRules.java
  │   │   └── ProjectBusinessRules.java
  │   └── exception/             # 業務異常
  │       ├── BusinessException.java
  │       ├── TaskNotFoundException.java
  │       └── ProjectNotFoundException.java
  │
  ├── dataaccess/                # 資料存取層
  │   ├── repository/            # 資料存取介面
  │   │   ├── TaskRepository.java
  │   │   ├── ProjectRepository.java
  │   │   └── custom/            # 自訂查詢
  │   │       ├── TaskRepositoryCustom.java
  │   │       └── TaskRepositoryImpl.java
  │   ├── entity/                # JPA 實體
  │   │   ├── TaskEntity.java
  │   │   └── ProjectEntity.java
  │   ├── mapper/                # 實體映射器
  │   │   ├── TaskMapper.java
  │   │   └── ProjectMapper.java
  │   └── config/                # 資料庫配置
  │       ├── DatabaseConfig.java
  │       └── JpaConfig.java
  │
  └── common/                    # 共用元件
      ├── config/                # 應用配置
      │   ├── ApplicationConfig.java
      │   └── WebConfig.java
      ├── constant/              # 常數定義
      │   └── ApplicationConstants.java
      └── util/                  # 工具類別
          ├── DateUtils.java
          └── ValidationUtils.java
  ```
  
  ##### 🎯 三層式架構的優勢
  
  **1. 關注點分離（Separation of Concerns）**
  - 每層都有明確的職責
  - 降低層間耦合度
  - 提高程式碼的可讀性
  
  **2. 可維護性（Maintainability）**
  - 變更影響局限在特定層次
  - 易於定位和修復問題
  - 支援團隊並行開發
  
  **3. 可測試性（Testability）**
  - 每層可以獨立測試
  - 支援 Mock 和 Stub 測試
  - 便於自動化測試
  
  **4. 可擴展性（Scalability）**
  - 每層可以獨立擴展
  - 支援水平和垂直擴展
  - 易於新增功能
  
  **5. 技術無關性（Technology Independence）**
  - 可以獨立選擇每層的技術
  - 易於技術升級和遷移
  - 支援多種資料庫和前端技術
  
  ##### 🚀 實作步驟
  
  **步驟 1: 重構展示層**
  ```java
  // 建立 Controller
  @RestController
  public class TaskController {
      private final TaskService taskService;
      
      @PostMapping("/tasks")
      public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequest request) {
          // 輸入驗證
          // 呼叫業務邏輯
          // 回傳結果
      }
  }
  ```
  
  **步驟 2: 抽取業務邏輯層**
  ```java
  // 建立 Service
  @Service
  public class TaskService {
      private final TaskRepository taskRepository;
      
      public TaskDTO createTask(CreateTaskCommand command) {
          // 業務邏輯實作
          // 資料驗證
          // 呼叫資料層
      }
  }
  ```
  
  **步驟 3: 建立資料存取層**
  ```java
  // 建立 Repository
  @Repository
  public interface TaskRepository extends JpaRepository<Task, Long> {
      List<Task> findByProjectId(Long projectId);
  }
  ```
  
  **步驟 4: 配置依賴注入**
  ```java
  @Configuration
  public class ApplicationConfig {
      // Bean 配置
      // 資料庫配置
      // 服務配置
  }
  ```
  
  ##### 📝 Copilot 輔助重構提示
  
  ```
  將現有的 TaskList 類別重構為三層式架構：
  1. 建立 TaskController 處理 HTTP 請求
  2. 抽取 TaskService 包含業務邏輯
  3. 建立 TaskRepository 處理資料存取
  4. 定義 TaskDTO 用於資料傳輸
  5. 實作異常處理和輸入驗證
  ```
  
  ##### ⚖️ 三層式架構 vs 六角形架構
  
  | 特性 | 三層式架構 | 六角形架構 |
  |------|-----------|-----------|
  | **複雜度** | 簡單易懂 | 相對複雜 |
  | **學習曲線** | 平緩 | 較陡峭 |
  | **適用場景** | 一般企業應用 | 複雜領域應用 |
  | **測試便利性** | 中等 | 優秀 |
  | **技術無關性** | 中等 | 優秀 |
  | **維護成本** | 中等 | 較低 |
  | **團隊要求** | 一般 | 較高 |

- **Copilot 輔助重構**
  - 程式碼結構
  - 設計模式推薦

```
將六角形架構要求，加入 #editFiles #file:copilot-instructions.md 
```
```
開始按照 #file:copilot-instructions.md  重構
Let's do it step by step.
```

#### 🎯 架構選擇建議與下一步

基於本工作坊的實際進展和我們已經完成的**六角形架構重構**，以下是具體的建議：

##### ✅ **當前成果總結**

我們已經成功完成了**六角形架構的完整實作**：

**🏗️ 已完成的架構層次**
- **Domain Layer**: Task、Project 實體與 TaskId、ProjectId 值物件
- **Service Layer**: TaskDomainService、ProjectDomainService 等業務服務  
- **Port Layer**: 清楚定義的 Inbound 和 Outbound Ports
- **Adapter Layer**: CLI 輸入適配器與 InMemory 持久化適配器
- **Configuration**: ApplicationConfig 依賴注入配置

**🧪 測試覆蓋狀況**
- ✅ **Value Objects**: ProjectId、Project 完整測試套件
- ✅ **Integration Tests**: ApplicationConfig 端對端測試
- ✅ **Domain Tests**: 基礎領域邏輯測試
- 📊 **測試覆蓋率**: 核心領域模型 80%+ 覆蓋

##### 🚀 **推薦的後續發展路徑**

**選項 1: 深化六角形架構實作** ⭐️ **強烈推薦**
```markdown
理由：已有完整基礎，可快速達成專業級標準
- 擴展 Repository 模式 (JPA + H2)
- 新增 REST API Adapter 
- 實作完整測試策略
- 導入 Spring Boot 框架整合
```

**選項 2: 平行實作三層式架構**
```markdown
理由：學習比較不同架構模式
- 建立獨立分支進行三層式實作
- 比較兩種架構的優缺點
- 適合團隊學習和技術選型參考
```

##### 📋 **具體實作步驟 (推薦路徑)**

**階段一：Repository 模式擴展** (2-3小時)
```
1. 新增 JPA Entity 模型
2. 實作 Spring Data Repository
3. 配置 H2 記憶體資料庫
4. 完成資料持久化測試
```

**階段二：Web API 開發** (2-3小時)  
```
1. 建立 REST Controller Adapter
2. 實作 DTO 轉換層
3. 新增 OpenAPI/Swagger 文件
4. API 整合測試
```

**階段三：企業級特性** (3-4小時)
```
1. Spring Security 整合
2. 異常處理機制
3. 日誌與監控
4. 部署與 CI/CD
```

##### 🎯 **GitHub Copilot 最佳實務**

**重構建議指令**
```
基於現有六角形架構，使用 GitHub Copilot 協助：

1. Repository 擴展：
   @workspace /fix 將 InMemoryRepository 轉換為 JPA Repository

2. API 開發：
   #file:TaskDomainService.java @github 生成對應的 REST Controller

3. 測試完善：
   #folder:src/test @workspace /tests 生成完整測試覆蓋

4. 文件生成：
   #file:README.md @vscode /doc 更新架構說明文件
```

**持續改進循環**
```
1. 使用 Copilot 生成基礎程式碼
2. 執行測試驗證功能正確性  
3. 重構優化程式碼品質
4. 更新文件與註解
5. 重複循環直到達成目標
```

##### 💡 **學習重點提醒**

- **架構思維**: 重點理解依賴反轉和關注點分離原則
- **測試驅動**: 先寫測試，再實作功能，確保程式碼品質
- **漸進式開發**: 每次只專注一個小功能，穩紮穩打
- **Copilot 協作**: 善用 AI 輔助，但要理解生成的程式碼邏輯

##### 🎉 **預期學習成果**

完成本工作坊後，您將具備：
- ✅ 六角形架構的深度理解與實作能力
- ✅ GitHub Copilot 在企業開發中的實戰經驗  
- ✅ Spring Boot 生態系統整合技能
- ✅ 現代軟體開發最佳實務經驗
- ✅ 可直接應用於專案的架構模板


### 6. Repository 模式實作

- **資料存取抽象化**
  - Repository 介面設計

- **Spring Data JPA 整合**
  - Entity 模型對應

- **資料庫設計**
  - H2 開發資料庫
  - PostgreSQL 生產環境

### 7. Web API 與 Swagger 整合

- **Spring Boot REST API**
  - Controller 層設計

- **API 文件化**
  - OpenAPI 3.0 規範
  - Swagger UI 整合
  - API 註解最佳實務
  - 範例與說明撰寫

- **API 測試**
  - Spring Boot Test 
  - 自動化 API 測試

- **Copilot API 開發**
  - RESTful endpoint 生成
  - 請求/回應 DTO 建立
  - 驗證邏輯實作

### 8. 多租戶應用改造(延伸題)

- **多租戶架構設計**
  - 租戶隔離策略
    - 資料庫隔離
    - Schema 隔離
    - 資料列隔離
  - 租戶識別機制
  - 安全性考量

- **實作策略**
  - Tenant Context 管理
  - 動態資料源切換
  - 租戶特定配置
  - 效能最佳化

- **Spring Security 整合**
  - JWT Token 驗證
  - 角色權限管理
  - 租戶權限控制
  - API 安全防護