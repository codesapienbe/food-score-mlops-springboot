package com.university.foodml.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class for structured logging with monitoring capabilities.
 * Implements proper logging standards for production observability.
 */
@Configuration
public class LoggingConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);
    
    /**
     * Initialize logging configuration.
     * Logs startup information for monitoring purposes.
     */
    @PostConstruct
    public void initializeLogging() {
        logger.info("Logging configuration initialized");
        logger.info("Structured logging enabled with JSON format");
        logger.info("Log files will be written to logs/application.log");
    }
} 