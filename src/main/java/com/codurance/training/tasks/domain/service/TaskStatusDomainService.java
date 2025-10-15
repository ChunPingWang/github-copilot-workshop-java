package com.codurance.training.tasks.domain.service;

import com.codurance.training.tasks.domain.exception.ProjectNotFoundException;
import com.codurance.training.tasks.domain.exception.TaskNotFoundException;
import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.port.inbound.ManageTaskStatusUseCase;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;
import com.codurance.training.tasks.domain.port.outbound.TaskRepository;

/**
 * Domain service for managing task status.
 * Implements business logic for task completion and uncompleting.
 */
public class TaskStatusDomainService implements ManageTaskStatusUseCase {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskStatusDomainService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }
    
    @Override
    public void execute(ChangeTaskStatusCommand command) {
        // Find project to validate it exists
        Project project = projectRepository.findByName(command.projectName())
            .orElseThrow(() -> new ProjectNotFoundException(command.projectName()));
        
        // Find task by ID within the project
        TaskId taskId = TaskId.of(command.taskId());
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        // Validate task belongs to the specified project
        if (!task.getProjectId().equals(project.getId())) {
            throw new TaskNotFoundException("Task " + command.taskId() + " not found in project " + command.projectName());
        }
        
        // Update task status based on command
        if (command.completed()) {
            task.markAsCompleted();
        } else {
            task.markAsPending();
        }
        
        // Persist changes
        taskRepository.save(task);
    }
    
    @Override
    public void completeTask(TaskId taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        // Business logic: mark task as completed
        task.markAsCompleted();
        
        // Persist changes
        taskRepository.save(task);
    }
    
    @Override
    public void uncompleteTask(TaskId taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException(taskId));
        
        // Business logic: mark task as pending
        task.markAsPending();
        
        // Persist changes
        taskRepository.save(task);
    }
}