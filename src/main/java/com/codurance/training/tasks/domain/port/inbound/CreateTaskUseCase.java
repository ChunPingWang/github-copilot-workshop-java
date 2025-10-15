package com.codurance.training.tasks.domain.port.inbound;

import com.codurance.training.tasks.domain.model.TaskId;

/**
 * Use case for creating a new task.
 * This is an inbound port that defines the business operation 
 * without any technical implementation details.
 * 
 * Following hexagonal architecture, this interface represents 
 * what the application can do, not how it does it.
 */
public interface CreateTaskUseCase {
    
    /**
     * Creates a new task in the specified project.
     * 
     * @param command the command containing task creation details
     * @return the ID of the created task
     * @throws com.codurance.training.tasks.domain.exception.ProjectNotFoundException if project doesn't exist
     * @throws IllegalArgumentException if command is invalid
     */
    TaskId execute(CreateTaskCommand command);
    
    /**
     * Command object encapsulating the parameters needed to create a task.
     * Follows Command pattern and promotes immutability.
     */
    record CreateTaskCommand(
        String description,
        String projectName
    ) {
        public CreateTaskCommand {
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Task description cannot be null or empty");
            }
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new IllegalArgumentException("Project name cannot be null or empty");
            }
        }
    }
}