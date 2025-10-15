package com.codurance.training.tasks.domain.port.outbound;

import com.codurance.training.tasks.domain.model.Task;
import com.codurance.training.tasks.domain.model.TaskId;
import com.codurance.training.tasks.domain.model.ProjectId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for task persistence operations.
 * This interface defines what the domain needs from the infrastructure layer
 * for task-related data operations, using domain language only.
 * 
 * Following hexagonal architecture, this is defined by the domain
 * and implemented by the infrastructure adapters.
 */
public interface TaskRepository {
    
    /**
     * Saves a task to the persistence layer.
     * 
     * @param task the task to save
     * @return the saved task with any generated values
     */
    Task save(Task task);
    
    /**
     * Finds a task by its unique identifier.
     * 
     * @param taskId the task identifier
     * @return the task if found, empty otherwise
     */
    Optional<Task> findById(TaskId taskId);
    
    /**
     * Finds all tasks belonging to a specific project.
     * 
     * @param projectId the project identifier
     * @return list of tasks in the project, empty list if none found
     */
    List<Task> findByProjectId(ProjectId projectId);
    
    /**
     * Finds all tasks in the system.
     * 
     * @return list of all tasks
     */
    List<Task> findAll();
    
    /**
     * Deletes a task by its identifier.
     * 
     * @param taskId the task identifier
     * @return true if task was deleted, false if not found
     */
    boolean deleteById(TaskId taskId);
    
    /**
     * Generates the next unique task identifier.
     * 
     * @return a new unique task ID
     */
    TaskId nextId();
}