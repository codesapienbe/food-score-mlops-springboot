package com.university.foodml.ml;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Result of model training operation containing metrics and performance indicators.
 */
public record TrainingResult(
    double accuracy,
    double precision,
    double recall,
    double f1Score,
    double trainingTimeSeconds,
    int trainingSamples,
    int validationSamples,
    Map<String, Double> additionalMetrics,
    LocalDateTime trainingCompletedAt,
    String modelVersion
) {
    
    /**
     * Create a training result with basic metrics
     */
    public static TrainingResult of(double accuracy, double precision, double recall, double f1Score,
                                   double trainingTimeSeconds, int trainingSamples, int validationSamples) {
        return new TrainingResult(
            accuracy, precision, recall, f1Score, trainingTimeSeconds, 
            trainingSamples, validationSamples, Map.of(), LocalDateTime.now(), "1.0.0"
        );
    }
    
    /**
     * Create a training result with additional metrics
     */
    public static TrainingResult of(double accuracy, double precision, double recall, double f1Score,
                                   double trainingTimeSeconds, int trainingSamples, int validationSamples,
                                   Map<String, Double> additionalMetrics, String modelVersion) {
        return new TrainingResult(
            accuracy, precision, recall, f1Score, trainingTimeSeconds, 
            trainingSamples, validationSamples, additionalMetrics, LocalDateTime.now(), modelVersion
        );
    }
    
    /**
     * Check if the training was successful based on minimum performance thresholds
     */
    public boolean isSuccessful() {
        return accuracy >= 0.7 && f1Score >= 0.6;
    }
    
    /**
     * Get a summary of the training results
     */
    public String getSummary() {
        return String.format(
            "Training completed in %.2f seconds. Accuracy: %.3f, F1: %.3f, Samples: %d/%d",
            trainingTimeSeconds, accuracy, f1Score, trainingSamples, validationSamples
        );
    }
} 