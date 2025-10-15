package com.codurance.training.tasks.domain.model;

import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.model.ProjectId;
import com.codurance.training.tasks.domain.model.TaskStatus;

/**
 * Unit tests for the Task domain entity.
 * Tests the new hexagonal architecture Task implementation.
 */
public class TaskDomainTest {

    public void testTaskCreation() {
        // Given
        TaskId taskId = TaskId.of(1L);
        String description = "學習六角形架構";
        ProjectId projectId = ProjectId.of("project-1");

        // When
        Task task = Task.create(taskId, description, projectId);

        // Then
        if (!taskId.equals(task.getId())) {
            throw new AssertionError("Expected task ID: " + taskId + ", got: " + task.getId());
        }
        if (!description.equals(task.getDescription())) {
            throw new AssertionError("Expected description: " + description + ", got: " + task.getDescription());
        }
        if (!projectId.equals(task.getProjectId())) {
            throw new AssertionError("Expected project ID: " + projectId + ", got: " + task.getProjectId());
        }
        if (task.isCompleted()) {
            throw new AssertionError("New task should not be completed");
        }
        if (!TaskStatus.PENDING.equals(task.getStatus())) {
            throw new AssertionError("New task should have PENDING status");
        }

        System.out.println("✅ Task creation test passed!");
    }

    public void testTaskCompletion() {
        // Given
        TaskId taskId = TaskId.of(1L);
        String description = "學習六角形架構";
        ProjectId projectId = ProjectId.of("project-1");
        Task task = Task.create(taskId, description, projectId);

        // When
        task.markAsCompleted();

        // Then
        if (!task.isCompleted()) {
            throw new AssertionError("Task should be completed");
        }
        if (!TaskStatus.COMPLETED.equals(task.getStatus())) {
            throw new AssertionError("Task should have COMPLETED status");
        }

        System.out.println("✅ Task completion test passed!");
    }

    public void testTaskUncomplete() {
        // Given
        TaskId taskId = TaskId.of(1L);
        String description = "學習六角形架構";
        ProjectId projectId = ProjectId.of("project-1");
        Task task = Task.create(taskId, description, projectId);
        task.markAsCompleted(); // First complete it

        // When
        task.markAsPending();

        // Then
        if (task.isCompleted()) {
            throw new AssertionError("Task should not be completed");
        }
        if (!TaskStatus.PENDING.equals(task.getStatus())) {
            throw new AssertionError("Task should have PENDING status");
        }

        System.out.println("✅ Task uncomplete test passed!");
    }

    public void testInvalidTaskCreation() {
        // Test with null description
        try {
            TaskId taskId = TaskId.of(1L);
            ProjectId projectId = ProjectId.of("project-1");
            Task.create(taskId, null, projectId);
            throw new AssertionError("Should have thrown exception for null description");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with empty description
        try {
            TaskId taskId = TaskId.of(1L);
            ProjectId projectId = ProjectId.of("project-1");
            Task.create(taskId, "", projectId);
            throw new AssertionError("Should have thrown exception for empty description");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        System.out.println("✅ Invalid task creation test passed!");
    }

    /**
     * Simple test runner - in a real project, this would be handled by JUnit
     */
    public static void main(String[] args) {
        TaskDomainTest test = new TaskDomainTest();
        
        try {
            test.testTaskCreation();
            test.testTaskCompletion();
            test.testTaskUncomplete();
            test.testInvalidTaskCreation();
            System.out.println("🎉 All Task domain tests passed!");
        } catch (Exception e) {
            System.err.println("❌ Task test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}