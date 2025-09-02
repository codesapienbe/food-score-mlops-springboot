package com.university.foodml.ml;

import com.university.foodml.model.enums.FoodScore;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Comprehensive model evaluation results including various performance metrics.
 */
public record ModelEvaluation(
    double accuracy,
    double precision,
    double recall,
    double f1Score,
    double macroPrecision,
    double macroRecall,
    double macroF1,
    Map<FoodScore, Double> perClassPrecision,
    Map<FoodScore, Double> perClassRecall,
    Map<FoodScore, Double> perClassF1,
    int totalSamples,
    Map<FoodScore, Integer> classDistribution,
    double evaluationTimeSeconds,
    LocalDateTime evaluatedAt,
    String modelVersion
) {
    
    /**
     * Create evaluation result with basic metrics
     */
    public static ModelEvaluation of(double accuracy, double precision, double recall, double f1Score,
                                    int totalSamples, String modelVersion) {
        return new ModelEvaluation(
            accuracy, precision, recall, f1Score, 0.0, 0.0, 0.0,
            Map.of(), Map.of(), Map.of(), totalSamples, Map.of(),
            0.0, LocalDateTime.now(), modelVersion
        );
    }
    
    /**
     * Create comprehensive evaluation result
     */
    public static ModelEvaluation of(double accuracy, double precision, double recall, double f1Score,
                                    double macroPrecision, double macroRecall, double macroF1,
                                    Map<FoodScore, Double> perClassPrecision,
                                    Map<FoodScore, Double> perClassRecall,
                                    Map<FoodScore, Double> perClassF1,
                                    int totalSamples, Map<FoodScore, Integer> classDistribution,
                                    double evaluationTimeSeconds, String modelVersion) {
        return new ModelEvaluation(
            accuracy, precision, recall, f1Score, macroPrecision, macroRecall, macroF1,
            perClassPrecision, perClassRecall, perClassF1, totalSamples, classDistribution,
            evaluationTimeSeconds, LocalDateTime.now(), modelVersion
        );
    }
    
    /**
     * Check if the model meets minimum performance requirements
     */
    public boolean meetsRequirements() {
        return accuracy >= 0.75 && f1Score >= 0.7;
    }
    
    /**
     * Get overall performance grade
     */
    public String getPerformanceGrade() {
        if (accuracy >= 0.9 && f1Score >= 0.85) return "A+";
        if (accuracy >= 0.85 && f1Score >= 0.8) return "A";
        if (accuracy >= 0.8 && f1Score >= 0.75) return "B+";
        if (accuracy >= 0.75 && f1Score >= 0.7) return "B";
        if (accuracy >= 0.7 && f1Score >= 0.65) return "C+";
        if (accuracy >= 0.65 && f1Score >= 0.6) return "C";
        return "D";
    }
    
    /**
     * Get evaluation summary
     */
    public String getSummary() {
        return String.format(
            "Model %s - Grade: %s, Accuracy: %.3f, F1: %.3f, Samples: %d",
            modelVersion, getPerformanceGrade(), accuracy, f1Score, totalSamples
        );
    }
} 