package com.university.foodml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security configuration for the Food Score MLOps application.
 * Implements input validation, authentication, and authorization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain with custom security policies.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for API endpoints
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console for development
                .requestMatchers("/api/v1/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(inputValidationFilter(), UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions().disable()); // Allow H2 console frames
        
        return http.build();
    }

    /**
     * Custom input validation filter for security.
     * Validates and sanitizes all incoming requests.
     */
    @Bean
    public OncePerRequestFilter inputValidationFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, 
                                         HttpServletResponse response, 
                                         FilterChain filterChain) 
                    throws ServletException, IOException {
                
                // Log security-relevant information
                logSecurityEvent(request);
                
                // Basic input validation
                if (isMaliciousRequest(request)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Invalid request detected");
                    return;
                }
                
                filterChain.doFilter(request, response);
            }
            
            private boolean isMaliciousRequest(HttpServletRequest request) {
                String userAgent = request.getHeader("User-Agent");
                String queryString = request.getQueryString();
                
                // Check for suspicious patterns
                if (userAgent != null && userAgent.toLowerCase().contains("sqlmap")) {
                    return true; // SQL injection tool detected
                }
                
                if (queryString != null && queryString.toLowerCase().contains("script")) {
                    return true; // XSS attempt detected
                }
                
                return false;
            }
            
            private void logSecurityEvent(HttpServletRequest request) {
                // Log security-relevant information for monitoring
                String correlationId = java.util.UUID.randomUUID().toString();
                org.slf4j.MDC.put("correlation_id", correlationId);
                org.slf4j.MDC.put("user_id", "anonymous");
                org.slf4j.MDC.put("request_id", request.getRequestId());
                
                org.slf4j.LoggerFactory.getLogger(SecurityConfig.class)
                    .info("Security validation for request: {} {}", 
                          request.getMethod(), request.getRequestURI());
                
                org.slf4j.MDC.clear();
            }
        };
    }
} 