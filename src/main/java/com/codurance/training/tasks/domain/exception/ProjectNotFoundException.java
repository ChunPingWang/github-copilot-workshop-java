package com.codurance.training.tasks.domain.exception;

import com.codurance.training.tasks.domain.model.ProjectId;

/**
 * Domain exception thrown when a project cannot be found.
 * Part of the ubiquitous language and domain model.
 */
public class ProjectNotFoundException extends RuntimeException {
    private final ProjectId projectId;

    public ProjectNotFoundException(ProjectId projectId) {
        super(String.format("Project with ID '%s' not found", projectId.getValue()));
        this.projectId = projectId;
    }
    
    public ProjectNotFoundException(String projectName) {
        super(String.format("Project with name '%s' not found", projectName));
        this.projectId = null;
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}