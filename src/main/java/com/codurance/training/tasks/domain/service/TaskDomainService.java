package com.codurance.training.tasks.domain.service;

import com.codurance.training.tasks.domain.exception.ProjectNotFoundException;
import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.port.inbound.CreateTaskUseCase;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;
import com.codurance.training.tasks.domain.port.outbound.TaskRepository;

/**
 * Domain service implementing the Create Task use case.
 * Contains pure business logic without any infrastructure dependencies.
 * 
 * Following hexagonal architecture, this service orchestrates domain objects
 * and repositories to fulfill business requirements.
 */
public class TaskDomainService implements CreateTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskDomainService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }
    
    @Override
    public TaskId execute(CreateTaskCommand command) {
        // Business rule: Ensure project exists before creating task
        Project project = projectRepository.findByName(command.projectName())
            .orElseThrow(() -> new ProjectNotFoundException(command.projectName()));
        
        // Generate new task ID
        TaskId taskId = taskRepository.nextId();
        
        // Create task domain object
        Task task = Task.create(taskId, command.description(), project.getId());
        
        // Persist the task
        Task savedTask = taskRepository.save(task);
        
        return savedTask.getId();
    }
}