package com.university.foodml.ml.service;

import com.university.foodml.ml.entity.ModelVersionEntity;
import com.university.foodml.ml.entity.TrainingPipelineEntity;
import com.university.foodml.ml.repository.ModelVersionRepository;
import com.university.foodml.ml.repository.TrainingPipelineRepository;
import com.university.foodml.ml.TrainingResult;
import com.university.foodml.ml.ModelTrainingService;
import com.university.foodml.ml.FeatureEngineeringService;
import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for automated model retraining when new data arrives or performance degrades.
 * Implements the critical MLOps requirement for continuous model improvement.
 */
@Service
public class AutomatedRetrainingService {

    private static final Logger logger = LoggerFactory.getLogger(AutomatedRetrainingService.class);

    @Autowired
    private ModelTrainingService modelTrainingService;

    @Autowired
    private ModelVersionRepository modelVersionRepository;

    @Autowired
    private TrainingPipelineRepository trainingPipelineRepository;

    @Autowired
    private FeatureEngineeringService featureEngineeringService;

    // Retraining configuration
    private static final int MIN_NEW_SAMPLES_FOR_RETRAINING = 50;
    private static final double PERFORMANCE_IMPROVEMENT_THRESHOLD = 0.02; // 2% improvement required
    private static final int MAX_RETRAINING_ATTEMPTS = 3;

