package com.codurance.training.tasks.domain.model;

import java.util.Objects;

/**
 * Value Object representing a Project identifier.
 * Ensures type safety and domain language clarity.
 */
public final class ProjectId {
    private final String value;

    private ProjectId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        this.value = value.trim();
    }

    public static ProjectId of(String value) {
        return new ProjectId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectId projectId = (ProjectId) o;
        return Objects.equals(value, projectId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ProjectId{" + value + '}';
    }
}