package com.codurance.training.tasks;

import com.codurance.training.tasks.adapter.inbound.cli.TaskListCLI;
import com.codurance.training.tasks.config.ApplicationConfig;

/**
 * Main application entry point for the Task List Management system.
 * 
 * This application demonstrates hexagonal architecture principles:
 * - Clean separation between domain logic and infrastructure
 * - Dependency inversion through ports and adapters
 * - Testable business logic without external dependencies
 * 
 * The application follows the six-sided architecture pattern where:
 * - Domain layer contains business logic and rules
 * - Ports define contracts for external communication
 * - Adapters implement the ports for specific technologies
 * - Configuration layer wires everything together
 */
public class TaskListApplication {
    
    public static void main(String[] args) {
        System.out.println("Starting Task List Management System...");
        System.out.println("Architecture: Hexagonal (Ports & Adapters)");
        System.out.println("==========================================");
        
        try {
            // Initialize application configuration
            ApplicationConfig config = new ApplicationConfig();
            
            // Create and start CLI
            TaskListCLI cli = config.createTaskListCLI();
            cli.run();
            
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}