package com.university.foodml.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for structured logging with monitoring capabilities.
 * Implements proper logging standards for production observability.
 */
@Service
public class LoggingService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    
    /**
     * Log business event with structured format and monitoring fields.
     * 
     * @param level Log level (INFO, WARN, ERROR, DEBUG)
     * @param component Component name for monitoring
     * @param message Business event message
     * @param correlationId Correlation ID for request tracing
     * @param userId User ID for audit purposes
     * @param requestId Request ID for request tracing
     */
    public void logBusinessEvent(String level, String component, String message, 
                                String correlationId, String userId, String requestId) {
        
        // Set MDC context for structured logging
        if (correlationId != null) MDC.put("correlation_id", correlationId);
        if (userId != null) MDC.put("user_id", userId);
        if (requestId != null) MDC.put("request_id", requestId);
        
        try {
            switch (level.toUpperCase()) {
                case "ERROR":
                    logger.error("[{}] {}", component, message);
                    break;
                case "WARN":
                    logger.warn("[{}] {}", component, message);
                    break;
                case "DEBUG":
                    logger.debug("[{}] {}", component, message);
                    break;
                default:
                    logger.info("[{}] {}", component, message);
                    break;
            }
        } finally {
            // Clean up MDC context
            MDC.clear();
        }
    }
    
    /**
     * Log error with structured format and monitoring fields.
     * 
     * @param component Component name for monitoring
     * @param message Error message
     * @param throwable Exception details
     * @param correlationId Correlation ID for request tracing
     * @param userId User ID for audit purposes
     * @param requestId Request ID for request tracing
     */
    public void logError(String component, String message, Throwable throwable,
                        String correlationId, String userId, String requestId) {
        
        // Set MDC context for structured logging
        if (correlationId != null) MDC.put("correlation_id", correlationId);
        if (userId != null) MDC.put("user_id", userId);
        if (requestId != null) MDC.put("request_id", requestId);
        
        try {
            logger.error("[{}] {}: {}", component, message, throwable.getMessage(), throwable);
        } finally {
            // Clean up MDC context
            MDC.clear();
        }
    }
    
    /**
     * Generate correlation ID for request tracing.
     * 
     * @return Unique correlation ID
     */
    public String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Log performance metric with structured format.
     * 
     * @param component Component name for monitoring
     * @param operation Operation name
     * @param durationMs Duration in milliseconds
     * @param correlationId Correlation ID for request tracing
     */
    public void logPerformanceMetric(String component, String operation, long durationMs, String correlationId) {
        
        if (correlationId != null) MDC.put("correlation_id", correlationId);
        
        try {
            logger.info("[{}] Performance: {} completed in {}ms", component, operation, durationMs);
        } finally {
            MDC.clear();
        }
    }
} 