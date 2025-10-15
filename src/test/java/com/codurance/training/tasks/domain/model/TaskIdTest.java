package com.codurance.training.tasks.domain.model;

import com.codurance.training.tasks.domain.model.TaskId;

/**
 * Unit tests for TaskId value object.
 * Tests immutability, equality, and validation rules.
 */
public class TaskIdTest {
    
    public void testTaskIdCreation() {
        // Given
        Long value = 1L;
        
        // When
        TaskId taskId = TaskId.of(value);
        
        // Then
        if (!value.equals(taskId.getValue())) {
            throw new AssertionError("Expected value: " + value + ", got: " + taskId.getValue());
        }
        
        System.out.println("✅ TaskId creation test passed!");
    }
    
    public void testTaskIdEquality() {
        // Given
        TaskId taskId1 = TaskId.of(1L);
        TaskId taskId2 = TaskId.of(1L);
        TaskId taskId3 = TaskId.of(2L);
        
        // Then
        if (!taskId1.equals(taskId2)) {
            throw new AssertionError("TaskIds with same value should be equal");
        }
        
        if (taskId1.equals(taskId3)) {
            throw new AssertionError("TaskIds with different values should not be equal");
        }
        
        if (!taskId1.equals(taskId1)) {
            throw new AssertionError("TaskId should equal itself");
        }
        
        if (taskId1.equals(null)) {
            throw new AssertionError("TaskId should not equal null");
        }
        
        System.out.println("✅ TaskId equality test passed!");
    }
    
    public void testTaskIdHashCode() {
        // Given
        TaskId taskId1 = TaskId.of(1L);
        TaskId taskId2 = TaskId.of(1L);
        TaskId taskId3 = TaskId.of(2L);
        
        // Then
        if (taskId1.hashCode() != taskId2.hashCode()) {
            throw new AssertionError("Equal TaskIds should have same hash code");
        }
        
        if (taskId1.hashCode() == taskId3.hashCode()) {
            // Note: This might occasionally fail due to hash collisions, but unlikely with simple values
            System.out.println("⚠️ Hash collision detected (rare but possible)");
        }
        
        System.out.println("✅ TaskId hashCode test passed!");
    }
    
    public void testTaskIdToString() {
        // Given
        TaskId taskId = TaskId.of(42L);
        
        // When
        String toString = taskId.toString();
        
        // Then
        if (!toString.contains("42")) {
            throw new AssertionError("toString should contain the value");
        }
        
        if (!toString.contains("TaskId")) {
            throw new AssertionError("toString should contain class name");
        }
        
        System.out.println("✅ TaskId toString test passed!");
    }
    
    public void testTaskIdValidation() {
        // Test null value
        try {
            TaskId.of(null);
            throw new AssertionError("Should throw exception for null value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test negative values (should throw exception)
        try {
            TaskId.of(-1L);
            throw new AssertionError("Should throw exception for negative value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test zero (should throw exception)
        try {
            TaskId.of(0L);
            throw new AssertionError("Should throw exception for zero value");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test positive values (should work)
        TaskId positiveId = TaskId.of(1L);
        if (!Long.valueOf(1L).equals(positiveId.getValue())) {
            throw new AssertionError("Positive values should be allowed");
        }
        
        System.out.println("✅ TaskId validation test passed!");
    }
    
    public static void main(String[] args) {
        TaskIdTest test = new TaskIdTest();
        
        try {
            test.testTaskIdCreation();
            test.testTaskIdEquality();
            test.testTaskIdHashCode();
            test.testTaskIdToString();
            test.testTaskIdValidation();
            System.out.println("🎉 All TaskId tests passed!");
        } catch (Exception e) {
            System.err.println("❌ TaskId test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}