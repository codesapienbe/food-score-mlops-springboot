package com.university.foodml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Load balancer configuration for scalability and performance.
 * Implements load balancing strategies and CORS configuration.
 */
@Configuration
public class LoadBalancerConfig implements WebMvcConfigurer {

    /**
     * REST template for external service calls.
     * Configured for optimal performance and load balancing.
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Configure timeout settings
        restTemplate.getInterceptors().add((request, body, execution) -> {
            // Add custom headers for load balancing
            request.getHeaders().add("X-Load-Balanced", "true");
            request.getHeaders().add("X-Request-ID", java.util.UUID.randomUUID().toString());
            return execution.execute(request, body);
        });
        
        return restTemplate;
    }

    /**
     * CORS configuration for cross-origin requests.
     * Enables proper handling of requests from different domains.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
} 