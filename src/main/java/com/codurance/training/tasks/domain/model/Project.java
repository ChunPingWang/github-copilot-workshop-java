package com.codurance.training.tasks.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Project aggregate root in the domain model.
 * Manages a collection of tasks and enforces business rules.
 */
public final class Project {
    private final ProjectId id;
    private final String name;
    private final List<Task> tasks;

    private Project(ProjectId id, String name) {
        this.id = Objects.requireNonNull(id, "Project ID cannot be null");
        this.name = validateName(name);
        this.tasks = new ArrayList<>();
    }

    /**
     * Factory method to create a new project.
     */
    public static Project create(ProjectId id, String name) {
        return new Project(id, name);
    }

    /**
     * Business logic: Add a task to this project.
     */
    public void addTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        if (!this.id.equals(task.getProjectId())) {
            throw new IllegalArgumentException("Task does not belong to this project");
        }
        this.tasks.add(task);
    }

    /**
     * Business rule: Check if all tasks in the project are completed.
     */
    public boolean allTasksCompleted() {
        return !tasks.isEmpty() && tasks.stream().allMatch(Task::isCompleted);
    }

    /**
     * Business query: Get all pending tasks.
     */
    public List<Task> getPendingTasks() {
        return tasks.stream()
            .filter(Task::isPending)
            .toList();
    }

    /**
     * Business query: Get all completed tasks.
     */
    public List<Task> getCompletedTasks() {
        return tasks.stream()
            .filter(Task::isCompleted)
            .toList();
    }

    /**
     * Business query: Count total tasks.
     */
    public int getTotalTaskCount() {
        return tasks.size();
    }

    /**
     * Business query: Count completed tasks.
     */
    public int getCompletedTaskCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        return name.trim();
    }

    // Getters
    public ProjectId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Project{id=%s, name='%s', taskCount=%d}", 
            id, name, tasks.size());
    }
}