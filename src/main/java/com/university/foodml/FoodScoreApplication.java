package com.university.foodml;

import com.university.foodml.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class FoodScoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(FoodScoreApplication.class);

    public static void main(String[] args) {
        String correlationId = java.util.UUID.randomUUID().toString();
        
        // Set correlation ID for structured logging
        org.slf4j.MDC.put("correlation_id", correlationId);
        
        logger.info("Starting Food Score MLOps Pipeline");
        logger.info("JDK Version: {}", System.getProperty("java.version"));
        logger.info("Virtual Threads: Enabled (Java 21+)");
        logger.info("LangChain4J: Integrated");
        logger.info("OpenFoodFacts: Ready");
        logger.info("Food Score Classification: A (Best) → E (Worst)");
        logger.info("MLOps Pipeline: Ready");

        try {
            SpringApplication.run(FoodScoreApplication.class, args);
            logger.info("Food Score MLOps Pipeline started successfully");
        } catch (Exception e) {
            logger.error("Failed to start Food Score MLOps Pipeline", e);
            throw e;
        } finally {
            org.slf4j.MDC.clear();
        }
    }
}