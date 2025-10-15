package com.codurance.training.tasks.domain.exception;

/**
 * Base domain exception for business rule violations.
 * All domain-specific exceptions should extend this class.
 */
public abstract class BusinessRuleViolationException extends RuntimeException {
    
    protected BusinessRuleViolationException(String message) {
        super(message);
    }

    protected BusinessRuleViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}