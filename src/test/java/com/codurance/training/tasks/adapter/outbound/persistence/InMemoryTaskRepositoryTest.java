package com.codurance.training.tasks.adapter.outbound.persistence;

import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.model.ProjectId;
import com.codurance.training.tasks.domain.model.TaskStatus;

import java.util.List;
import java.util.Optional;

/**
 * Unit tests for InMemoryTaskRepository adapter.
 * Tests persistence logic in hexagonal architecture outbound adapter.
 */
public class InMemoryTaskRepositoryTest {

    public static void main(String[] args) {
        try {
            testSaveAndFindTask();
            testFindByProjectId();
            testFindAll();
            testDeleteById();
            testNextId();
            System.out.println("🎉 All InMemoryTaskRepository tests passed!");
        } catch (Exception e) {
            System.err.println("❌ InMemoryTaskRepository test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testSaveAndFindTask() {
        // Given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskId taskId = TaskId.of(1L);
        ProjectId projectId = ProjectId.of("project-1");
        Task task = Task.create(taskId, "Test task", projectId);
        
        // When
        Task savedTask = repository.save(task);
        Optional<Task> foundTask = repository.findById(taskId);
        
        // Then
        if (savedTask == null) {
            throw new AssertionError("Save should return task");
        }
        
        if (!foundTask.isPresent()) {
            throw new AssertionError("Should find saved task");
        }
        
        if (!task.getDescription().equals(foundTask.get().getDescription())) {
            throw new AssertionError("Found task should have same description");
        }
        
        System.out.println("✅ Save and find task test passed!");
    }

    private static void testFindByProjectId() {
        // Given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        ProjectId projectId = ProjectId.of("project-1");
        
        Task task1 = Task.create(TaskId.of(1L), "Task 1", projectId);
        Task task2 = Task.create(TaskId.of(2L), "Task 2", projectId);
        Task task3 = Task.create(TaskId.of(3L), "Task 3", ProjectId.of("project-2"));
        
        repository.save(task1);
        repository.save(task2);
        repository.save(task3);
        
        // When
        List<Task> projectTasks = repository.findByProjectId(projectId);
        
        // Then
        if (projectTasks.size() != 2) {
            throw new AssertionError("Should find 2 tasks for project, found: " + projectTasks.size());
        }
        
        boolean foundTask1 = projectTasks.stream().anyMatch(t -> "Task 1".equals(t.getDescription()));
        boolean foundTask2 = projectTasks.stream().anyMatch(t -> "Task 2".equals(t.getDescription()));
        
        if (!foundTask1 || !foundTask2) {
            throw new AssertionError("Should find both tasks for the project");
        }
        
        System.out.println("✅ Find by project ID test passed!");
    }

    private static void testFindAll() {
        // Given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        
        Task task1 = Task.create(TaskId.of(1L), "Task 1", ProjectId.of("project-1"));
        Task task2 = Task.create(TaskId.of(2L), "Task 2", ProjectId.of("project-2"));
        
        repository.save(task1);
        repository.save(task2);
        
        // When
        List<Task> allTasks = repository.findAll();
        
        // Then
        if (allTasks.size() != 2) {
            throw new AssertionError("Should find all 2 tasks, found: " + allTasks.size());
        }
        
        System.out.println("✅ Find all test passed!");
    }

    private static void testDeleteById() {
        // Given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskId taskId = TaskId.of(1L);
        Task task = Task.create(taskId, "Test task", ProjectId.of("project-1"));
        
        repository.save(task);
        
        // When
        boolean deleted = repository.deleteById(taskId);
        Optional<Task> foundTask = repository.findById(taskId);
        
        // Then
        if (!deleted) {
            throw new AssertionError("Delete should return true");
        }
        
        if (foundTask.isPresent()) {
            throw new AssertionError("Task should be deleted");
        }
        
        // Test delete non-existent task
        boolean deletedAgain = repository.deleteById(taskId);
        if (deletedAgain) {
            throw new AssertionError("Delete non-existent task should return false");
        }
        
        System.out.println("✅ Delete by ID test passed!");
    }

    private static void testNextId() {
        // Given
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        
        // When
        TaskId id1 = repository.nextId();
        TaskId id2 = repository.nextId();
        
        // Then
        if (id1 == null || id2 == null) {
            throw new AssertionError("NextId should not return null");
        }
        
        if (id1.equals(id2)) {
            throw new AssertionError("NextId should generate unique IDs");
        }
        
        if (id1.getValue() >= id2.getValue()) {
            throw new AssertionError("NextId should generate increasing IDs");
        }
        
        System.out.println("✅ Next ID test passed!");
    }
}