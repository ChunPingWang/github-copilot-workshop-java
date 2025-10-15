package com.codurance.training.tasks.domain.model;

/**
 * Enumeration representing the status of a task.
 * Part of the domain language and business rules.
 */
public enum TaskStatus {
    PENDING("pending"),
    COMPLETED("completed");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isPending() {
        return this == PENDING;
    }
}