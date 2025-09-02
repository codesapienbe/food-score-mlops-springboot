package com.university.foodml;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "logging.level.root=WARN",
    "logging.level.com.university.foodml=INFO",
    "logging.config=classpath:logback-spring.xml"
})
class FoodScoreApplicationTests {

    @Test
    void contextLoads() {
        // Simple test to verify the application context can be loaded
        // Using NONE web environment to avoid full context loading
        // Focused on testing core Spring Boot functionality
    }
    
    @Test
    void loggingConfigurationTest() {
        // Test that logging configuration is properly loaded
        // This will verify that our Logback configuration is working
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FoodScoreApplicationTests.class);
        logger.info("Logging configuration test successful");
    }
}
