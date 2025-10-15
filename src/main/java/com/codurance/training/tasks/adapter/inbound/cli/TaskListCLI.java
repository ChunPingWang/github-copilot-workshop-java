package com.codurance.training.tasks.adapter.inbound.cli;

import com.codurance.training.tasks.adapter.inbound.cli.command.CommandParser;
import com.codurance.training.tasks.adapter.inbound.cli.command.TaskCommand;
import com.codurance.training.tasks.adapter.inbound.cli.display.TaskDisplayService;
import com.codurance.training.tasks.domain.port.inbound.CreateProjectUseCase;
import com.codurance.training.tasks.domain.port.inbound.CreateTaskUseCase;
import com.codurance.training.tasks.domain.port.inbound.ManageTaskStatusUseCase;
import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * CLI Adapter for the Task Management system.
 * Handles command-line interface interactions and delegates to domain use cases.
 * 
 * This adapter translates CLI commands to domain operations,
 * following hexagonal architecture principles.
 */
public class TaskListCLI {
    
    private static final String PROMPT = "> ";
    private static final String QUIT_COMMAND = "quit";
    
    private final CreateProjectUseCase createProjectUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final ManageTaskStatusUseCase manageTaskStatusUseCase;
    private final ViewTasksUseCase viewTasksUseCase;
    
    private final CommandParser commandParser;
    private final TaskDisplayService displayService;
    
    private final BufferedReader input;
    private final PrintWriter output;
    
    public TaskListCLI(
            CreateProjectUseCase createProjectUseCase,
            CreateTaskUseCase createTaskUseCase,
            ManageTaskStatusUseCase manageTaskStatusUseCase,
            ViewTasksUseCase viewTasksUseCase) {
        
        this.createProjectUseCase = createProjectUseCase;
        this.createTaskUseCase = createTaskUseCase;
        this.manageTaskStatusUseCase = manageTaskStatusUseCase;
        this.viewTasksUseCase = viewTasksUseCase;
        
        this.commandParser = new CommandParser();
        this.displayService = new TaskDisplayService();
        
        this.input = new BufferedReader(new InputStreamReader(System.in));
        this.output = new PrintWriter(System.out, true);
    }
    
    /**
     * Starts the interactive CLI session.
     */
    public void run() {
        output.println("Welcome to Task List Manager!");
        output.println("Type 'help' for available commands or 'quit' to exit.");
        
        while (true) {
            try {
                output.print(PROMPT);
                String inputLine = input.readLine();
                
                if (inputLine == null || QUIT_COMMAND.equals(inputLine.trim())) {
                    break;
                }
                
                processCommand(inputLine);
                
            } catch (IOException e) {
                output.println("Error reading input: " + e.getMessage());
                break;
            } catch (Exception e) {
                output.println("Error: " + e.getMessage());
            }
        }
        
        output.println("Goodbye!");
    }
    
    private void processCommand(String inputLine) {
        try {
            TaskCommand command = commandParser.parse(inputLine);
            executeCommand(command);
        } catch (Exception e) {
            output.println("Error: " + e.getMessage());
        }
    }
    
    private void executeCommand(TaskCommand command) {
        switch (command.getType()) {
            case HELP:
                displayHelp();
                break;
            case SHOW:
                displayTasks();
                break;
            case PROJECT:
                createProject(command.getArgument());
                break;
            case ADD:
                addTask(command.getProjectName(), command.getArgument());
                break;
            case CHECK:
                markTaskDone(command.getProjectName(), command.getTaskId());
                break;
            case UNCHECK:
                markTaskUndone(command.getProjectName(), command.getTaskId());
                break;
            default:
                output.println("Unknown command: " + command.getType());
        }
    }
    
    private void displayHelp() {
        output.println();
        output.println("Available commands:");
        output.println("  show                     # Show all projects and tasks");
        output.println("  project <project name>   # Create a new project");
        output.println("  add <project> <task>     # Add a task to a project");
        output.println("  check <project> <task>   # Mark a task as done");
        output.println("  uncheck <project> <task> # Mark a task as not done");
        output.println("  help                     # Show this help message");
        output.println("  quit                     # Exit the application");
        output.println();
    }
    
    private void displayTasks() {
        var projectViews = viewTasksUseCase.getAllProjectsWithTasks();
        displayService.displayProjects(projectViews, output);
    }
    
    private void createProject(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            output.println("Error: Project name cannot be empty");
            return;
        }
        
        try {
            createProjectUseCase.execute(new CreateProjectUseCase.CreateProjectCommand(projectName.trim()));
            output.println("Created project: " + projectName.trim());
        } catch (Exception e) {
            output.println("Error creating project: " + e.getMessage());
        }
    }
    
    private void addTask(String projectName, String taskDescription) {
        if (projectName == null || projectName.trim().isEmpty()) {
            output.println("Error: Project name cannot be empty");
            return;
        }
        
        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            output.println("Error: Task description cannot be empty");
            return;
        }
        
        try {
            createTaskUseCase.execute(new CreateTaskUseCase.CreateTaskCommand(
                taskDescription.trim(),
                projectName.trim()
            ));
            output.println("Added task \"" + taskDescription.trim() + "\" to project \"" + projectName.trim() + "\"");
        } catch (Exception e) {
            output.println("Error adding task: " + e.getMessage());
        }
    }
    
    private void markTaskDone(String projectName, String taskIdStr) {
        markTaskStatus(projectName, taskIdStr, true);
    }
    
    private void markTaskUndone(String projectName, String taskIdStr) {
        markTaskStatus(projectName, taskIdStr, false);
    }
    
    private void markTaskStatus(String projectName, String taskIdStr, boolean completed) {
        if (projectName == null || projectName.trim().isEmpty()) {
            output.println("Error: Project name cannot be empty");
            return;
        }
        
        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            output.println("Error: Task ID cannot be empty");
            return;
        }
        
        try {
            Long taskId = Long.parseLong(taskIdStr.trim());
            manageTaskStatusUseCase.execute(new ManageTaskStatusUseCase.ChangeTaskStatusCommand(
                projectName.trim(),
                taskId,
                completed
            ));
            
            String status = completed ? "completed" : "uncompleted";
            output.println("Marked task " + taskId + " as " + status);
            
        } catch (NumberFormatException e) {
            output.println("Error: Task ID must be a number");
        } catch (Exception e) {
            output.println("Error updating task status: " + e.getMessage());
        }
    }
}