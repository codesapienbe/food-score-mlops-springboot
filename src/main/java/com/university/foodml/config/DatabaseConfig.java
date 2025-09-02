package com.university.foodml.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Database configuration for performance optimization.
 * Implements connection pooling and query optimization strategies.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.university.foodml.ml.repository")
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * Primary data source with optimized connection pooling.
     * Uses HikariCP for high-performance database connections.
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Optimized data source with connection pooling configuration.
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
        
        // Connection pool optimization
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(5);
        dataSource.setIdleTimeout(300000); // 5 minutes
        dataSource.setConnectionTimeout(20000); // 20 seconds
        dataSource.setMaxLifetime(1800000); // 30 minutes
        
        // Performance optimization
        dataSource.setLeakDetectionThreshold(60000); // 1 minute
        dataSource.setValidationTimeout(5000); // 5 seconds
        
        return dataSource;
    }

    /**
     * JPA properties for performance optimization.
     */
    @Bean
    @ConfigurationProperties("spring.jpa.properties")
    public org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties hibernateProperties() {
        return new org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties();
    }
} 