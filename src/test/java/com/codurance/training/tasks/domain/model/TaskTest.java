package com.codurance.training.tasks.domain.model;

/**
 * Comprehensive unit tests for Task domain entity following hexagonal architecture.
 * Tests business logic, value validation, and domain behavior.
 */
public class TaskTest {

    public static void main(String[] args) {
        try {
            testTaskCreation();
            testTaskValidation();
            testTaskStatusOperations();
            testTaskEquality();
            System.out.println("🎉 All Task tests passed!");
        } catch (Exception e) {
            System.err.println("❌ Task test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testTaskCreation() {
        // Given
        TaskId taskId = TaskId.of(1L);
        String description = "Learn hexagonal architecture";
        ProjectId projectId = ProjectId.of("project-1");
        
        // When
        Task task = Task.create(taskId, description, projectId);
        
        // Then
        if (!taskId.equals(task.getId())) {
            throw new AssertionError("Task should have correct ID");
        }
        
        if (!description.equals(task.getDescription())) {
            throw new AssertionError("Task should have correct description");
        }
        
        if (!projectId.equals(task.getProjectId())) {
            throw new AssertionError("Task should have correct project ID");
        }
        
        if (!task.isPending()) {
            throw new AssertionError("New task should be pending");
        }
        
        if (task.isCompleted()) {
            throw new AssertionError("New task should not be completed");
        }
        
        System.out.println("✅ Task creation test passed!");
    }

    private static void testTaskValidation() {
        // Test null task ID
        try {
            Task.create(null, "Valid description", ProjectId.of("project-1"));
            throw new AssertionError("Should throw exception for null task ID");
        } catch (NullPointerException e) {
            // Expected
        }
        
        // Test null description
        try {
            Task.create(TaskId.of(1L), null, ProjectId.of("project-1"));
            throw new AssertionError("Should throw exception for null description");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test empty description
        try {
            Task.create(TaskId.of(2L), "", ProjectId.of("project-1"));
            throw new AssertionError("Should throw exception for empty description");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test whitespace only description
        try {
            Task.create(TaskId.of(3L), "   ", ProjectId.of("project-1"));
            throw new AssertionError("Should throw exception for whitespace-only description");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test null project ID
        try {
            Task.create(TaskId.of(4L), "Valid description", null);
            throw new AssertionError("Should throw exception for null project ID");
        } catch (NullPointerException e) {
            // Expected
        }
        
        System.out.println("✅ Task validation test passed!");
    }

    private static void testTaskStatusOperations() {
        // Given
        Task task = Task.create(TaskId.of(1L), "Test task", ProjectId.of("project-1"));
        
        // Test initial state
        if (!task.isPending()) {
            throw new AssertionError("New task should be pending");
        }
        
        // Test mark as completed
        task.markAsCompleted();
        if (!task.isCompleted()) {
            throw new AssertionError("Task should be completed after marking");
        }
        
        if (task.isPending()) {
            throw new AssertionError("Completed task should not be pending");
        }
        
        // Test mark completed task as completed again (should throw)
        try {
            task.markAsCompleted();
            throw new AssertionError("Should throw exception when marking completed task as completed");
        } catch (IllegalStateException e) {
            // Expected
        }
        
        // Test mark as pending
        task.markAsPending();
        if (!task.isPending()) {
            throw new AssertionError("Task should be pending after marking");
        }
        
        if (task.isCompleted()) {
            throw new AssertionError("Pending task should not be completed");
        }
        
        System.out.println("✅ Task status operations test passed!");
    }

    private static void testTaskEquality() {
        // Given
        TaskId taskId1 = TaskId.of(1L);
        TaskId taskId2 = TaskId.of(1L);
        TaskId taskId3 = TaskId.of(2L);
        
        ProjectId projectId = ProjectId.of("project-1");
        
        Task task1 = Task.create(taskId1, "Task 1", projectId);
        Task task2 = Task.create(taskId2, "Task 1 Different Description", projectId);
        Task task3 = Task.create(taskId3, "Task 1", projectId);
        
        // Test equality based on ID
        if (!task1.equals(task2)) {
            throw new AssertionError("Tasks with same ID should be equal");
        }
        
        if (task1.equals(task3)) {
            throw new AssertionError("Tasks with different IDs should not be equal");
        }
        
        // Test hash code consistency
        if (task1.hashCode() != task2.hashCode()) {
            throw new AssertionError("Equal tasks should have same hash code");
        }
        
        // Test toString contains key information
        String taskString = task1.toString();
        if (!taskString.contains("Task 1")) {
            throw new AssertionError("toString should contain description");
        }
        
        if (!taskString.contains(taskId1.toString())) {
            throw new AssertionError("toString should contain task ID");
        }
        
        System.out.println("✅ Task equality test passed!");
    }
}