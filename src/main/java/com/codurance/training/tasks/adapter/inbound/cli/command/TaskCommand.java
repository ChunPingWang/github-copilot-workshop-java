package com.codurance.training.tasks.adapter.inbound.cli.command;

/**
 * Represents a parsed CLI command with its type and arguments.
 */
public class TaskCommand {
    private final CommandType type;
    private final String argument;
    private final String projectName;
    private final String taskId;
    
    public TaskCommand(CommandType type) {
        this(type, null, null, null);
    }
    
    public TaskCommand(CommandType type, String argument) {
        this(type, argument, null, null);
    }
    
    public TaskCommand(CommandType type, String argument, String projectName, String taskId) {
        this.type = type;
        this.argument = argument;
        this.projectName = projectName;
        this.taskId = taskId;
    }
    
    public CommandType getType() {
        return type;
    }
    
    public String getArgument() {
        return argument;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public String getTaskId() {
        return taskId;
    }
}