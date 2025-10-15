package com.codurance.training.tasks.domain.service;

import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.ProjectId;
import com.codurance.training.tasks.domain.port.inbound.CreateProjectUseCase;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;

/**
 * Domain service implementing the Create Project use case.
 * Encapsulates business logic for project creation.
 */
public class ProjectDomainService implements CreateProjectUseCase {
    
    private final ProjectRepository projectRepository;
    
    public ProjectDomainService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    
    @Override
    public ProjectId execute(CreateProjectCommand command) {
        // Business rule: Project names must be unique
        if (projectRepository.existsByName(command.name())) {
            throw new IllegalArgumentException("Project with name '" + command.name() + "' already exists");
        }
        
        // Generate new project ID
        ProjectId projectId = projectRepository.nextId();
        
        // Create project domain object
        Project project = Project.create(projectId, command.name());
        
        // Persist the project
        Project savedProject = projectRepository.save(project);
        
        return savedProject.getId();
    }
}