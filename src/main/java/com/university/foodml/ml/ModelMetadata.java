package com.university.foodml.ml;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Metadata information about a machine learning model.
 */
public record ModelMetadata(
    String modelId,
    String modelName,
    String modelVersion,
    String algorithm,
    String description,
    LocalDateTime createdAt,
    LocalDateTime lastUpdated,
    LocalDateTime lastTrained,
    int trainingSamples,
    Map<String, Object> hyperparameters,
    Map<String, Object> additionalInfo
) {
    
    /**
     * Create basic model metadata
     */
    public static ModelMetadata of(String modelId, String modelName, String modelVersion, 
                                  String algorithm, String description) {
        return new ModelMetadata(
            modelId, modelName, modelVersion, algorithm, description,
            LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
            0, Map.of(), Map.of()
        );
    }
    
    /**
     * Create comprehensive model metadata
     */
    public static ModelMetadata of(String modelId, String modelName, String modelVersion,
                                  String algorithm, String description, int trainingSamples,
                                  Map<String, Object> hyperparameters) {
        return new ModelMetadata(
            modelId, modelName, modelVersion, algorithm, description,
            LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
            trainingSamples, hyperparameters, Map.of()
        );
    }
    
    /**
     * Update training information
     */
    public ModelMetadata withTrainingUpdate(int trainingSamples, LocalDateTime trainingTime) {
        return new ModelMetadata(
            modelId, modelName, modelVersion, algorithm, description,
            createdAt, LocalDateTime.now(), trainingTime,
            trainingSamples, hyperparameters, additionalInfo
        );
    }
    
    /**
     * Get model identifier with version
     */
    public String getFullIdentifier() {
        return String.format("%s-%s", modelName, modelVersion);
    }
    
    /**
     * Check if model is recent (trained within last 30 days)
     */
    public boolean isRecent() {
        return lastTrained.isAfter(LocalDateTime.now().minusDays(30));
    }
    
    /**
     * Get model age in days
     */
    public long getAgeInDays() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toDays();
    }
} 