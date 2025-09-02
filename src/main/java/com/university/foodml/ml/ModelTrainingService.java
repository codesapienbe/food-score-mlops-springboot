package com.university.foodml.ml;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for orchestrating machine learning model training.
 * Handles data preparation, model training, evaluation, and deployment.
 */
@Service
public class ModelTrainingService {

    private static final Logger logger = LoggerFactory.getLogger(ModelTrainingService.class);
    
    private final FeatureEngineeringService featureEngineeringService;
    private final MLModel mlModel;
    private final ModelRegistryService modelRegistryService;
    
    @Autowired
    public ModelTrainingService(FeatureEngineeringService featureEngineeringService,
                              MLModel mlModel,
                              ModelRegistryService modelRegistryService) {
        this.featureEngineeringService = featureEngineeringService;
        this.mlModel = mlModel;
        this.modelRegistryService = modelRegistryService;
    }
    
    /**
     * Train a new model with the provided training data
     */
    public TrainingResult trainModel(List<FoodFeatures> rawTrainingData, List<FoodScore> labels) {
        if (rawTrainingData == null || labels == null || rawTrainingData.size() != labels.size()) {
            throw new IllegalArgumentException("Training data and labels must not be null and must have same size");
        }
        
        if (rawTrainingData.size() < 50) {
            throw new IllegalArgumentException("Need at least 50 training samples for reliable model training");
        }
        
        logger.info("🚀 Starting model training pipeline with {} samples", rawTrainingData.size());
        
        try {
            // Step 1: Feature Engineering
            logger.info("🔧 Step 1: Feature Engineering");
            List<FoodFeatures> enhancedFeatures = featureEngineeringService.prepareTrainingDataset(rawTrainingData);
            
            if (enhancedFeatures.size() < 50) {
                throw new RuntimeException("Insufficient valid samples after feature engineering: " + enhancedFeatures.size());
            }
            
            // Step 2: Convert to arrays for ML model
            logger.info("🔄 Step 2: Data Conversion");
            FoodFeatures[] featuresArray = enhancedFeatures.toArray(new FoodFeatures[0]);
            FoodScore[] labelsArray = labels.toArray(new FoodScore[0]);
            
            // Step 3: Train the model
            logger.info("🤖 Step 3: Model Training");
            TrainingResult trainingResult = mlModel.train(featuresArray, labelsArray);
            
            // Step 4: Evaluate the model
            logger.info("📊 Step 4: Model Evaluation");
            ModelEvaluation evaluation = mlModel.evaluate(featuresArray, labelsArray);
            
            // Step 5: Register the model if it meets requirements
            if (trainingResult.isSuccessful() && evaluation.meetsRequirements()) {
                logger.info("✅ Step 5: Model Registration");
                modelRegistryService.registerModel(mlModel, trainingResult, evaluation);
                logger.info("🎉 Model training pipeline completed successfully!");
            } else {
                logger.warn("⚠️ Model training completed but performance requirements not met");
            }
            
            return trainingResult;
            
        } catch (Exception e) {
            logger.error("❌ Model training pipeline failed", e);
            throw new RuntimeException("Model training failed", e);
        }
    }
    
    /**
     * Asynchronously train a model
     */
    @Async("mlExecutor")
    public CompletableFuture<TrainingResult> trainModelAsync(List<FoodFeatures> rawTrainingData, List<FoodScore> labels) {
        return CompletableFuture.completedFuture(trainModel(rawTrainingData, labels));
    }
    
    /**
     * Retrain model with new data (for automated retraining)
     */
    public TrainingResult retrainModel(List<FoodFeatures> newData, List<FoodScore> newLabels) {
        logger.info("🔄 Starting model retraining with {} new samples", newData.size());
        
        // Get existing training data and combine with new data
        List<FoodFeatures> allTrainingData = modelRegistryService.getCombinedTrainingData(newData);
        List<FoodScore> allLabels = modelRegistryService.getCombinedLabels(newLabels);
        
        // Train new model
        TrainingResult result = trainModel(allTrainingData, allLabels);
        
        // Compare with current model performance
        if (shouldDeployNewModel(result)) {
            logger.info("🚀 New model performance is better, deploying...");
            deployNewModel();
        } else {
            logger.info("⚠️ New model performance is not better, keeping current model");
        }
        
        return result;
    }
    
    /**
     * Check if new model should be deployed based on performance
     */
    private boolean shouldDeployNewModel(TrainingResult newModelResult) {
        // Get current model performance from registry
        ModelEvaluation currentEvaluation = modelRegistryService.getCurrentModelEvaluation();
        
        if (currentEvaluation == null) {
            return true; // No current model, deploy new one
        }
        
        // Deploy if new model is significantly better (5% improvement)
        double accuracyImprovement = newModelResult.accuracy() - currentEvaluation.accuracy();
        double f1Improvement = newModelResult.f1Score() - currentEvaluation.f1Score();
        
        return accuracyImprovement > 0.05 || f1Improvement > 0.05;
    }
    
    /**
     * Deploy the newly trained model
     */
    private void deployNewModel() {
        try {
            // Save the new model
            String modelPath = "models/model-" + System.currentTimeMillis();
            mlModel.saveModel(modelPath);
            
            // Update model registry
            modelRegistryService.updateCurrentModel(mlModel, modelPath);
            
            logger.info("✅ New model deployed successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to deploy new model", e);
            throw new RuntimeException("Model deployment failed", e);
        }
    }
    
    /**
     * Get training status and metrics
     */
    public TrainingStatus getTrainingStatus() {
        return new TrainingStatus(
            mlModel.isTrained(),
            mlModel.getMetadata(),
            LocalDateTime.now()
        );
    }
    
    /**
     * Training status record
     */
    public record TrainingStatus(
        boolean isTrained,
        ModelMetadata metadata,
        LocalDateTime lastChecked
    ) {}
} 