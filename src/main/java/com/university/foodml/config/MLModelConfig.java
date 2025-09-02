package com.university.foodml.config;

import com.university.foodml.ml.MLModel;
import com.university.foodml.ml.StatisticalMLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for machine learning models.
 * Provides beans for different ML model implementations.
 */
@Configuration
public class MLModelConfig {

    private static final Logger logger = LoggerFactory.getLogger(MLModelConfig.class);

    @Bean
    @Primary
    public MLModel mlModel() {
        logger.info("🤖 Creating Statistical ML model (linear regression approach)");
        return new StatisticalMLModel();
    }
} 