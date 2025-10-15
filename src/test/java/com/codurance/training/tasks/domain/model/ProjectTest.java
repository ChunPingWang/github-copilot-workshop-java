package com.codurance.training.tasks.domain.model;

import com.codurance.training.tasks.domain.model.Project;
import com.codurance.training.tasks.domain.model.ProjectId;

/**
 * Unit tests for Project domain entity.
 * Tests business logic and invariants.
 */
public class ProjectTest {
    
    public void testProjectCreation() {
        // Given
        ProjectId projectId = ProjectId.of("project-1");
        String name = "學習六角形架構";
        
        // When
        Project project = Project.create(projectId, name);
        
        // Then
        if (!name.equals(project.getName())) {
            throw new AssertionError("Expected name: " + name + ", got: " + project.getName());
        }
        
        if (!projectId.equals(project.getId())) {
            throw new AssertionError("Expected ID: " + projectId + ", got: " + project.getId());
        }
        
        System.out.println("✅ Project creation test passed!");
    }
    
    public void testProjectValidation() {
        // Test null ID
        try {
            Project.create(null, "Valid Name");
            throw new AssertionError("Should throw exception for null ID");
        } catch (NullPointerException e) {
            // Expected
        }
        
        // Test null name
        try {
            ProjectId id = ProjectId.of("project-1");
            Project.create(id, null);
            throw new AssertionError("Should throw exception for null name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test empty name
        try {
            ProjectId id = ProjectId.of("project-2");
            Project.create(id, "");
            throw new AssertionError("Should throw exception for empty name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test whitespace only name
        try {
            ProjectId id = ProjectId.of("project-3");
            Project.create(id, "   ");
            throw new AssertionError("Should throw exception for whitespace-only name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        System.out.println("✅ Project validation test passed!");
    }
    
    public void testProjectWithSpecificId() {
        // Given
        ProjectId projectId = ProjectId.of("project-123");
        String name = "測試專案";
        
        // When
        Project project = Project.create(projectId, name);
        
        // Then
        if (!projectId.equals(project.getId())) {
            throw new AssertionError("Expected ID: " + projectId + ", got: " + project.getId());
        }
        
        if (!name.equals(project.getName())) {
            throw new AssertionError("Expected name: " + name + ", got: " + project.getName());
        }
        
        System.out.println("✅ Project with specific ID test passed!");
    }
    
    public void testProjectEquality() {
        // Given
        ProjectId id1 = ProjectId.of("project-1");
        ProjectId id2 = ProjectId.of("project-1");
        ProjectId id3 = ProjectId.of("project-2");
        String name = "我的專案";
        
        Project project1 = Project.create(id1, name);
        Project project2 = Project.create(id2, name);
        Project project3 = Project.create(id3, name);
        
        // Then
        if (!project1.equals(project2)) {
            throw new AssertionError("Projects with same ID should be equal");
        }
        
        if (project1.equals(project3)) {
            throw new AssertionError("Projects with different IDs should not be equal");
        }
        
        if (project1.hashCode() != project2.hashCode()) {
            throw new AssertionError("Equal projects should have same hash code");
        }
        
        System.out.println("✅ Project equality test passed!");
    }
    
    public void testProjectHashCode() {
        // Given
        ProjectId id = ProjectId.of("project-1");
        Project project1 = Project.create(id, "Project 1");
        Project project2 = Project.create(id, "Project 1 Modified");
        
        // Then - Hash code should be based on ID
        if (project1.hashCode() != project2.hashCode()) {
            throw new AssertionError("Projects with same ID should have same hash code");
        }
        
        System.out.println("✅ Project hashCode test passed!");
    }
    
    public void testProjectToString() {
        // Given
        ProjectId id = ProjectId.of("project-123");
        Project project = Project.create(id, "我的專案");
        
        // When
        String toString = project.toString();
        
        // Then
        if (!toString.contains("我的專案")) {
            throw new AssertionError("toString should contain project name");
        }
        
        if (!toString.contains("project-123")) {
            throw new AssertionError("toString should contain project ID");
        }
        
        System.out.println("✅ Project toString test passed!");
    }
    
    public static void main(String[] args) {
        ProjectTest test = new ProjectTest();
        
        try {
            test.testProjectCreation();
            test.testProjectValidation();
            test.testProjectWithSpecificId();
            test.testProjectEquality();
            test.testProjectHashCode();
            test.testProjectToString();
            System.out.println("🎉 All Project tests passed!");
        } catch (Exception e) {
            System.err.println("❌ Project test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}