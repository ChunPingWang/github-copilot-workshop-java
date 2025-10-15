package com.codurance.training.tasks.domain.model;

import java.util.Objects;

/**
 * Value Object representing a Task identifier.
 * Following hexagonal architecture principles, this is a domain concept 
 * that ensures type safety and prevents primitive obsession.
 */
public final class TaskId {
    private final Long value;

    private TaskId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Task ID must be a positive number");
        }
        this.value = value;
    }

    public static TaskId of(Long value) {
        return new TaskId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(value, taskId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TaskId{" + value + '}';
    }
}