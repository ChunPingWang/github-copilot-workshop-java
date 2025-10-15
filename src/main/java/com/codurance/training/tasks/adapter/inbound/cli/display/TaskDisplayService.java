package com.codurance.training.tasks.adapter.inbound.cli.display;

import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase.ProjectView;
import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase.TaskView;

import java.io.PrintWriter;
import java.util.List;

/**
 * Service for displaying task and project information in the CLI.
 * Handles the formatting and presentation of domain data for command-line users.
 */
public class TaskDisplayService {
    
    /**
     * Displays all projects and their tasks in a formatted way.
     * 
     * @param projectViews list of projects with their tasks
     * @param output output writer for displaying results
     */
    public void displayProjects(List<ProjectView> projectViews, PrintWriter output) {
        if (projectViews.isEmpty()) {
            output.println("No projects found.");
            return;
        }
        
        output.println();
        
        for (ProjectView project : projectViews) {
            displayProject(project, output);
            output.println();
        }
    }
    
    private void displayProject(ProjectView project, PrintWriter output) {
        output.println(project.projectName());
        
        List<TaskView> tasks = project.tasks();
        if (tasks.isEmpty()) {
            output.println("    No tasks");
            return;
        }
        
        for (TaskView task : tasks) {
            displayTask(task, output);
        }
    }
    
    private void displayTask(TaskView task, PrintWriter output) {
        String checkbox = task.isCompleted() ? "[x]" : "[ ]";
        output.printf("    %s %d: %s%n", checkbox, task.taskId(), task.description());
    }
}