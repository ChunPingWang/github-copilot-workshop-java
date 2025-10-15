package com.codurance.training.tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task 類別測試")
class TaskTest {

    @Nested
    @DisplayName("建構子測試")
    class ConstructorTests {

        @Test
        @DisplayName("建立任務_當提供有效參數_應成功建立任務")
        void createTask_whenValidParameters_thenTaskCreatedSuccessfully() {
            // Given
            long expectedId = 1L;
            String expectedDescription = "學習 Java";
            boolean expectedDone = false;

            // When
            Task task = new Task(expectedId, expectedDescription, expectedDone);

            // Then
            assertAll(
                () -> assertEquals(expectedId, task.getId()),
                () -> assertEquals(expectedDescription, task.getDescription()),
                () -> assertEquals(expectedDone, task.isDone())
            );
        }

        @Test
        @DisplayName("建立任務_當描述為空字串_應成功建立任務")
        void createTask_whenEmptyDescription_thenTaskCreatedSuccessfully() {
            // Given
            long expectedId = 1L;
            String expectedDescription = "";
            boolean expectedDone = true;

            // When
            Task task = new Task(expectedId, expectedDescription, expectedDone);

            // Then
            assertAll(
                () -> assertEquals(expectedId, task.getId()),
                () -> assertEquals(expectedDescription, task.getDescription()),
                () -> assertEquals(expectedDone, task.isDone())
            );
        }

        @Test
        @DisplayName("建立任務_當ID為負數_應成功建立任務")
        void createTask_whenNegativeId_thenTaskCreatedSuccessfully() {
            // Given
            long expectedId = -1L;
            String expectedDescription = "測試負數ID";
            boolean expectedDone = false;

            // When
            Task task = new Task(expectedId, expectedDescription, expectedDone);

            // Then
            assertEquals(expectedId, task.getId());
        }

        @Test
        @DisplayName("建立任務_當描述為null_應成功建立任務但可能有風險")
        void createTask_whenNullDescription_thenTaskCreatedSuccessfully() {
            // Given
            long id = 1L;
            String description = null;
            boolean done = false;

            // When
            Task task = new Task(id, description, done);

            // Then
            assertAll(
                () -> assertEquals(id, task.getId()),
                () -> assertNull(task.getDescription()),
                () -> assertEquals(done, task.isDone())
            );
        }
    }

    @Nested
    @DisplayName("Getter 方法測試")
    class GetterTests {

        @Test
        @DisplayName("取得ID_當任務已建立_應回傳正確的ID")
        void getId_whenTaskCreated_thenReturnCorrectId() {
            // Given
            long expectedId = 42L;
            Task task = new Task(expectedId, "測試任務", false);

            // When
            long actualId = task.getId();

            // Then
            assertEquals(expectedId, actualId);
        }

        @Test
        @DisplayName("取得描述_當任務已建立_應回傳正確的描述")
        void getDescription_whenTaskCreated_thenReturnCorrectDescription() {
            // Given
            String expectedDescription = "重要的任務";
            Task task = new Task(1L, expectedDescription, false);

            // When
            String actualDescription = task.getDescription();

            // Then
            assertEquals(expectedDescription, actualDescription);
        }

        @Test
        @DisplayName("檢查完成狀態_當任務未完成_應回傳false")
        void isDone_whenTaskNotCompleted_thenReturnFalse() {
            // Given
            Task task = new Task(1L, "未完成的任務", false);

            // When
            boolean isDone = task.isDone();

            // Then
            assertFalse(isDone);
        }

        @Test
        @DisplayName("檢查完成狀態_當任務已完成_應回傳true")
        void isDone_whenTaskCompleted_thenReturnTrue() {
            // Given
            Task task = new Task(1L, "已完成的任務", true);

            // When
            boolean isDone = task.isDone();

            // Then
            assertTrue(isDone);
        }
    }

    @Nested
    @DisplayName("Setter 方法測試")
    class SetterTests {

        @Test
        @DisplayName("設定完成狀態為true_當任務原本未完成_應更新為完成")
        void setDone_whenSetToTrue_thenTaskMarkedAsCompleted() {
            // Given
            Task task = new Task(1L, "測試任務", false);
            assertFalse(task.isDone()); // 確認初始狀態

            // When
            task.setDone(true);

            // Then
            assertTrue(task.isDone());
        }

        @Test
        @DisplayName("設定完成狀態為false_當任務原本已完成_應更新為未完成")
        void setDone_whenSetToFalse_thenTaskMarkedAsIncomplete() {
            // Given
            Task task = new Task(1L, "測試任務", true);
            assertTrue(task.isDone()); // 確認初始狀態

            // When
            task.setDone(false);

            // Then
            assertFalse(task.isDone());
        }

        @Test
        @DisplayName("重複設定相同狀態_應保持該狀態")
        void setDone_whenSetSameValueMultipleTimes_thenStateRemainsSame() {
            // Given
            Task task = new Task(1L, "測試任務", false);

            // When
            task.setDone(true);
            task.setDone(true);
            task.setDone(true);

            // Then
            assertTrue(task.isDone());

            // When
            task.setDone(false);
            task.setDone(false);

            // Then
            assertFalse(task.isDone());
        }
    }

    @Nested
    @DisplayName("不可變性測試")
    class ImmutabilityTests {

        @Test
        @DisplayName("ID和描述_建立後不應改變")
        void idAndDescription_whenTaskCreated_thenShouldRemainImmutable() {
            // Given
            long originalId = 100L;
            String originalDescription = "不可變的任務";
            Task task = new Task(originalId, originalDescription, false);

            // When - 執行各種操作
            task.setDone(true);
            task.setDone(false);

            // Then - ID和描述應該保持不變
            assertEquals(originalId, task.getId());
            assertEquals(originalDescription, task.getDescription());
        }
    }

    @Nested
    @DisplayName("邊界值測試")
    class BoundaryValueTests {

        @Test
        @DisplayName("最大ID值_應正常運作")
        void maxIdValue_shouldWorkCorrectly() {
            // Given
            long maxId = Long.MAX_VALUE;
            Task task = new Task(maxId, "最大ID任務", false);

            // When & Then
            assertEquals(maxId, task.getId());
        }

        @Test
        @DisplayName("最小ID值_應正常運作")
        void minIdValue_shouldWorkCorrectly() {
            // Given
            long minId = Long.MIN_VALUE;
            Task task = new Task(minId, "最小ID任務", true);

            // When & Then
            assertEquals(minId, task.getId());
        }

        @Test
        @DisplayName("極長描述_應正常運作")
        void veryLongDescription_shouldWorkCorrectly() {
            // Given
            String longDescription = "很長的描述".repeat(1000);
            Task task = new Task(1L, longDescription, false);

            // When & Then
            assertEquals(longDescription, task.getDescription());
        }
    }

    @Nested
    @DisplayName("等值性測試")
    class EqualityTests {

        @Test
        @DisplayName("相同參數的任務_應被視為不同物件")
        void tasksWithSameParameters_shouldBeDifferentObjects() {
            // Given
            Task task1 = new Task(1L, "相同任務", false);
            Task task2 = new Task(1L, "相同任務", false);

            // When & Then
            assertNotSame(task1, task2);
            // 注意：Task 類別沒有覆寫 equals 方法，所以使用物件參照比較
            assertNotEquals(task1, task2);
        }
    }
}