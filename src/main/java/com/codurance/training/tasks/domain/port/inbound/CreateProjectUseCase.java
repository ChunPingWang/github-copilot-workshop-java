package com.codurance.training.tasks.domain.port.inbound;

import com.codurance.training.tasks.domain.model.ProjectId;

/**
 * Use case for creating a new project.
 * Represents the business capability to create projects.
 */
public interface CreateProjectUseCase {
    
    /**
     * Creates a new project with the given name.
     * 
     * @param command the command containing project creation details
     * @return the ID of the created project
     * @throws IllegalArgumentException if command is invalid
     */
    ProjectId execute(CreateProjectCommand command);
    
    /**
     * Command object for creating a project.
     */
    record CreateProjectCommand(String name) {
        public CreateProjectCommand {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Project name cannot be null or empty");
            }
        }
    }
}