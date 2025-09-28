---
description: New prompt created from chat session
mode: ask
model: GPT-4.1 (copilot)
---
<user>
協助我理解這個專案的狀況，包含業務與技術相關資訊
並以中文回覆
</user>
<assistant>
這個專案是一個簡易的「任務清單管理系統」，用 Java 實作，主要功能是管理多個專案（project）下的任務（task），並支援命令列互動。以下是業務與技術相關的說明：

## 業務面

- **核心功能**：  
  - 建立專案（add project）
  - 在專案下新增任務（add task）
  - 顯示所有專案及其任務（show）
  - 勾選/取消勾選任務完成狀態（check/uncheck）
  - 支援查詢指令（help）
  - 支援離開程式（quit）

- **使用流程**：  
  使用者在命令列輸入指令，系統會根據指令執行相應操作並回饋結果。例如可以建立專案、在專案下新增任務、標記任務完成等。

## 技術面

- **主要檔案與結構**：  
  - Task.java：定義任務物件，包含 id、描述、完成狀態。
  - TaskList.java：主要邏輯，負責解析指令、管理專案與任務、輸出結果。
  - ApplicationTest.java：單元測試，模擬使用者輸入並驗證系統行為。

- **技術細節**：  
  - 使用 `BufferedReader` 和 `PrintWriter` 處理輸入輸出。
  - 任務以 `Map<String, List<Task>>` 方式儲存，key 為專案名稱，value 為任務列表。
  - 任務 ID 自動遞增，確保唯一性。
  - 測試檔案用 JUnit 驗證各種指令的正確性。

- **執行方式**：  
  透過 run 腳本執行，會自動編譯並啟動命令列互動程式。

## 總結

這是一個結構簡單、易於理解的命令列任務管理工具，適合學習 Java 基礎、命令列互動、物件導向設計與單元測試。  
如需深入了解某個類別或功能，可參考上述檔案連結。
</assistant>