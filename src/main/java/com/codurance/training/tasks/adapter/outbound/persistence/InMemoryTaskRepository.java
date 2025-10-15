package com.codurance.training.tasks.adapter.outbound.persistence;

import com.codurance.training.tasks.domain.model.ProjectId;
import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.port.outbound.TaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TaskRepository.
 * This adapter provides persistence functionality for tasks without external dependencies.
 * 
 * In a production system, this would be replaced with a database adapter.
 */
public class InMemoryTaskRepository implements TaskRepository {
    
    private final Map<TaskId, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    
    @Override
    public TaskId nextId() {
        return TaskId.of(nextId.getAndIncrement());
    }
    
    @Override
    public Task save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        
        tasks.put(task.getId(), task);
        return task;
    }
    
    @Override
    public Optional<Task> findById(TaskId taskId) {
        if (taskId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(tasks.get(taskId));
    }
    
    @Override
    public List<Task> findByProjectId(ProjectId projectId) {
        if (projectId == null) {
            return new ArrayList<>();
        }
        
        return tasks.values().stream()
            .filter(task -> task.getProjectId().equals(projectId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
    
    @Override
    public boolean deleteById(TaskId taskId) {
        if (taskId == null) {
            return false;
        }
        return tasks.remove(taskId) != null;
    }
    
    /**
     * Checks if a task with the given project and description exists.
     * Useful for preventing duplicate tasks within a project.
     */
    public boolean existsByProjectIdAndDescription(ProjectId projectId, String description) {
        if (projectId == null || description == null || description.trim().isEmpty()) {
            return false;
        }
        
        return tasks.values().stream()
            .anyMatch(task -> task.getProjectId().equals(projectId) 
                && task.getDescription().equals(description.trim()));
    }
    
    /**
     * Clears all tasks. Useful for testing.
     */
    public void clear() {
        tasks.clear();
        nextId.set(1);
    }
    
    /**
     * Returns the current number of tasks stored.
     */
    public int size() {
        return tasks.size();
    }
    
    /**
     * Returns the number of tasks for a specific project.
     */
    public int countByProjectId(ProjectId projectId) {
        if (projectId == null) {
            return 0;
        }
        
        return (int) tasks.values().stream()
            .filter(task -> task.getProjectId().equals(projectId))
            .count();
    }
}