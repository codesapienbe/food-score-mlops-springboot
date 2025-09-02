package com.university.foodml.ml.service;

import com.university.foodml.ml.entity.FeatureStoreEntity;
import com.university.foodml.ml.repository.FeatureStoreRepository;
import com.university.foodml.model.dto.FoodFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for detecting data drift in ML features.
 * Monitors feature distributions and alerts when significant changes occur.
 */
@Service
public class DataDriftDetectionService {

    private static final Logger logger = LoggerFactory.getLogger(DataDriftDetectionService.class);

    @Autowired
    private FeatureStoreRepository featureStoreRepository;

    // Drift detection configuration
    private static final double DRIFT_THRESHOLD = 0.15;
    private static final double SEVERE_DRIFT_THRESHOLD = 0.30;
    private static final int MIN_SAMPLES_FOR_DRIFT = 50;
    private static final int MAX_HISTORY_SIZE = 1000;

    // Feature history storage for drift detection
    private final Map<String, List<Double>> featureHistory = new ConcurrentHashMap<>();
    private final Map<String, Double> baselineStats = new ConcurrentHashMap<>();

    /**
     * Analyze features for potential data drift
     */
    public Map<String, Object> detectDataDrift(List<FoodFeatures> currentFeatures) {
        Map<String, Object> driftReport = new HashMap<>();
        
        if (currentFeatures.size() < MIN_SAMPLES_FOR_DRIFT) {
            driftReport.put("status", "INSUFFICIENT_DATA");
            driftReport.put("message", "Not enough samples for drift detection");
            return driftReport;
        }

        try {
            // Calculate drift for each numerical feature
            Map<String, Double> featureDrift = calculateFeatureDrift(currentFeatures);
            
            // Determine overall drift status
            String driftStatus = determineDriftStatus(featureDrift);
            
            // Generate drift report
            driftReport.put("status", driftStatus);
            driftReport.put("timestamp", new Date());
            driftReport.put("sampleCount", currentFeatures.size());
            driftReport.put("featureDrift", featureDrift);
            driftReport.put("overallDriftScore", calculateOverallDriftScore(featureDrift));
            driftReport.put("driftThreshold", DRIFT_THRESHOLD);
            driftReport.put("severeDriftThreshold", SEVERE_DRIFT_THRESHOLD);
            
            // Add recommendations
            driftReport.put("recommendations", generateDriftRecommendations(featureDrift));
            
            // Update feature history
            updateFeatureHistory(currentFeatures);
            
            logger.info("Data drift analysis completed. Status: {}, Overall score: {}", 
                driftStatus, driftReport.get("overallDriftScore"));

        } catch (Exception e) {
            logger.error("Error during data drift detection: {}", e.getMessage(), e);
            driftReport.put("status", "ERROR");
            driftReport.put("error", e.getMessage());
        }

        return driftReport;
    }

    /**
     * Calculate drift for individual features
     */
    private Map<String, Double> calculateFeatureDrift(List<FoodFeatures> currentFeatures) {
        Map<String, Double> driftScores = new HashMap<>();
        
        // Key numerical features to monitor
        String[] featuresToMonitor = {
            "proteinQuality", "sugarQuality", "saltQuality", "energyDensity", 
            "novaQuality", "additiveQuality", "nutritionalBalance"
        };

        for (String featureName : featuresToMonitor) {
            try {
                double driftScore = calculateSingleFeatureDrift(featureName, currentFeatures);
                driftScores.put(featureName, driftScore);
            } catch (Exception e) {
                logger.warn("Could not calculate drift for feature {}: {}", featureName, e.getMessage());
                driftScores.put(featureName, 0.0);
            }
        }

        return driftScores;
    }

    /**
     * Calculate drift for a single feature
     */
    private double calculateSingleFeatureDrift(String featureName, List<FoodFeatures> currentFeatures) {
        // Extract feature values
        List<Double> currentValues = extractFeatureValues(featureName, currentFeatures);
        
        if (currentValues.size() < MIN_SAMPLES_FOR_DRIFT) {
            return 0.0;
        }

        // Get baseline statistics
        Double baselineMean = baselineStats.get(featureName + "_mean");
        Double baselineStd = baselineStats.get(featureName + "_std");
        
        if (baselineMean == null || baselineStd == null) {
            // Initialize baseline if not available
            initializeBaseline(featureName, currentValues);
            return 0.0;
        }

        // Calculate current statistics
        double currentMean = currentValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double currentStd = calculateStandardDeviation(currentValues, currentMean);

        // Calculate drift using multiple metrics
        double meanDrift = Math.abs(currentMean - baselineMean) / (baselineStd + 1e-8);
        double stdDrift = Math.abs(currentStd - baselineStd) / (baselineStd + 1e-8);
        
        // Combined drift score
        double driftScore = (meanDrift + stdDrift) / 2.0;
        
        return Math.min(driftScore, 1.0); // Cap at 1.0
    }

