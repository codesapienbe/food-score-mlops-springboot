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
    "logging.level.com.university.foodml=INFO"
})
class FoodScoreApplicationTests {

    @Test
    void contextLoads() {
        // Simple test to verify the application context can be loaded
        // Using NONE web environment to avoid full context loading
        // Focused on testing core Spring Boot functionality
    }
}
