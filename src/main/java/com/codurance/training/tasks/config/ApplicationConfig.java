package com.codurance.training.tasks.config;

import com.codurance.training.tasks.adapter.inbound.cli.TaskListCLI;
import com.codurance.training.tasks.adapter.outbound.persistence.InMemoryProjectRepository;
import com.codurance.training.tasks.adapter.outbound.persistence.InMemoryTaskRepository;
import com.codurance.training.tasks.domain.port.inbound.CreateProjectUseCase;
import com.codurance.training.tasks.domain.port.inbound.CreateTaskUseCase;
import com.codurance.training.tasks.domain.port.inbound.ManageTaskStatusUseCase;
import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;
import com.codurance.training.tasks.domain.port.outbound.TaskRepository;
import com.codurance.training.tasks.domain.service.ProjectDomainService;
import com.codurance.training.tasks.domain.service.TaskDomainService;
import com.codurance.training.tasks.domain.service.TaskStatusDomainService;
import com.codurance.training.tasks.domain.service.TaskViewDomainService;

/**
 * Application configuration for dependency injection.
 * 
 * This configuration class wires together all components of the hexagonal architecture:
 * - Outbound adapters (repositories)
 * - Domain services (use case implementations)
 * - Inbound adapters (CLI)
 * 
 * In a production system, this would be replaced with a framework like Spring.
 */
public class ApplicationConfig {
    
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    
    private final CreateProjectUseCase createProjectUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final ManageTaskStatusUseCase manageTaskStatusUseCase;
    private final ViewTasksUseCase viewTasksUseCase;
    
    public ApplicationConfig() {
        // Create outbound adapters
        this.projectRepository = new InMemoryProjectRepository();
        this.taskRepository = new InMemoryTaskRepository();
        
        // Create domain services (use case implementations)
        this.createProjectUseCase = new ProjectDomainService(projectRepository);
        this.createTaskUseCase = new TaskDomainService(taskRepository, projectRepository);
        this.manageTaskStatusUseCase = new TaskStatusDomainService(taskRepository, projectRepository);
        this.viewTasksUseCase = new TaskViewDomainService(projectRepository, taskRepository);
    }
    
    /**
     * Creates and returns the main CLI application.
     * This is the entry point for the hexagonal architecture.
     */
    public TaskListCLI createTaskListCLI() {
        return new TaskListCLI(
            createProjectUseCase,
            createTaskUseCase,
            manageTaskStatusUseCase,
            viewTasksUseCase
        );
    }
    
    /**
     * Provides access to the project repository for testing purposes.
     */
    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }
    
    /**
     * Provides access to the task repository for testing purposes.
     */
    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    
    /**
     * Clears all data. Useful for testing and resetting application state.
     */
    public void clearAllData() {
        if (projectRepository instanceof InMemoryProjectRepository) {
            ((InMemoryProjectRepository) projectRepository).clear();
        }
        if (taskRepository instanceof InMemoryTaskRepository) {
            ((InMemoryTaskRepository) taskRepository).clear();
        }
    }
}