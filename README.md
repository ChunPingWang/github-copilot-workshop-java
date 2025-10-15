# GitHub Copilot Workshop - Java Edition

歡迎來到 GitHub Copilot Java 工作坊！本工作坊將透過實際的程式開發練習，帶領您深入了解 GitHub Copilot 在 Java 開發中的強大功能，並建立現代化的軟體開發最佳實務。

## 🎯 工作坊目標

透過 TaskList Kata 這個經典的程式練習，我們將從簡單的任務管理應用開始，逐步演進為具備企業級特性的多租戶 Web 應用程式。在這個過程中，您將學會如何有效運用 GitHub Copilot 來提升開發效率與程式碼品質。

## 📚 課程大綱

### 1. GitHub Copilot 基礎介紹

- **Copilot 核心功能**
- **有效使用技巧**

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