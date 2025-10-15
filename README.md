# GitHub Copilot Workshop - Java Edition

歡迎來到 GitHub Copilot Java 工作坊！本工作坊將透過實際的程式開發練習，帶領您深入了解 GitHub Copilot 在 Java 開發中的強大功能，並建立現代化的軟體開發最佳實務。

## 🎯 工作坊目標

透過 TaskList Kata 這個經典的程式練習，我們將從簡單的任務管理應用開始，逐步演進為具備企業級特性的多租戶 Web 應用程式。在這個過程中，您將學會如何有效運用 GitHub Copilot 來提升開發效率與程式碼品質。

## 📚 課程大綱

### 1. GitHub Copilot 基礎介紹

#### A. **輸入框區域**
- **📎 Add Context...** 按鈕：位於左上角，用於添加上下文信息到對話中

#### B. **AI 模型選擇器**

##### 代理模式選擇 (Agent ▼)：
- **Agent**：標準的 GitHub Copilot 代理模式，提供一般性的程式開發協助
- **Edit**：編輯模式，專門用於直接修改和編輯程式碼，會直接在檔案中進行變更
- **Ask**：問答模式，純粹的對話模式，不會直接修改程式碼，適合諮詢和學習
- **@workspace**：工作區專用代理，專門針對當前工作區的程式碼結構和專案內容提供建議
- **@vscode**：VS Code 專用代理，專門協助 VS Code 相關操作、擴展開發和編輯器功能

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

#### C. **提示文字**
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

#### D. **右側操作按鈕**
- **🔧 設置圖標**：可能用於配置或設置選項
- **▷ 發送按鈕**：用於發送消息或執行命令
- **▼ 下拉菜單**：可能包含更多選項或功能

#### E. **整體設計特點**
- 深色主題界面
- 圓角邊框設計
- 現代化的 UI 風格
- 清晰的功能分區

這個界面是 GitHub Copilot 在 VS Code 中的聊天功能界面，讓用戶可以與 AI 助手進行交互，獲取編程幫助和代碼建議。

#### F. Prompt：絕對不要自己寫 Prompt

#####  新增初始化 git 的 prompt

- **VS Code 右上角，齒輪處點下新增**

   ![截圖 2025-10-05 晚上11.38.40](https://hackmd.io/_uploads/H1red1-pll.png)

- **正上方是點下新增一個 prompt 檔**

   ![截圖 2025-10-05 晚上11.39.01](https://hackmd.io/_uploads/rJIzuyWplx.png)

- **點選你要存放的位置，選擇第一個，存放在 .github/prompts 下**

   *另外一個選項，使用者資料夾後面再說*

   ![截圖 2025-10-05 晚上11.39.12](https://hackmd.io/_uploads/B1Emu1WTll.png)

- **空的 prompt 檔產生**

   ![截圖 2025-10-05 晚上11.40.02](https://hackmd.io/_uploads/HJy4u1Z6xx.png)

- **查看檔案儲存位置**

   ![截圖 2025-10-05 晚上11.58.51](https://hackmd.io/_uploads/HJ-p_JWTel.png)

- **魔法打敗魔法，自己不想寫，請它寫**

   ```
   你是一個擅長使用Git 的專家，撰寫 git init 的 promot，並用繁體中文輸出
   ```

- **結果內容如下圖**

   ![截圖 2025-10-06 凌晨12.00.30](https://hackmd.io/_uploads/H1SXFkbpee.png)

- **試試看，在右下角視窗輸入 / ，就會出現你剛剛寫的 git_init**

   ![截圖 2025-10-05 晚上11.44.19](https://hackmd.io/_uploads/Sy5NOJZpge.png)


### 2. TaskList Kata 基礎實作

- **專案結構建立**
- **核心功能開發**
  - `Task` 物件模型設計
  - `TaskList` 容器實作
  - 基本 CRUD 操作
  - 任務狀態管理

- **Copilot 輔助開發**

### 3. 建立基本的開發規範

- **程式碼規範**

- **Copilot 協助規範制定**

### 4. 測試案例建立

- **Copilot 測試生成**

### 5. 重構成六角形架構或三層式架構

- **架構模式選擇**
  - 六角形架構（Hexagonal Architecture）
    - 核心業務邏輯隔離
    - 介面卡模式實作
    - 依賴注入與控制反轉
  - 三層式架構（Three-tier Architecture）
    - 展示層（Presentation Layer）
    - 業務邏輯層（Business Logic Layer）
    - 資料存取層（Data Access Layer）

- **Copilot 輔助重構**
  - 程式碼結構
  - 設計模式推薦

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
