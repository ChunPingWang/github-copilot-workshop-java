package com.codurance.training.tasks.domain.exception;

import com.codurance.training.tasks.domain.model.TaskId;

/**
 * Domain exception thrown when a task cannot be found.
 * Part of the ubiquitous language and domain model.
 */
public class TaskNotFoundException extends RuntimeException {
    private final TaskId taskId;

    public TaskNotFoundException(TaskId taskId) {
        super(String.format("Task with ID %s not found", taskId.getValue()));
        this.taskId = taskId;
    }
    
    public TaskNotFoundException(String message) {
        super(message);
        this.taskId = null;
    }

    public TaskId getTaskId() {
        return taskId;
    }
}