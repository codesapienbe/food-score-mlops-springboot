package com.university.foodml.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

/**
 * Cache configuration for performance optimization.
 * Implements caching strategies for frequently accessed data.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Primary cache manager for the application.
     * Uses in-memory caching for development and testing.
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList(
            "product-search",
            "product-barcode", 
            "food-predictions",
            "model-versions",
            "feature-store",
            "training-pipelines"
        ));
        return cacheManager;
    }

    /**
     * Cache configuration for ML model predictions.
     * Optimizes inference performance with result caching.
     */
    @Bean("predictionCacheManager")
    public CacheManager predictionCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList(
            "ml-predictions",
            "feature-cache",
            "model-cache"
        ));
        return cacheManager;
    }

    /**
     * Cache configuration for OpenFoodFacts data.
     * Reduces external API calls and improves response times.
     */
    @Bean("foodDataCacheManager")
    public CacheManager foodDataCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(Arrays.asList(
            "product-details",
            "nutrition-data",
            "ingredient-analysis"
        ));
        return cacheManager;
    }
} 