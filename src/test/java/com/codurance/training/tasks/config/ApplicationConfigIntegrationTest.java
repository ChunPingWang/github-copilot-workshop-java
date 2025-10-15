package com.codurance.training.tasks.config;

import com.codurance.training.tasks.adapter.outbound.persistence.InMemoryProjectRepository;
import com.codurance.training.tasks.adapter.outbound.persistence.InMemoryTaskRepository;
import com.codurance.training.tasks.config.ApplicationConfig;
import com.codurance.training.tasks.domain.port.inbound.CreateProjectUseCase;
import com.codurance.training.tasks.domain.port.inbound.CreateTaskUseCase;
import com.codurance.training.tasks.domain.port.inbound.ViewTasksUseCase;
import com.codurance.training.tasks.domain.service.ProjectDomainService;
import com.codurance.training.tasks.domain.service.TaskDomainService;
import com.codurance.training.tasks.domain.service.TaskViewDomainService;

import java.util.List;

/**
 * Integration test for the hexagonal architecture configuration.
 * Validates that all components work together correctly.
 * 
 * This test demonstrates the hexagonal architecture's testability by testing
 * the domain services directly without going through the CLI adapter.
 */
public class ApplicationConfigIntegrationTest {
    
    private InMemoryProjectRepository projectRepository;
    private InMemoryTaskRepository taskRepository;
    private CreateProjectUseCase createProjectUseCase;
    private CreateTaskUseCase createTaskUseCase;
    private ViewTasksUseCase viewTasksUseCase;
    
    public void setUp() {
        // Create repositories
        projectRepository = new InMemoryProjectRepository();
        taskRepository = new InMemoryTaskRepository();
        
        // Create domain services
        createProjectUseCase = new ProjectDomainService(projectRepository);
        createTaskUseCase = new TaskDomainService(taskRepository, projectRepository);
        viewTasksUseCase = new TaskViewDomainService(projectRepository, taskRepository);
        
        // Clear any existing data
        projectRepository.clear();
        taskRepository.clear();
    }
    
    public void testFullWorkflow() {
        setUp();
        
        // Given - 準備測試資料
        String projectName = "學習六角形架構";
        String task1Description = "理解領域層設計";
        String task2Description = "實作適配器模式";
        
        // When - 執行業務操作
        // 1. 建立專案
        createProjectUseCase.execute(new CreateProjectUseCase.CreateProjectCommand(projectName));
        
        // 2. 新增任務
        createTaskUseCase.execute(new CreateTaskUseCase.CreateTaskCommand(task1Description, projectName));
        createTaskUseCase.execute(new CreateTaskUseCase.CreateTaskCommand(task2Description, projectName));
        
        // 3. 查看結果
        List<ViewTasksUseCase.ProjectView> projects = viewTasksUseCase.getAllProjectsWithTasks();
        
        // Then - 驗證結果
        if (projects == null || projects.size() != 1) {
            throw new AssertionError("Expected exactly 1 project, got: " + (projects == null ? "null" : projects.size()));
        }
        
        ViewTasksUseCase.ProjectView project = projects.get(0);
        if (!projectName.equals(project.projectName())) {
            throw new AssertionError("Expected project name: " + projectName + ", got: " + project.projectName());
        }
        
        if (project.tasks().size() != 2) {
            throw new AssertionError("Expected 2 tasks, got: " + project.tasks().size());
        }
        
        // 驗證任務內容
        List<ViewTasksUseCase.TaskView> tasks = project.tasks();
        boolean hasTask1 = tasks.stream().anyMatch(task -> task.description().equals(task1Description));
        boolean hasTask2 = tasks.stream().anyMatch(task -> task.description().equals(task2Description));
        
        if (!hasTask1) {
            throw new AssertionError("Task 1 not found: " + task1Description);
        }
        if (!hasTask2) {
            throw new AssertionError("Task 2 not found: " + task2Description);
        }
        
        // 驗證所有任務都是未完成狀態
        boolean allIncomplete = tasks.stream().allMatch(task -> !task.isCompleted());
        if (!allIncomplete) {
            throw new AssertionError("All tasks should be incomplete");
        }
        
        System.out.println("✅ Full workflow test passed!");
    }
    
    public void testApplicationConfig() {
        // Test ApplicationConfig integration
        ApplicationConfig config = new ApplicationConfig();
        
        if (config.createTaskListCLI() == null) {
            throw new AssertionError("TaskListCLI should not be null");
        }
        
        if (config.getProjectRepository() == null) {
            throw new AssertionError("ProjectRepository should not be null");
        }
        
        if (config.getTaskRepository() == null) {
            throw new AssertionError("TaskRepository should not be null");
        }
        
        System.out.println("✅ ApplicationConfig test passed!");
    }
    
    /**
     * Simple test runner - in a real project, this would be handled by JUnit
     */
    public static void main(String[] args) {
        ApplicationConfigIntegrationTest test = new ApplicationConfigIntegrationTest();
        
        try {
            test.testFullWorkflow();
            test.testApplicationConfig();
            System.out.println("🎉 All integration tests passed!");
        } catch (Exception e) {
            System.err.println("❌ Integration test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}