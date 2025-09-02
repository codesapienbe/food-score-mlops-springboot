package com.university.foodml.ml;

import com.university.foodml.model.enums.FoodScore;

/**
 * Prediction result with confidence score and additional metadata.
 */
public record PredictionWithConfidence(
    FoodScore predictedScore,
    double confidence,
    double[] classProbabilities,
    String modelVersion,
    long predictionTimestamp
) {
    
    /**
     * Create a prediction with confidence
     */
    public static PredictionWithConfidence of(FoodScore predictedScore, double confidence, 
                                            double[] classProbabilities, String modelVersion) {
        return new PredictionWithConfidence(
            predictedScore, confidence, classProbabilities, modelVersion, System.currentTimeMillis()
        );
    }
    
    /**
     * Create a prediction with confidence (simplified)
     */
    public static PredictionWithConfidence of(FoodScore predictedScore, double confidence, String modelVersion) {
        return new PredictionWithConfidence(
            predictedScore, confidence, new double[0], modelVersion, System.currentTimeMillis()
        );
    }
    
    /**
     * Check if the prediction has high confidence
     */
    public boolean isHighConfidence() {
        return confidence >= 0.8;
    }
    
    /**
     * Check if the prediction has medium confidence
     */
    public boolean isMediumConfidence() {
        return confidence >= 0.6 && confidence < 0.8;
    }
    
    /**
     * Check if the prediction has low confidence
     */
    public boolean isLowConfidence() {
        return confidence < 0.6;
    }
    
    /**
     * Get confidence level description
     */
    public String getConfidenceLevel() {
        if (isHighConfidence()) return "HIGH";
        if (isMediumConfidence()) return "MEDIUM";
        return "LOW";
    }
} 