    /**
     * Extract values for a specific feature
     */
    private List<Double> extractFeatureValues(String featureName, List<FoodFeatures> features) {
        return features.stream()
            .map(feature -> {
                Double value = null;
                switch (featureName) {
                    case "proteinQuality": value = feature.proteinQuality(); break;
                    case "sugarQuality": value = feature.sugarQuality(); break;
                    case "saltQuality": value = feature.saltQuality(); break;
                    case "energyDensity": value = feature.energyDensity(); break;
                    case "novaQuality": value = feature.novaQuality() != null ? feature.novaQuality().doubleValue() : 0.0; break;
                    case "additiveQuality": value = feature.additiveQuality(); break;
                    case "nutritionalBalance": value = feature.nutritionalBalance(); break;
                    default: value = 0.0; break;
                }
                return value;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Initialize baseline statistics for a feature
     */
    private void initializeBaseline(String featureName, List<Double> values) {
        if (values.size() < MIN_SAMPLES_FOR_DRIFT) return;
        
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double std = calculateStandardDeviation(values, mean);
        
        baselineStats.put(featureName + "_mean", mean);
        baselineStats.put(featureName + "_std", std);
        
        logger.info("Initialized baseline for feature {}: mean={}, std={}", featureName, mean, std);
    }

    /**
     * Calculate standard deviation
     */
    private double calculateStandardDeviation(List<Double> values, double mean) {
        if (values.size() < 2) return 0.0;
        
        double variance = values.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
        
        return Math.sqrt(variance);
    }

    /**
     * Determine overall drift status
     */
    private String determineDriftStatus(Map<String, Double> featureDrift) {
        if (featureDrift.isEmpty()) return "UNKNOWN";
        
        double maxDrift = featureDrift.values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0.0);
        
        if (maxDrift >= SEVERE_DRIFT_THRESHOLD) {
            return "SEVERE_DRIFT";
        } else if (maxDrift >= DRIFT_THRESHOLD) {
            return "MODERATE_DRIFT";
        } else {
            return "NO_DRIFT";
        }
    }

    /**
     * Calculate overall drift score
     */
    private double calculateOverallDriftScore(Map<String, Double> featureDrift) {
        if (featureDrift.isEmpty()) return 0.0;
        
        return featureDrift.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    /**
     * Generate recommendations based on drift detection
     */
    private List<String> generateDriftRecommendations(Map<String, Double> featureDrift) {
        List<String> recommendations = new ArrayList<>();
        
        // Find features with severe drift
        List<String> severeDriftFeatures = featureDrift.entrySet().stream()
            .filter(entry -> entry.getValue() >= SEVERE_DRIFT_THRESHOLD)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        if (!severeDriftFeatures.isEmpty()) {
            recommendations.add("IMMEDIATE ACTION REQUIRED: Severe drift detected in features: " + 
                String.join(", ", severeDriftFeatures));
            recommendations.add("Consider immediate model retraining");
            recommendations.add("Investigate data source changes");
        }
        
        // Find features with moderate drift
        List<String> moderateDriftFeatures = featureDrift.entrySet().stream()
            .filter(entry -> entry.getValue() >= DRIFT_THRESHOLD && entry.getValue() < SEVERE_DRIFT_THRESHOLD)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        if (!moderateDriftFeatures.isEmpty()) {
            recommendations.add("Monitor features with moderate drift: " + 
                String.join(", ", moderateDriftFeatures));
            recommendations.add("Schedule model retraining within 1-2 weeks");
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("No immediate action required");
            recommendations.add("Continue monitoring feature distributions");
        }
        
        return recommendations;
    }

    /**
     * Update feature history for drift detection
     */
    private void updateFeatureHistory(List<FoodFeatures> currentFeatures) {
        // Update history for each feature
        String[] featuresToMonitor = {
            "proteinQuality", "sugarQuality", "saltQuality", "energyDensity", 
            "novaQuality", "additiveQuality", "nutritionalBalance"
        };

        for (String featureName : featuresToMonitor) {
            List<Double> values = extractFeatureValues(featureName, currentFeatures);
            if (!values.isEmpty()) {
                double avgValue = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                
                featureHistory.computeIfAbsent(featureName, k -> new ArrayList<>())
                    .add(avgValue);
                
                // Limit history size
                List<Double> history = featureHistory.get(featureName);
                if (history.size() > MAX_HISTORY_SIZE) {
                    history.subList(0, history.size() - MAX_HISTORY_SIZE).clear();
                }
            }
        }
    }

    /**
     * Get drift detection statistics
     */
    public Map<String, Object> getDriftDetectionStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("monitoredFeatures", featureHistory.keySet());
        stats.put("baselineFeatures", baselineStats.keySet().stream()
            .map(key -> key.replace("_mean", "").replace("_std", ""))
            .distinct()
            .collect(Collectors.toList()));
        stats.put("driftThreshold", DRIFT_THRESHOLD);
        stats.put("severeDriftThreshold", SEVERE_DRIFT_THRESHOLD);
        stats.put("minSamplesForDrift", MIN_SAMPLES_FOR_DRIFT);
        
        return stats;
    }

    /**
     * Reset baseline statistics (useful for manual recalibration)
     */
    public void resetBaseline() {
        baselineStats.clear();
        logger.info("Baseline statistics reset");
    }
} 