package com.university.foodml.ml.service;

import com.university.foodml.ml.entity.FeatureStoreEntity;
import com.university.foodml.ml.entity.ModelVersionEntity;
import com.university.foodml.ml.entity.TrainingPipelineEntity;
import com.university.foodml.ml.repository.FeatureStoreRepository;
import com.university.foodml.ml.repository.ModelVersionRepository;
import com.university.foodml.ml.repository.TrainingPipelineRepository;
import com.university.foodml.ml.ModelTrainingService;
import com.university.foodml.ml.ModelEvaluation;
import com.university.foodml.ml.ModelMetadata;
import com.university.foodml.ml.ModelRegistryService;
import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import com.university.foodml.ml.TrainingResult;

/**
 * Service for monitoring ML model performance and detecting data drift.
 * Triggers automated retraining when performance degrades or new data arrives.
 */
@Service
public class ModelMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(ModelMonitoringService.class);

    @Autowired
    private ModelVersionRepository modelVersionRepository;

    @Autowired
    private TrainingPipelineRepository trainingPipelineRepository;

    @Autowired
    private ModelTrainingService modelTrainingService;

    // Performance thresholds
    private static final double ACCURACY_THRESHOLD = 0.75;
    private static final double F1_SCORE_THRESHOLD = 0.70;
    private static final double DRIFT_THRESHOLD = 0.15;
    private static final int MIN_SAMPLES_FOR_DRIFT = 100;

    // Monitoring data storage
    private final Map<String, List<Double>> predictionHistory = new ConcurrentHashMap<>();
    private final Map<String, List<FoodFeatures>> featureHistory = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastRetrainingTime = new ConcurrentHashMap<>();

    /**
     * Monitor model performance and trigger retraining if needed
     */
    public void monitorModelPerformance(String modelId) {
        try {
            Optional<ModelVersionEntity> currentModel = modelVersionRepository.findByModelId(modelId);
            if (currentModel.isEmpty()) {
                logger.warn("Model {} not found for monitoring", modelId);
                return;
            }

            ModelVersionEntity model = currentModel.get();
            
            // Check if retraining is needed
            if (shouldRetrainModel(model)) {
                logger.info("🚨 Performance degradation detected for model {}. Triggering retraining...", modelId);
                triggerAutomatedRetraining(model, "PERFORMANCE_DEGRADATION");
            }

            // Check for data drift
            if (detectDataDrift(modelId)) {
                logger.info("🌊 Data drift detected for model {}. Triggering retraining...", modelId);
                triggerAutomatedRetraining(model, "DATA_DRIFT");
            }

            // Check if new data has arrived
            if (hasNewDataArrived(modelId)) {
                logger.info("📊 New data detected for model {}. Triggering retraining...", modelId);
                triggerAutomatedRetraining(model, "NEW_DATA");
            }

        } catch (Exception e) {
            logger.error("Error monitoring model performance for model {}: {}", modelId, e.getMessage(), e);
        }
    }

    /**
     * Determine if model should be retrained based on performance metrics
     */
    private boolean shouldRetrainModel(ModelVersionEntity model) {
        // Check accuracy threshold
        if (model.getAccuracy() < ACCURACY_THRESHOLD) {
            logger.warn("Model {} accuracy {} below threshold {}", 
                model.getModelId(), model.getAccuracy(), ACCURACY_THRESHOLD);
            return true;
        }

        // Check F1 score threshold
        if (model.getF1Score() < F1_SCORE_THRESHOLD) {
            logger.warn("Model {} F1 score {} below threshold {}", 
                model.getModelId(), model.getF1Score(), F1_SCORE_THRESHOLD);
            return true;
        }

        // Check if model is older than 30 days
        if (model.getTrainedAt() != null && 
            ChronoUnit.DAYS.between(model.getTrainedAt(), LocalDateTime.now()) > 30) {
            logger.info("Model {} is older than 30 days. Scheduling retraining...", model.getModelId());
            return true;
        }

        return false;
    }

    /**
     * Detect data drift by comparing current feature distributions with training data
     */
    private boolean detectDataDrift(String modelId) {
        List<FoodFeatures> recentFeatures = featureHistory.get(modelId);
        if (recentFeatures == null || recentFeatures.size() < MIN_SAMPLES_FOR_DRIFT) {
            return false;
        }

        // Calculate feature drift for key nutritional components
        double proteinDrift = calculateFeatureDrift(recentFeatures, FoodFeatures::proteinQuality);
        double sugarDrift = calculateFeatureDrift(recentFeatures, FoodFeatures::sugarQuality);
        double saltDrift = calculateFeatureDrift(recentFeatures, FoodFeatures::saltQuality);

        double maxDrift = Math.max(Math.max(proteinDrift, sugarDrift), saltDrift);
        
        // Trigger retraining if drift is detected
        if (maxDrift > DRIFT_THRESHOLD) {
            logger.warn("🚨 Data drift detected ({}), triggering model retraining", maxDrift);
            try {
                TrainingResult result = modelTrainingService.retrainModel(
                    recentFeatures.stream().limit(100).toList(), 
                    recentFeatures.stream().map(f -> FoodScore.valueOf(f.nutritionGradeFr().toUpperCase())).limit(100).toList()
                );
                logger.info("✅ Model retraining completed with accuracy: {}", result.accuracy());
            } catch (Exception e) {
                logger.error("❌ Model retraining failed", e);
            }
        }

        return false;
    }

    /**
     * Calculate drift for a specific feature
     */
    private <T> double calculateFeatureDrift(List<FoodFeatures> features, 
                                           java.util.function.Function<FoodFeatures, T> featureExtractor) {
        if (features.size() < 2) return 0.0;

        // Simple drift calculation based on feature value changes
        List<T> values = features.stream()
            .map(featureExtractor)
            .filter(Objects::nonNull)
            .toList();

        if (values.size() < 2) return 0.0;

        // Calculate coefficient of variation as drift indicator
        double mean = values.stream()
            .mapToDouble(v -> ((Number) v).doubleValue())
            .average()
            .orElse(0.0);

        if (mean == 0) return 0.0;

        double variance = values.stream()
            .mapToDouble(v -> Math.pow(((Number) v).doubleValue() - mean, 2))
            .average()
            .orElse(0.0);

        double stdDev = Math.sqrt(variance);
        return stdDev / mean;
    }

    /**
     * Check if new data has arrived since last retraining
     */
    private boolean hasNewDataArrived(String modelId) {
        LocalDateTime lastRetraining = lastRetrainingTime.get(modelId);
        if (lastRetraining == null) {
            return false;
        }

        // Check if more than 7 days have passed since last retraining
        long daysSinceRetraining = ChronoUnit.DAYS.between(lastRetraining, LocalDateTime.now());
        
        if (daysSinceRetraining >= 7) {
            logger.info("New data check: {} days since last retraining for model {}", daysSinceRetraining, modelId);
            return true;
        }

        return false;
    }

    /**
     * Trigger automated retraining with specified reason
     */
    private void triggerAutomatedRetraining(ModelVersionEntity model, String triggerType) {
        try {
            // Create training pipeline record
            TrainingPipelineEntity pipeline = new TrainingPipelineEntity(
                generatePipelineId(),
                "Automated Retraining - " + triggerType,
                model.getModelId(),
                TrainingPipelineEntity.TriggerType.valueOf(triggerType)
            );
            
            pipeline.setEnvironment("PRODUCTION");
            pipeline.setTrainingSamples(0); // Will be updated during training
            pipeline.setValidationSamples(0); // Will be updated during training
            
            trainingPipelineRepository.save(pipeline);
            logger.info("Created training pipeline {} for automated retraining", pipeline.getPipelineId());

            // Start retraining
            try {
                TrainingResult result = modelTrainingService.retrainModel(
                    Collections.emptyList(), // No new data for performance-based retraining
                    Collections.emptyList()  // No new labels for performance-based retraining
                );
                logger.info("✅ Performance-based retraining completed with accuracy: {}", result.accuracy());
            } catch (Exception e) {
                logger.error("❌ Performance-based retraining failed", e);
            }
            
            // Update last retraining time
            lastRetrainingTime.put(model.getModelId(), LocalDateTime.now());

        } catch (Exception e) {
            logger.error("Failed to trigger automated retraining for model {}: {}", 
                model.getModelId(), e.getMessage(), e);
        }
    }

    /**
     * Record prediction for monitoring
     */
    public void recordPrediction(String modelId, FoodScore predictedScore, double confidence) {
        predictionHistory.computeIfAbsent(modelId, k -> new ArrayList<>())
            .add(confidence);

        // Keep only last 1000 predictions for memory management
        List<Double> predictions = predictionHistory.get(modelId);
        if (predictions.size() > 1000) {
            predictions.subList(0, predictions.size() - 1000).clear();
        }
    }

    /**
     * Record features for drift detection
     */
    public void recordFeatures(String modelId, FoodFeatures features) {
        featureHistory.computeIfAbsent(modelId, k -> new ArrayList<>())
            .add(features);

        // Keep only last 1000 feature sets for memory management
        List<FoodFeatures> featuresList = featureHistory.get(modelId);
        if (featuresList.size() > 1000) {
            featuresList.subList(0, featuresList.size() - 1000).clear();
        }
    }

    /**
     * Generate unique pipeline ID
     */
    private String generatePipelineId() {
        return "pipeline_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Scheduled monitoring every 6 hours
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // 6 hours
    public void scheduledModelMonitoring() {
        logger.info("🔄 Starting scheduled model monitoring...");
        
        try {
            // Monitor all current models
            Optional<ModelVersionEntity> currentModel = modelVersionRepository.findByIsCurrentTrue();
            if (currentModel.isPresent()) {
                monitorModelPerformance(currentModel.get().getModelId());
            }

            // Check for stuck training pipelines
            List<TrainingPipelineEntity> stuckPipelines = trainingPipelineRepository
                .findByStatus(TrainingPipelineEntity.TrainingStatus.RUNNING);
            
            for (TrainingPipelineEntity pipeline : stuckPipelines) {
                // Check if pipeline has been running for more than 2 hours
                if (pipeline.getStartedAt() != null && 
                    ChronoUnit.HOURS.between(pipeline.getStartedAt(), LocalDateTime.now()) > 2) {
                    logger.warn("Pipeline {} has been running for more than 2 hours. Marking as failed.", 
                        pipeline.getPipelineId());
                    pipeline.failTraining("Pipeline timeout - running for more than 2 hours");
                    trainingPipelineRepository.save(pipeline);
                }
            }

        } catch (Exception e) {
            logger.error("Error during scheduled model monitoring: {}", e.getMessage(), e);
        }
    }

    /**
     * Get monitoring statistics for a model
     */
    public Map<String, Object> getMonitoringStats(String modelId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Double> predictions = predictionHistory.get(modelId);
        if (predictions != null && !predictions.isEmpty()) {
            stats.put("totalPredictions", predictions.size());
            stats.put("averageConfidence", predictions.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            stats.put("minConfidence", predictions.stream().mapToDouble(Double::doubleValue).min().orElse(0.0));
            stats.put("maxConfidence", predictions.stream().mapToDouble(Double::doubleValue).max().orElse(0.0));
        }

        List<FoodFeatures> features = featureHistory.get(modelId);
        if (features != null) {
            stats.put("totalFeatures", features.size());
        }

        LocalDateTime lastRetraining = lastRetrainingTime.get(modelId);
        if (lastRetraining != null) {
            stats.put("lastRetraining", lastRetraining);
            stats.put("daysSinceRetraining", ChronoUnit.DAYS.between(lastRetraining, LocalDateTime.now()));
        }

        return stats;
    }
} 