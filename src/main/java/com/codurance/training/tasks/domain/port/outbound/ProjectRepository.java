package com.codurance.training.tasks.domain.port.outbound;

import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.ProjectId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for project persistence operations.
 * Defines the contract for project data access using domain language.
 */
public interface ProjectRepository {
    
    /**
     * Saves a project to the persistence layer.
     * 
     * @param project the project to save
     * @return the saved project with any generated values
     */
    Project save(Project project);
    
    /**
     * Finds a project by its unique identifier.
     * 
     * @param projectId the project identifier
     * @return the project if found, empty otherwise
     */
    Optional<Project> findById(ProjectId projectId);
    
    /**
     * Finds a project by its name.
     * 
     * @param name the project name
     * @return the project if found, empty otherwise
     */
    Optional<Project> findByName(String name);
    
    /**
     * Finds all projects in the system.
     * 
     * @return list of all projects
     */
    List<Project> findAll();
    
    /**
     * Checks if a project with the given name exists.
     * 
     * @param name the project name
     * @return true if project exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Generates the next unique project identifier.
     * 
     * @return a new unique project ID
     */
    ProjectId nextId();
}