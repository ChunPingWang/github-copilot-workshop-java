package com.codurance.training.tasks.domain.model;

import com.codurance.training.tasks.domain.model.ProjectId;

/**
 * Unit tests for ProjectId value object.
 * Tests immutability, equality, and validation rules.
 */
public class ProjectIdTest {
    
    public void testProjectIdCreation() {
        // Given
        String value = "project-123";
        
        // When
        ProjectId projectId = ProjectId.of(value);
        
        // Then
        if (!value.equals(projectId.getValue())) {
            throw new AssertionError("Expected value: " + value + ", got: " + projectId.getValue());
        }
        
        System.out.println("✅ ProjectId creation test passed!");
    }
    
    public void testProjectIdEquality() {
        // Given
        ProjectId projectId1 = ProjectId.of("project-1");
        ProjectId projectId2 = ProjectId.of("project-1");
        ProjectId projectId3 = ProjectId.of("project-2");
        
        // Then
        if (!projectId1.equals(projectId2)) {
            throw new AssertionError("ProjectIds with same value should be equal");
        }
        
        if (projectId1.equals(projectId3)) {
            throw new AssertionError("ProjectIds with different values should not be equal");
        }
        
        if (!projectId1.equals(projectId1)) {
            throw new AssertionError("ProjectId should equal itself");
        }
        
        if (projectId1.equals(null)) {
            throw new AssertionError("ProjectId should not equal null");
        }
        
        System.out.println("✅ ProjectId equality test passed!");
    }
    
    public void testProjectIdValidation() {
        // Test null value
        try {
            ProjectId.of(null);
            throw new AssertionError("Should throw exception for null value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test empty string
        try {
            ProjectId.of("");
            throw new AssertionError("Should throw exception for empty value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test whitespace only
        try {
            ProjectId.of("   ");
            throw new AssertionError("Should throw exception for whitespace-only value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test trimming
        ProjectId projectId = ProjectId.of("  project-1  ");
        if (!"project-1".equals(projectId.getValue())) {
            throw new AssertionError("ProjectId should trim whitespace");
        }
        
        System.out.println("✅ ProjectId validation test passed!");
    }
    
    public void testProjectIdToString() {
        // Given
        ProjectId projectId = ProjectId.of("my-project");
        
        // When
        String toString = projectId.toString();
        
        // Then
        if (!toString.contains("my-project")) {
            throw new AssertionError("toString should contain the value");
        }
        
        if (!toString.contains("ProjectId")) {
            throw new AssertionError("toString should contain class name");
        }
        
        System.out.println("✅ ProjectId toString test passed!");
    }
    
    public static void main(String[] args) {
        ProjectIdTest test = new ProjectIdTest();
        
        try {
            test.testProjectIdCreation();
            test.testProjectIdEquality();
            test.testProjectIdValidation();
            test.testProjectIdToString();
            System.out.println("🎉 All ProjectId tests passed!");
        } catch (Exception e) {
            System.err.println("❌ ProjectId test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}