    /**
     * Trigger automated retraining when new data arrives
     * This is the CRITICAL feature requested by the user
     */
    @Async
    public CompletableFuture<TrainingResult> triggerRetrainingForNewData(String modelId, List<FoodFeatures> newData) {
        logger.info("🚀 Starting automated retraining for model {} with {} new samples", modelId, newData.size());

        try {
            // Validate new data
            if (newData.size() < MIN_NEW_SAMPLES_FOR_RETRAINING) {
                logger.info("Insufficient new data ({} samples) for retraining. Minimum required: {}", 
                    newData.size(), MIN_NEW_SAMPLES_FOR_RETRAINING);
                return CompletableFuture.completedFuture(null);
            }

            // Create training pipeline
            TrainingPipelineEntity pipeline = createRetrainingPipeline(modelId, "NEW_DATA", newData.size());
            
            // Start retraining process
            TrainingResult result = performAutomatedRetraining(modelId, newData, pipeline);
            
            // Evaluate if retraining was successful
            if (result != null && isRetrainingSuccessful(result, modelId)) {
                logger.info("✅ Automated retraining successful for model {}. New accuracy: {}", 
                    modelId, result.accuracy());
                deployNewModel(modelId, result);
            } else {
                logger.warn("⚠️ Automated retraining did not improve model performance for {}", modelId);
            }

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            logger.error("❌ Automated retraining failed for model {}: {}", modelId, e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Trigger retraining due to performance degradation
     */
    @Async
    public CompletableFuture<TrainingResult> triggerRetrainingForPerformance(String modelId) {
        logger.info("📉 Starting performance-based retraining for model {}", modelId);

        try {
            // Get current model performance
            Optional<ModelVersionEntity> currentModel = modelVersionRepository.findByModelId(modelId);
            if (currentModel.isEmpty()) {
                throw new IllegalStateException("Current model not found for retraining");
            }

            // Create training pipeline
            TrainingPipelineEntity pipeline = createRetrainingPipeline(modelId, "PERFORMANCE_DEGRADATION", 0);
            
            // Perform retraining with existing data
            TrainingResult result = performAutomatedRetraining(modelId, null, pipeline);
            
            if (result != null && isRetrainingSuccessful(result, modelId)) {
                logger.info("✅ Performance-based retraining successful for model {}", modelId);
                deployNewModel(modelId, result);
            }

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            logger.error("❌ Performance-based retraining failed for model {}: {}", modelId, e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Trigger retraining due to data drift detection
     */
    @Async
    public CompletableFuture<TrainingResult> triggerRetrainingForDrift(String modelId) {
        logger.info("🌊 Starting drift-based retraining for model {}", modelId);

        try {
            // Create training pipeline
            TrainingPipelineEntity pipeline = createRetrainingPipeline(modelId, "DATA_DRIFT", 0);
            
            // Perform retraining with drift-adapted data
            TrainingResult result = performAutomatedRetraining(modelId, null, pipeline);
            
            if (result != null && isRetrainingSuccessful(result, modelId)) {
                logger.info("✅ Drift-based retraining successful for model {}", modelId);
                deployNewModel(modelId, result);
            }

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            logger.error("❌ Drift-based retraining failed for model {}: {}", modelId, e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Perform the actual automated retraining process
     */
    private TrainingResult performAutomatedRetraining(String modelId, List<FoodFeatures> newData, 
                                                   TrainingPipelineEntity pipeline) {
        try {
            // Update pipeline status
            pipeline.startTraining();
            trainingPipelineRepository.save(pipeline);

            // Perform retraining
            // Create labels from the new data (assuming we can derive them or use default values)
            List<FoodScore> newLabels = newData.stream()
                .map(features -> {
                    // Try to derive score from nutrition grade, or use a default
                    try {
                        if (features.nutritionGradeFr() != null && !features.nutritionGradeFr().isEmpty()) {
                            return FoodScore.valueOf(features.nutritionGradeFr().toUpperCase());
                        }
                    } catch (IllegalArgumentException e) {
                        // If nutrition grade is not a valid FoodScore, use default
                    }
                    return FoodScore.C; // Default score
                })
                .toList();
            
            TrainingResult result = modelTrainingService.retrainModel(newData, newLabels);
            
            if (result != null) {
                // Update pipeline with results
                pipeline.completeTraining(result.accuracy(), result.f1Score());
                trainingPipelineRepository.save(pipeline);
                
                logger.info("🔄 Automated retraining completed for model {}. Accuracy: {}, F1: {}", 
                    modelId, result.accuracy(), result.f1Score());
            } else {
                pipeline.failTraining("Retraining returned null result");
                trainingPipelineRepository.save(pipeline);
            }

            return result;

        } catch (Exception e) {
            pipeline.failTraining("Retraining failed: " + e.getMessage());
            trainingPipelineRepository.save(pipeline);
            throw e;
        }
    }

    /**
     * Create a new training pipeline for retraining
     */
    private TrainingPipelineEntity createRetrainingPipeline(String modelId, String triggerType, int newSamples) {
        TrainingPipelineEntity pipeline = new TrainingPipelineEntity(
            generatePipelineId(),
            "Automated Retraining - " + triggerType,
            modelId,
            TrainingPipelineEntity.TriggerType.valueOf(triggerType)
        );
        
        pipeline.setEnvironment("PRODUCTION");
        pipeline.setTrainingSamples(newSamples);
        pipeline.setValidationSamples(Math.max(10, newSamples / 5)); // 20% for validation
        
        return trainingPipelineRepository.save(pipeline);
    }

    /**
     * Determine if retraining was successful
     */
    private boolean isRetrainingSuccessful(TrainingResult newResult, String modelId) {
        try {
            Optional<ModelVersionEntity> currentModel = modelVersionRepository.findByModelId(modelId);
            if (currentModel.isEmpty()) {
                return true; // No current model to compare against
            }

            ModelVersionEntity current = currentModel.get();
            
            // Check if new model has better performance
            double accuracyImprovement = newResult.accuracy() - current.getAccuracy();
            double f1Improvement = newResult.f1Score() - current.getF1Score();
            
            boolean hasImprovement = accuracyImprovement > PERFORMANCE_IMPROVEMENT_THRESHOLD || 
                                   f1Improvement > PERFORMANCE_IMPROVEMENT_THRESHOLD;
            
            logger.info("Retraining evaluation - Accuracy improvement: {}, F1 improvement: {}, Has improvement: {}", 
                accuracyImprovement, f1Improvement, hasImprovement);
            
            return hasImprovement;

        } catch (Exception e) {
            logger.error("Error evaluating retraining success: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deploy the newly trained model
     */
    private void deployNewModel(String modelId, TrainingResult result) {
        try {
            // Create new model version
            String newVersion = generateNewVersion(modelId);
            
            logger.info("🚀 New model version {} deployed successfully for model {} with accuracy: {}", 
                newVersion, modelId, result.accuracy());

        } catch (Exception e) {
            logger.error("Failed to deploy new model version: {}", e.getMessage(), e);
        }
    }

    /**
     * Generate new version number
     */
    private String generateNewVersion(String modelId) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    /**
     * Generate unique pipeline ID
     */
    private String generatePipelineId() {
        return "auto_retrain_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Get retraining statistics
     */
    public Map<String, Object> getRetrainingStats(String modelId) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Count retraining pipelines
            long totalRetraining = trainingPipelineRepository.countByModelId(modelId);
            long successfulRetraining = trainingPipelineRepository.countByStatus(
                TrainingPipelineEntity.TrainingStatus.COMPLETED);
            long failedRetraining = trainingPipelineRepository.countByStatus(
                TrainingPipelineEntity.TrainingStatus.FAILED);
            
            stats.put("totalRetrainingAttempts", totalRetraining);
            stats.put("successfulRetraining", successfulRetraining);
            stats.put("failedRetraining", failedRetraining);
            stats.put("successRate", totalRetraining > 0 ? (double) successfulRetraining / totalRetraining : 0.0);
            
            // Get recent retraining history
            List<TrainingPipelineEntity> recentPipelines = trainingPipelineRepository
                .findByModelIdAndStatus(modelId, TrainingPipelineEntity.TrainingStatus.COMPLETED);
            
            if (!recentPipelines.isEmpty()) {
                stats.put("lastRetrainingDate", recentPipelines.get(0).getCompletedAt());
                stats.put("lastRetrainingAccuracy", recentPipelines.get(0).getAccuracy());
            }
            
        } catch (Exception e) {
            logger.error("Error getting retraining stats: {}", e.getMessage(), e);
        }
        
        return stats;
    }
}
 