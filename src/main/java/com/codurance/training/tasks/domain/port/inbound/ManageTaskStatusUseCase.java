package com.codurance.training.tasks.domain.port.inbound;

import com.codurance.training.tasks.domain.model.TaskId;

/**
 * Use case for managing task completion status.
 * Encapsulates the business logic for marking tasks as done or undone.
 */
public interface ManageTaskStatusUseCase {
    
    /**
     * Changes the completion status of a task.
     * 
     * @param command the command containing task identification and new status
     * @throws com.codurance.training.tasks.domain.exception.TaskNotFoundException if task doesn't exist
     * @throws com.codurance.training.tasks.domain.exception.ProjectNotFoundException if project doesn't exist
     */
    void execute(ChangeTaskStatusCommand command);
    
    /**
     * Marks a task as completed.
     * 
     * @param taskId the ID of the task to complete
     * @throws com.codurance.training.tasks.domain.exception.TaskNotFoundException if task doesn't exist
     */
    void completeTask(TaskId taskId);
    
    /**
     * Marks a task as pending (undoes completion).
     * 
     * @param taskId the ID of the task to mark as pending
     * @throws com.codurance.training.tasks.domain.exception.TaskNotFoundException if task doesn't exist
     */
    void uncompleteTask(TaskId taskId);
    
    /**
     * Command for changing task status.
     */
    record ChangeTaskStatusCommand(
        String projectName,
        Long taskId,
        boolean completed
    ) {}
}