# 測試指南

本專案包含完整的單元測試，涵蓋正向和反向測試條件。

## 🧪 執行測試

### 快速執行
```bash
# 使用自訂腳本（推薦）
./run-tests.sh

# 或使用 Gradle 直接執行
./gradlew test
```

### 生成覆蓋率報告
```bash
./gradlew jacocoTestReport
```

### 驗證覆蓋率
```bash
./gradlew jacocoTestCoverageVerification
```

## 📊 測試結構

### Task 類別測試 (`TaskTest.java`)
- **建構子測試**: 驗證各種參數組合的物件建立
- **Getter 方法測試**: 確保屬性正確回傳
- **Setter 方法測試**: 驗證狀態變更功能
- **不可變性測試**: 確認 ID 和描述的不可變特性
- **邊界值測試**: 測試極值情況（最大/最小 ID、極長描述）
- **等值性測試**: 驗證物件比較行為

### TaskList 類別測試 (`TaskListTest.java`)
- **建構子測試**: 驗證依賴注入和 null 處理
- **指令執行測試**: 測試各種命令的執行
- **專案管理測試**: 驗證專案的新增和管理
- **任務管理測試**: 測試任務的 CRUD 操作
- **顯示功能測試**: 確認輸出格式正確
- **執行流程測試**: 驗證主程式迴圈
- **邊界值和異常測試**: 測試錯誤處理和邊界情況

## 🎯 測試覆蓋標準

- **目標覆蓋率**: 80%
- **測試架構**: Given-When-Then 模式
- **命名慣例**: `{methodName}_when_{condition}_then_{expectedResult}`

## 📋 正向和反向測試條件

### 正向測試 (Happy Path)
- ✅ 正常的任務建立和操作
- ✅ 有效的指令執行
- ✅ 正確的專案和任務管理
- ✅ 預期的輸出格式

### 反向測試 (Edge Cases & Error Handling)
- ❌ 無效的輸入參數
- ❌ 不存在的專案或任務
- ❌ 邊界值測試（極值）
- ❌ 異常情況處理
- ❌ 錯誤訊息驗證

## 🛠️ 測試工具

- **JUnit 5**: 測試框架
- **Mockito**: Mock 物件
- **Jacoco**: 覆蓋率分析
- **AssertJ**: 流暢的斷言（可選）

## 📈 覆蓋率報告

執行測試後，可在以下位置查看報告：
- **HTML 報告**: `build/reports/jacoco/test/html/index.html`
- **XML 報告**: `build/reports/jacoco/test/jacocoTestReport.xml`
- **測試結果**: `build/reports/tests/test/index.html`

## 💡 測試最佳實務

1. **命名清晰**: 測試方法名稱應明確表達測試意圖
2. **單一責任**: 每個測試方法只測試一個功能點
3. **獨立性**: 測試之間不應有相依性
4. **可讀性**: 使用 Given-When-Then 結構
5. **完整性**: 同時包含正向和反向測試案例

---

**提示**: 使用 `./run-tests.sh` 可以一次執行所有測試並生成完整報告！