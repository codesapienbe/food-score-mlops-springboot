package com.university.foodml;

import com.university.foodml.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class FoodScoreApplication {

    private static final Logger logger = LoggerFactory.getLogger(FoodScoreApplication.class);

    public static void main(String[] args) {
        // Test basic logging first to ensure the system is working
        try {
            logger.info("Testing logging system initialization...");
            logger.info("JDK Version: {}", System.getProperty("java.version"));
            logger.info("Logging system test successful");
        } catch (Exception e) {
            System.err.println("Logging system test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        logger.info("Starting Food Score MLOps Pipeline");
        logger.info("Virtual Threads: Enabled (Java 21+)");
        logger.info("LangChain4J: Integrated");
        logger.info("OpenFoodFacts: Ready");
        logger.info("Food Score Classification: A (Best) → E (Worst)");
        logger.info("MLOps Pipeline: Ready");

        try {
            ConfigurableApplicationContext context = SpringApplication.run(FoodScoreApplication.class, args);
            
            // Now that Spring Boot is initialized, we can use structured logging
            LoggingService loggingService = context.getBean(LoggingService.class);
            loggingService.logStartupEvent("Application", "Food Score MLOps Pipeline started successfully", 
                "JDK: " + System.getProperty("java.version") + ", Spring Boot: " + 
                context.getEnvironment().getProperty("spring.boot.version", "3.2.12"));
                
            logger.info("Food Score MLOps Pipeline started successfully");
        } catch (Exception e) {
            logger.error("Failed to start Food Score MLOps Pipeline", e);
            throw e;
        }
    }
}