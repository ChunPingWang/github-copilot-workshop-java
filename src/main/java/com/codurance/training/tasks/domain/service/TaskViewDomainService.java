package com.codurance.training.tasks.domain.service;

import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;
import com.codurance.training.tasks.domain.port.outbound.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain service for viewing tasks and projects.
 * Provides read-only operations for the task management system.
 */
public class TaskViewDomainService implements ViewTasksUseCase {
    
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    
    public TaskViewDomainService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }
    
    @Override
    public List<ViewTasksUseCase.ProjectView> getAllProjectsWithTasks() {
        List<Project> projects = projectRepository.findAll();
        
        return projects.stream()
            .map(this::mapToProjectView)
            .collect(Collectors.toList());
    }
    
    private ViewTasksUseCase.ProjectView mapToProjectView(Project project) {
        List<Task> projectTasks = taskRepository.findByProjectId(project.getId());
        
        List<ViewTasksUseCase.TaskView> taskViews = projectTasks.stream()
            .map(task -> new ViewTasksUseCase.TaskView(
                task.getId().getValue(),
                task.getDescription(),
                task.isCompleted()
            ))
            .collect(Collectors.toList());
        
        return new ViewTasksUseCase.ProjectView(project.getName(), taskViews);
    }
}