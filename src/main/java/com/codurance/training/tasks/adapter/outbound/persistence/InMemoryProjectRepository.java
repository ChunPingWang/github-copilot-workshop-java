package com.codurance.training.tasks.adapter.outbound.persistence;

import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.ProjectId;
import com.codurance.training.tasks.domain.port.outbound.ProjectRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of ProjectRepository.
 * This adapter provides persistence functionality for projects without external dependencies.
 * 
 * In a production system, this would be replaced with a database adapter.
 */
public class InMemoryProjectRepository implements ProjectRepository {
    
    private final Map<ProjectId, Project> projects = new ConcurrentHashMap<>();
    private final Map<String, ProjectId> projectNameIndex = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    
    @Override
    public ProjectId nextId() {
        return ProjectId.of(String.valueOf(nextId.getAndIncrement()));
    }
    
    @Override
    public Project save(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        
        // Check for name conflicts (excluding the project being updated)
        Project existingWithSameName = projectNameIndex.containsKey(project.getName()) 
            ? projects.get(projectNameIndex.get(project.getName()))
            : null;
            
        if (existingWithSameName != null && !existingWithSameName.getId().equals(project.getId())) {
            throw new IllegalArgumentException("Project with name '" + project.getName() + "' already exists");
        }
        
        projects.put(project.getId(), project);
        projectNameIndex.put(project.getName(), project.getId());
        return project;
    }
    
    @Override
    public Optional<Project> findById(ProjectId projectId) {
        if (projectId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(projects.get(projectId));
    }
    
    @Override
    public Optional<Project> findByName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return Optional.empty();
        }
        
        ProjectId projectId = projectNameIndex.get(projectName.trim());
        return projectId != null ? Optional.ofNullable(projects.get(projectId)) : Optional.empty();
    }
    
    @Override
    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }
    
    @Override
    public boolean existsByName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return false;
        }
        return projectNameIndex.containsKey(projectName.trim());
    }
    
    /**
     * Clears all projects. Useful for testing.
     */
    public void clear() {
        projects.clear();
        projectNameIndex.clear();
        nextId.set(1);
    }
    
    /**
     * Returns the current number of projects stored.
     */
    public int size() {
        return projects.size();
    }
}