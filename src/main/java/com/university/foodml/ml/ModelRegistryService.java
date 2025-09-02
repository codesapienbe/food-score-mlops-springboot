package com.university.foodml.ml;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing machine learning model versions, metadata, and deployment.
 * Provides model registry functionality for MLOps lifecycle management.
 */
@Service
public class ModelRegistryService {

    private static final Logger logger = LoggerFactory.getLogger(ModelRegistryService.class);
    
    private final Map<String, ModelVersion> modelVersions = new ConcurrentHashMap<>();
    private final Map<String, ModelEvaluation> modelEvaluations = new ConcurrentHashMap<>();
    private final Map<String, TrainingResult> trainingResults = new ConcurrentHashMap<>();
    
    private String currentModelId = null;
    private final List<FoodFeatures> trainingDataHistory = new ArrayList<>();
    private final List<FoodScore> labelsHistory = new ArrayList<>();
    
    /**
     * Register a new model version
     */
    public void registerModel(MLModel model, TrainingResult trainingResult, ModelEvaluation evaluation) {
        if (model == null || trainingResult == null || evaluation == null) {
            throw new IllegalArgumentException("Model, training result, and evaluation cannot be null");
        }
        
        ModelMetadata metadata = model.getMetadata();
        String modelId = metadata.modelId();
        
        // Create model version record
        ModelVersion version = new ModelVersion(
            modelId,
            metadata.modelVersion(),
            metadata.algorithm(),
            LocalDateTime.now(),
            trainingResult.trainingSamples(),
            trainingResult.accuracy(),
            trainingResult.f1Score(),
            metadata.description()
        );
        
        // Store model information
        modelVersions.put(modelId, version);
        modelEvaluations.put(modelId, evaluation);
        trainingResults.put(modelId, trainingResult);
        
        // Set as current model if it's the first one or better than current
        if (currentModelId == null || isBetterModel(evaluation, getCurrentModelEvaluation())) {
            currentModelId = modelId;
            logger.info("🚀 New model {} registered and set as current model", modelId);
        } else {
            logger.info("📝 New model {} registered but not set as current", modelId);
        }
        
        logger.info("✅ Model registration completed: {}", version);
    }
    
    /**
     * Update the current model
     */
    public void updateCurrentModel(MLModel model, String modelPath) {
        if (model == null || modelPath == null) {
            throw new IllegalArgumentException("Model and model path cannot be null");
        }
        
        ModelMetadata metadata = model.getMetadata();
        String modelId = metadata.modelId();
        
        // Update current model
        currentModelId = modelId;
        
        // Update model path in version info
        ModelVersion version = modelVersions.get(modelId);
        if (version != null) {
            version = version.withModelPath(modelPath);
            modelVersions.put(modelId, version);
        }
        
        logger.info("🔄 Current model updated to: {}", modelId);
    }
    
    /**
     * Get current model evaluation
     */
    public ModelEvaluation getCurrentModelEvaluation() {
        if (currentModelId == null) {
            return null;
        }
        return modelEvaluations.get(currentModelId);
    }
    
    /**
     * Get current model version
     */
    public ModelVersion getCurrentModelVersion() {
        if (currentModelId == null) {
            return null;
        }
        return modelVersions.get(currentModelId);
    }
    
    /**
     * Get all model versions
     */
    public List<ModelVersion> getAllModelVersions() {
        return new ArrayList<>(modelVersions.values());
    }
    
    /**
     * Get model evaluation by ID
     */
    public ModelEvaluation getModelEvaluation(String modelId) {
        return modelEvaluations.get(modelId);
    }
    
    /**
     * Get training result by ID
     */
    public TrainingResult getTrainingResult(String modelId) {
        return trainingResults.get(modelId);
    }
    
    /**
     * Add training data to history
     */
    public void addTrainingData(List<FoodFeatures> features, List<FoodScore> labels) {
        if (features != null && labels != null && features.size() == labels.size()) {
            trainingDataHistory.addAll(features);
            labelsHistory.addAll(labels);
            logger.info("📊 Added {} new training samples to history", features.size());
        }
    }
    
    /**
     * Get combined training data (existing + new)
     */
    public List<FoodFeatures> getCombinedTrainingData(List<FoodFeatures> newData) {
        List<FoodFeatures> combined = new ArrayList<>(trainingDataHistory);
        if (newData != null) {
            combined.addAll(newData);
        }
        return combined;
    }
    
    /**
     * Get combined labels (existing + new)
     */
    public List<FoodScore> getCombinedLabels(List<FoodScore> newLabels) {
        List<FoodScore> combined = new ArrayList<>(labelsHistory);
        if (newLabels != null) {
            combined.addAll(newLabels);
        }
        return combined;
    }
    
    /**
     * Compare two model evaluations to determine which is better
     */
    private boolean isBetterModel(ModelEvaluation newModel, ModelEvaluation currentModel) {
        if (currentModel == null) {
            return true; // New model is better if there's no current model
        }
        
        // Compare accuracy and F1 score
        double accuracyImprovement = newModel.accuracy() - currentModel.accuracy();
        double f1Improvement = newModel.f1Score() - currentModel.f1Score();
        
        // New model is better if it shows significant improvement
        return accuracyImprovement > 0.02 || f1Improvement > 0.02;
    }
    
    /**
     * Get model registry statistics
     */
    public ModelRegistryStats getRegistryStats() {
        return new ModelRegistryStats(
            modelVersions.size(),
            currentModelId,
            trainingDataHistory.size(),
            LocalDateTime.now()
        );
    }
    
    /**
     * Model version record
     */
    public static class ModelVersion {
        private final String modelId;
        private final String version;
        private final String algorithm;
        private final LocalDateTime registeredAt;
        private final int trainingSamples;
        private final double accuracy;
        private final double f1Score;
        private final String description;
        private String modelPath;
        
        public ModelVersion(String modelId, String version, String algorithm, LocalDateTime registeredAt,
                           int trainingSamples, double accuracy, double f1Score, String description) {
            this.modelId = modelId;
            this.version = version;
            this.algorithm = algorithm;
            this.registeredAt = registeredAt;
            this.trainingSamples = trainingSamples;
            this.accuracy = accuracy;
            this.f1Score = f1Score;
            this.description = description;
        }
        
        public ModelVersion withModelPath(String modelPath) {
            this.modelPath = modelPath;
            return this;
        }
        
        // Getters
        public String modelId() { return modelId; }
        public String version() { return version; }
        public String algorithm() { return algorithm; }
        public LocalDateTime registeredAt() { return registeredAt; }
        public int trainingSamples() { return trainingSamples; }
        public double accuracy() { return accuracy; }
        public double f1Score() { return f1Score; }
        public String description() { return description; }
        public String modelPath() { return modelPath; }
        
        @Override
        public String toString() {
            return String.format("ModelVersion{id=%s, version=%s, accuracy=%.3f, f1=%.3f, samples=%d}",
                               modelId, version, accuracy, f1Score, trainingSamples);
        }
    }
    
    /**
     * Model registry statistics
     */
    public record ModelRegistryStats(
        int totalModels,
        String currentModelId,
        int totalTrainingSamples,
        LocalDateTime lastUpdated
    ) {}
} 