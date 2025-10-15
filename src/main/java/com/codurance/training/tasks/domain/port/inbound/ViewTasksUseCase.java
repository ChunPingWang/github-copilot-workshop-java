package com.codurance.training.tasks.domain.port.inbound;

import java.util.List;

/**
 * Use case for viewing tasks and projects.
 * Provides read-only access to the task management system.
 */
public interface ViewTasksUseCase {
    
    /**
     * Retrieves all projects with their tasks.
     * 
     * @return a list of all projects with their associated tasks
     */
    List<ProjectView> getAllProjectsWithTasks();
    
    /**
     * View model representing a project with its tasks.
     * This is a read-only representation optimized for display.
     */
    record ProjectView(
        String projectName,
        List<TaskView> tasks
    ) {}
    
    /**
     * View model representing a task for display purposes.
     */
    record TaskView(
        Long taskId,
        String description,
        boolean isCompleted
    ) {}
}