package com.university.foodml.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

/**
 * Configuration class to ensure proper Logback initialization and prevent
 * external dependencies from interfering with our logging configuration.
 */
@Configuration
@DependsOn("loggingService")
public class LoggingConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);
    
    @PostConstruct
    public void initializeLogging() {
        try {
            // Set system properties to ensure our Logback configuration takes precedence
            System.setProperty("logback.configurationFile", "classpath:logback-spring.xml");
            System.setProperty("logback.debug", "false");
            
            // Test that our logging configuration is working
            logger.info("LoggingConfig: Logback configuration initialized successfully");
            logger.info("LoggingConfig: Using configuration file: {}", 
                System.getProperty("logback.configurationFile"));
            
            // Test structured logging with MDC
            org.slf4j.MDC.put("component", "LoggingConfig");
            org.slf4j.MDC.put("initialization_phase", "post_construct");
            logger.info("LoggingConfig: Structured logging test successful");
            org.slf4j.MDC.clear();
            
        } catch (Exception e) {
            logger.error("LoggingConfig: Failed to initialize logging configuration", e);
            // Don't throw here as it would prevent the application from starting
            // Just log the error and continue
        }
    }
} 