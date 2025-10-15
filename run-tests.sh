#!/bin/bash

# 執行測試腳本
# 此腳本會執行所有測試並生成覆蓋率報告

echo "🧪 開始執行單元測試..."
echo "======================================"

# 清理之前的建構檔案
./gradlew clean

# 執行測試
./gradlew test

# 生成覆蓋率報告
./gradlew jacocoTestReport

# 驗證覆蓋率
./gradlew jacocoTestCoverageVerification

echo ""
echo "✅ 測試完成！"
echo "======================================"
echo "📊 測試報告位置："
echo "   - 測試結果: file://$(pwd)/build/reports/tests/test/index.html"
echo "   - 覆蓋率報告: file://$(pwd)/build/reports/jacoco/test/html/index.html"
echo ""
echo "📋 測試統計："
echo "   - 總測試數: $(grep -o '<td>.*tests</td>' build/reports/tests/test/index.html | head -1 | sed 's/<[^>]*>//g' || echo '未知')"
echo "   - 覆蓋率: 查看覆蓋率報告以獲取詳細資訊"
echo ""