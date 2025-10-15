package com.codurance.training.tasks.adapter.inbound.cli.command;

import java.util.Arrays;
import java.util.List;

/**
 * Parses CLI input strings into TaskCommand objects.
 * Handles the translation of user input to domain commands.
 */
public class CommandParser {
    
    /**
     * Parses a command line input into a TaskCommand.
     * 
     * @param input the raw command line input
     * @return parsed TaskCommand
     * @throws IllegalArgumentException if command format is invalid
     */
    public TaskCommand parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty");
        }
        
        String trimmedInput = input.trim();
        List<String> parts = Arrays.asList(trimmedInput.split("\\s+"));
        
        if (parts.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty");
        }
        
        String commandWord = parts.get(0).toLowerCase();
        
        switch (commandWord) {
            case "help":
                return new TaskCommand(CommandType.HELP);
                
            case "show":
                return new TaskCommand(CommandType.SHOW);
                
            case "project":
                if (parts.size() < 2) {
                    throw new IllegalArgumentException("project command requires a project name");
                }
                String projectName = String.join(" ", parts.subList(1, parts.size()));
                return new TaskCommand(CommandType.PROJECT, projectName);
                
            case "add":
                return parseAddCommand(parts);
                
            case "check":
                return parseCheckCommand(parts, CommandType.CHECK);
                
            case "uncheck":
                return parseCheckCommand(parts, CommandType.UNCHECK);
                
            default:
                throw new IllegalArgumentException("Unknown command: " + commandWord);
        }
    }
    
    private TaskCommand parseAddCommand(List<String> parts) {
        if (parts.size() < 3) {
            throw new IllegalArgumentException("add command requires: add <project> <task description>");
        }
        
        String projectName = parts.get(1);
        String taskDescription = String.join(" ", parts.subList(2, parts.size()));
        
        return new TaskCommand(CommandType.ADD, taskDescription, projectName, null);
    }
    
    private TaskCommand parseCheckCommand(List<String> parts, CommandType commandType) {
        if (parts.size() < 3) {
            String action = commandType == CommandType.CHECK ? "check" : "uncheck";
            throw new IllegalArgumentException(action + " command requires: " + action + " <project> <task_id>");
        }
        
        String projectName = parts.get(1);
        String taskId = parts.get(2);
        
        // Validate task ID is numeric
        try {
            Long.parseLong(taskId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Task ID must be a number");
        }
        
        return new TaskCommand(commandType, null, projectName, taskId);
    }
}