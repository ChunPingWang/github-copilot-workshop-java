package com.codurance.training.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskList 類別測試")
class TaskListTest {

    @Mock
    private BufferedReader mockReader;
    
    @Mock
    private PrintWriter mockWriter;
    
    private TaskList taskList;
    private StringWriter stringWriter;
    private PrintWriter realWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        realWriter = new PrintWriter(stringWriter);
        taskList = new TaskList(mockReader, realWriter);
    }

    @Nested
    @DisplayName("建構子測試")
    class ConstructorTests {

        @Test
        @DisplayName("建立TaskList_當提供有效的Reader和Writer_應成功建立")
        void createTaskList_whenValidReaderAndWriter_thenSuccessfullyCreated() {
            // Given & When
            TaskList newTaskList = new TaskList(mockReader, mockWriter);

            // Then
            assertNotNull(newTaskList);
        }

        @Test
        @DisplayName("建立TaskList_當Reader為null_應可正常建立但使用時可能有問題")
        void createTaskList_whenNullReader_thenCreatedButMayHaveIssuesWhenUsed() {
            // Given & When
            TaskList newTaskList = new TaskList(null, mockWriter);

            // Then
            assertNotNull(newTaskList);
        }

        @Test
        @DisplayName("建立TaskList_當Writer為null_應可正常建立但使用時可能有問題")
        void createTaskList_whenNullWriter_thenCreatedButMayHaveIssuesWhenUsed() {
            // Given & When
            TaskList newTaskList = new TaskList(mockReader, null);

            // Then
            assertNotNull(newTaskList);
        }
    }

    @Nested
    @DisplayName("指令執行測試")
    class CommandExecutionTests {

        @Test
        @DisplayName("執行show指令_當沒有專案_應顯示空白")
        void executeShowCommand_whenNoProjects_thenDisplayNothing() throws IOException {
            // Given
            String command = "show";
            
            // When
            invokeExecuteMethod(command);

            // Then
            String output = stringWriter.toString();
            assertTrue(output.isEmpty() || output.trim().isEmpty());
        }

        @Test
        @DisplayName("執行help指令_應顯示所有可用指令")
        void executeHelpCommand_whenCalled_thenDisplayAllCommands() throws IOException {
            // Given
            String command = "help";
            
            // When
            invokeExecuteMethod(command);

            // Then
            String output = stringWriter.toString();
            assertAll(
                () -> assertTrue(output.contains("Commands:")),
                () -> assertTrue(output.contains("show")),
                () -> assertTrue(output.contains("add project")),
                () -> assertTrue(output.contains("add task")),
                () -> assertTrue(output.contains("check")),
                () -> assertTrue(output.contains("uncheck"))
            );
        }

        @Test
        @DisplayName("執行未知指令_應顯示錯誤訊息")
        void executeUnknownCommand_whenCalled_thenDisplayErrorMessage() throws IOException {
            // Given
            String unknownCommand = "unknown";
            
            // When
            invokeExecuteMethod(unknownCommand);

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("I don't know what the command \"unknown\" is."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("執行空白指令_應顯示錯誤訊息")
        void executeEmptyCommand_whenCalled_thenDisplayErrorMessage(String command) throws IOException {
            // When
            invokeExecuteMethod(command);

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("I don't know what the command"));
        }
    }

    @Nested
    @DisplayName("專案管理測試")
    class ProjectManagementTests {

        @Test
        @DisplayName("新增專案_當提供有效名稱_應成功新增")
        void addProject_whenValidName_thenProjectAddedSuccessfully() throws IOException {
            // Given
            String command = "add project 學習計畫";
            
            // When
            invokeExecuteMethod(command);
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("學習計畫"));
        }

        @Test
        @DisplayName("新增專案_當名稱包含空格_應成功新增")
        void addProject_whenNameContainsSpaces_thenProjectAddedSuccessfully() throws IOException {
            // Given
            String command = "add project 我的 學習 計畫";
            
            // When
            invokeExecuteMethod(command);
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("我的 學習 計畫"));
        }

        @Test
        @DisplayName("新增重複專案名稱_應覆蓋原有專案")
        void addProject_whenDuplicateName_thenOverrideExistingProject() throws IOException {
            // Given
            String command1 = "add project 測試專案";
            String command2 = "add task 測試專案 原始任務";
            String command3 = "add project 測試專案"; // 重複新增
            
            // When
            invokeExecuteMethod(command1);
            invokeExecuteMethod(command2);
            invokeExecuteMethod(command3);
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("測試專案"));
            assertFalse(output.contains("原始任務")); // 原有任務應該被清除
        }
    }

    @Nested
    @DisplayName("任務管理測試")
    class TaskManagementTests {

        @BeforeEach
        void setUpProject() throws IOException {
            invokeExecuteMethod("add project 測試專案");
        }

        @Test
        @DisplayName("新增任務_當專案存在_應成功新增任務")
        void addTask_whenProjectExists_thenTaskAddedSuccessfully() throws IOException {
            // Given
            String command = "add task 測試專案 學習Java基礎";
            
            // When
            invokeExecuteMethod(command);
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertAll(
                () -> assertTrue(output.contains("測試專案")),
                () -> assertTrue(output.contains("學習Java基礎")),
                () -> assertTrue(output.contains("[ ] 1:")) // 未完成狀態
            );
        }

        @Test
        @DisplayName("新增任務_當專案不存在_應顯示錯誤訊息")
        void addTask_whenProjectDoesNotExist_thenDisplayErrorMessage() throws IOException {
            // Given
            String command = "add task 不存在的專案 測試任務";
            
            // When
            invokeExecuteMethod(command);

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("Could not find a project with the name \"不存在的專案\"."));
        }

        @Test
        @DisplayName("新增多個任務_應有不同的ID")
        void addMultipleTasks_whenCalled_thenTasksHaveDifferentIds() throws IOException {
            // Given
            String command1 = "add task 測試專案 第一個任務";
            String command2 = "add task 測試專案 第二個任務";
            
            // When
            invokeExecuteMethod(command1);
            invokeExecuteMethod(command2);
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertAll(
                () -> assertTrue(output.contains("[ ] 1: 第一個任務")),
                () -> assertTrue(output.contains("[ ] 2: 第二個任務"))
            );
        }

        @Test
        @DisplayName("完成任務_當任務存在_應標記為完成")
        void checkTask_whenTaskExists_thenMarkAsCompleted() throws IOException {
            // Given
            invokeExecuteMethod("add task 測試專案 測試任務");
            
            // When
            invokeExecuteMethod("check 1");
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("[x] 1: 測試任務"));
        }

        @Test
        @DisplayName("取消完成任務_當任務存在_應標記為未完成")
        void uncheckTask_whenTaskExists_thenMarkAsIncomplete() throws IOException {
            // Given
            invokeExecuteMethod("add task 測試專案 測試任務");
            invokeExecuteMethod("check 1");
            
            // When
            invokeExecuteMethod("uncheck 1");
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("[ ] 1: 測試任務"));
        }

        @Test
        @DisplayName("完成不存在的任務_應顯示錯誤訊息")
        void checkTask_whenTaskDoesNotExist_thenDisplayErrorMessage() throws IOException {
            // Given & When
            invokeExecuteMethod("check 999");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("Could not find a task with an ID of 999."));
        }

        @Test
        @DisplayName("取消完成不存在的任務_應顯示錯誤訊息")
        void uncheckTask_whenTaskDoesNotExist_thenDisplayErrorMessage() throws IOException {
            // Given & When
            invokeExecuteMethod("uncheck 999");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("Could not find a task with an ID of 999."));
        }
    }

    @Nested
    @DisplayName("顯示功能測試")
    class DisplayTests {

        @Test
        @DisplayName("顯示多個專案_應按新增順序顯示")
        void show_whenMultipleProjects_thenDisplayInOrder() throws IOException {
            // Given
            invokeExecuteMethod("add project 第一個專案");
            invokeExecuteMethod("add project 第二個專案");
            invokeExecuteMethod("add task 第一個專案 任務1");
            invokeExecuteMethod("add task 第二個專案 任務2");
            
            // When
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            int firstProjectIndex = output.indexOf("第一個專案");
            int secondProjectIndex = output.indexOf("第二個專案");
            assertTrue(firstProjectIndex < secondProjectIndex);
        }

        @Test
        @DisplayName("顯示混合狀態的任務_應正確顯示完成標記")
        void show_whenMixedTaskStates_thenDisplayCorrectCompletionMarkers() throws IOException {
            // Given
            invokeExecuteMethod("add project 測試專案");
            invokeExecuteMethod("add task 測試專案 已完成任務");
            invokeExecuteMethod("add task 測試專案 未完成任務");
            invokeExecuteMethod("check 1");
            
            // When
            invokeExecuteMethod("show");

            // Then
            String output = stringWriter.toString();
            assertAll(
                () -> assertTrue(output.contains("[x] 1: 已完成任務")),
                () -> assertTrue(output.contains("[ ] 2: 未完成任務"))
            );
        }
    }

    @Nested
    @DisplayName("執行流程測試")
    class RunLoopTests {

        @Test
        @DisplayName("執行迴圈_當輸入quit_應正常退出")
        void run_whenQuitCommand_thenExitNormally() throws IOException {
            // Given
            BufferedReader testReader = new BufferedReader(new StringReader("quit\n"));
            TaskList testTaskList = new TaskList(testReader, realWriter);
            
            // When & Then - 應該正常結束，不會拋出例外
            assertDoesNotThrow(() -> testTaskList.run());
        }

        @Test
        @DisplayName("執行迴圈_當Reader拋出IOException_應拋出RuntimeException")
        void run_whenIOException_thenThrowRuntimeException() throws IOException {
            // Given
            when(mockReader.readLine()).thenThrow(new IOException("測試異常"));
            TaskList testTaskList = new TaskList(mockReader, mockWriter);
            
            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                testTaskList.run();
            });
            assertEquals("測試異常", exception.getCause().getMessage());
        }
    }

    @Nested
    @DisplayName("邊界值和異常測試")
    class BoundaryAndExceptionTests {

        @Test
        @DisplayName("檢查任務ID_當提供非數字字串_應拋出NumberFormatException")
        void checkTask_whenNonNumericId_thenThrowNumberFormatException() {
            // Given & When & Then
            assertThrows(NumberFormatException.class, () -> {
                invokeExecuteMethod("check abc");
            });
        }

        @Test
        @DisplayName("檢查任務ID_當提供負數_應顯示找不到任務")
        void checkTask_whenNegativeId_thenDisplayTaskNotFound() throws IOException {
            // Given & When
            invokeExecuteMethod("check -1");

            // Then
            String output = stringWriter.toString();
            assertTrue(output.contains("Could not find a task with an ID of -1."));
        }

        @Test
        @DisplayName("新增指令_當缺少參數_應處理ArrayIndexOutOfBoundsException")
        void addCommand_whenMissingParameters_thenHandleException() {
            // Given & When & Then
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                invokeExecuteMethod("add");
            });
        }

        @Test
        @DisplayName("新增任務指令_當缺少任務描述_應處理IndexOutOfBoundsException")
        void addTaskCommand_whenMissingTaskDescription_thenHandleException() {
            // Given & When & Then
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
                invokeExecuteMethod("add task 專案名稱");
            });
        }
    }

    // 輔助方法：通過反射調用私有的 execute 方法
    private void invokeExecuteMethod(String command) throws IOException {
        try {
            var executeMethod = TaskList.class.getDeclaredMethod("execute", String.class);
            executeMethod.setAccessible(true);
            executeMethod.invoke(taskList, command);
        } catch (Exception e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException("Failed to invoke execute method", e);
        }
    }
}