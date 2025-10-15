package com.codurance.training.tasks.domain.model;

import java.util.Objects;

/**
 * Task domain entity following hexagonal architecture principles.
 * Contains business logic and invariants, independent of external frameworks.
 * 
 * This is a rich domain model that encapsulates business rules and behavior.
 */
public final class Task {
    private final TaskId id;
    private final String description;
    private final ProjectId projectId;
    private TaskStatus status;

    private Task(TaskId id, String description, ProjectId projectId, TaskStatus status) {
        this.id = Objects.requireNonNull(id, "Task ID cannot be null");
        this.description = validateDescription(description);
        this.projectId = Objects.requireNonNull(projectId, "Project ID cannot be null");
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
    }

    /**
     * Factory method to create a new pending task.
     * Follows domain-driven design principles.
     */
    public static Task create(TaskId id, String description, ProjectId projectId) {
        return new Task(id, description, projectId, TaskStatus.PENDING);
    }

    /**
     * Factory method to reconstruct a task from persistence.
     * Used by adapters to recreate domain objects.
     */
    public static Task restore(TaskId id, String description, ProjectId projectId, TaskStatus status) {
        return new Task(id, description, projectId, status);
    }

    /**
     * Business logic: Mark task as completed.
     * Encapsulates the business rule for task completion.
     */
    public void markAsCompleted() {
        if (this.status.isCompleted()) {
            throw new IllegalStateException("Task is already completed");
        }
        this.status = TaskStatus.COMPLETED;
    }

    /**
     * Business logic: Mark task as pending.
     * Allows reopening a completed task.
     */
    public void markAsPending() {
        this.status = TaskStatus.PENDING;
    }

    /**
     * Business rule: Check if task is completed.
     */
    public boolean isCompleted() {
        return status.isCompleted();
    }

    /**
     * Business rule: Check if task is pending.
     */
    public boolean isPending() {
        return status.isPending();
    }

    private String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be null or empty");
        }
        return description.trim();
    }

    // Getters
    public TaskId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Task{id=%s, description='%s', projectId=%s, status=%s}", 
            id, description, projectId, status);
    }
}