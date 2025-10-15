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

## 🧪 測試規範概述

本專案重視測試驅動開發，所有程式碼都應該有適當的測試覆蓋。

### 測試層級
- **單元測試**: 測試個別類別和方法
- **整合測試**: 測試元件之間的協作
- **端對端測試**: 測試完整的業務流程

### 基本要求
- **程式碼覆蓋率**: 最低 80%
- **測試架構**: Given-When-Then 模式
- **測試隔離**: 每個測試獨立運行
- **快速執行**: 單元測試應該快速完成

> 📋 **詳細測試規範**: 參考 `.github/instructions/test.instructions.md` 獲取完整的測試開發指南

## 🏗️ 六角形架構概述

本專案採用六角形架構模式，確保業務邏輯與外部依賴解耦。

### 核心原則
- **依賴倒置**: 所有依賴都指向核心業務邏輯
- **介面隔離**: 透過 Port 和 Adapter 模式分離關注點
- **可測試性**: 核心邏輯完全獨立於外部框架
- **技術無關性**: 業務邏輯不依賴具體技術實作

### 高階套件結構
```
com.codurance.training.tasks/
├── domain/                    # 領域層 (核心業務邏輯)
│   ├── model/                 # 領域實體和值物件
│   ├── port/                  # 埠介面定義
│   ├── service/               # 領域服務
│   └── exception/             # 領域異常
├── adapter/                   # 適配器層
│   ├── inbound/               # 輸入適配器 (Web, CLI)
│   └── outbound/              # 輸出適配器 (Database, Notification)
├── application/               # 應用程式層 (選用)
│   ├── service/               # 應用服務
│   ├── command/               # 應用命令
│   └── query/                 # 應用查詢
└── config/                    # 配置層
    └── ApplicationConfig.java
```

### 依賴方向檢查
- ✅ **Adapter → Domain**: 適配器可以依賴領域層
- ✅ **Domain → Port**: 領域層可以依賴自己定義的介面
- ❌ **Domain → Adapter**: 領域層絕不可依賴適配器
- ❌ **Domain → Framework**: 領域層不可依賴外部框架

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
- **六角形架構**: `Create a hexagonal architecture port interface for this use case`
- **領域模型**: `Design a domain model with business rules and invariants`
- **適配器實作**: `Implement an adapter that implements this port interface`

### 六角形架構 Copilot 提示
- **Use Case 設計**: `Create a use case interface for [business operation] following hexagonal architecture`
- **領域服務**: `Implement domain service that orchestrates business logic without external dependencies`
- **輸出埠設計**: `Design an outbound port interface for [external system] using domain language`
- **適配器實作**: `Create a [technology] adapter that implements [port interface] with proper error handling`
- **測試生成**: `Generate unit tests for domain service with mocked ports using hexagonal architecture patterns`

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
- [ ] 六角形架構：領域層無外部框架依賴
- [ ] 六角形架構：適配器正確實作埠介面
- [ ] 六角形架構：依賴方向正確 (向內依賴)

### 六角形架構檢查項目
- [ ] **領域純淨性**: 領域層不包含任何基礎設施或框架程式碼
- [ ] **埠介面設計**: 輸入埠表達業務意圖，輸出埠使用領域語言
- [ ] **適配器隔離**: 技術細節完全封裝在適配器層
- [ ] **依賴方向**: 所有依賴都指向領域核心
- [ ] **測試獨立性**: 領域邏輯可以無外部依賴進行測試

### 程式碼審查重點
- 商業邏輯正確性
- 錯誤處理完整性
- 效能考量
- 安全性檢查
- 可讀性和維護性
- 架構邊界清晰度
- 依賴方向正確性
- 領域邏輯純淨性

## 📚 參考資源

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Gradle User Manual](https://docs.gradle.org/current/userguide/userguide.html)

## 📄 分層指引文件

本專案的詳細分層開發規範已分散至各專門的指引文件：

- **領域層**: 參考 `.github/instructions/domain.instructions.md`
- **適配器層**: 參考 `.github/instructions/adapter.instructions.md`
- **應用程式層**: 參考 `.github/instructions/application.instructions.md`
- **配置層**: 參考 `.github/instructions/configuration.instructions.md`
- **測試層**: 參考 `.github/instructions/test.instructions.md`

---

**記住**: 程式碼是給人讀的，偶爾給機器執行。保持程式碼清晰、簡潔、可維護是我們的首要目標。