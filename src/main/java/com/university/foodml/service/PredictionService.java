package com.university.foodml.service;

import com.university.foodml.ml.FeatureEngineeringService;
import com.university.foodml.ml.MLModel;
import com.university.foodml.ml.PredictionWithConfidence;
import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PredictionService {

    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);
    
    private final MLModel mlModel;
    private final FeatureEngineeringService featureEngineeringService;

    @Autowired
    public PredictionService(MLModel mlModel, FeatureEngineeringService featureEngineeringService) {
        this.mlModel = mlModel;
        this.featureEngineeringService = featureEngineeringService;
    }

    public FoodScore predictFoodScore(FoodFeatures features) {
        if (features == null) {
            throw new IllegalArgumentException("Food features cannot be null");
        }
        
        try {
            // Check if ML model is trained
            if (!mlModel.isTrained()) {
                logger.warn("⚠️ ML model not trained, falling back to heuristic scoring");
                return predictWithHeuristic(features);
            }
            
            // Enhance features for ML prediction
            FoodFeatures enhancedFeatures = featureEngineeringService.enhanceFeatures(features);
            
            // Get ML prediction
            FoodScore predictedScore = mlModel.predict(enhancedFeatures);
            logger.info("🤖 ML model predicted score: {} for product: {}", predictedScore, features.productName());
            
            return predictedScore;
            
        } catch (Exception e) {
            logger.error("❌ ML prediction failed for product: {}, falling back to heuristic", features.productName(), e);
            return predictWithHeuristic(features);
        }
    }
    
    public PredictionWithConfidence predictWithConfidence(FoodFeatures features) {
        if (features == null) {
            throw new IllegalArgumentException("Food features cannot be null");
        }
        
        try {
            // Check if ML model is trained
            if (!mlModel.isTrained()) {
                logger.warn("⚠️ ML model not trained, cannot provide confidence scores");
                FoodScore score = predictWithHeuristic(features);
                return PredictionWithConfidence.of(score, 0.5, new double[0], "heuristic-1.0.0");
            }
            
            // Enhance features for ML prediction
            FoodFeatures enhancedFeatures = featureEngineeringService.enhanceFeatures(features);
            
            // Get ML prediction with confidence
            PredictionWithConfidence prediction = mlModel.predictWithConfidence(enhancedFeatures);
            logger.info("🤖 ML model prediction with confidence: {} (confidence: {}) for product: {}", 
                       prediction.predictedScore(), prediction.confidence(), features.productName());
            
            return prediction;
            
        } catch (Exception e) {
            logger.error("❌ ML prediction with confidence failed for product: {}", features.productName(), e);
            FoodScore score = predictWithHeuristic(features);
            return PredictionWithConfidence.of(score, 0.5, new double[0], "heuristic-1.0.0");
        }
    }

    @Async("mlExecutor")
    public CompletableFuture<FoodScore> predictFoodScoreAsync(FoodFeatures features) {
        return CompletableFuture.completedFuture(predictFoodScore(features));
    }
    
    @Async("mlExecutor")
    public CompletableFuture<PredictionWithConfidence> predictWithConfidenceAsync(FoodFeatures features) {
        return CompletableFuture.completedFuture(predictWithConfidence(features));
    }
    
    /**
     * Fallback heuristic-based scoring when ML model is not available
     */
    private FoodScore predictWithHeuristic(FoodFeatures features) {
        logger.info("🎯 Using heuristic scoring for: {}", features.productName());
        
        // Simple heuristic-based scoring for demonstration
        int score = 3; // Start with C (average)

        // Positive factors
        if (features.proteins100g() != null && features.proteins100g() >= 10.0) score++;
        if (features.novaGroups() != null && features.novaGroups() <= 2) score++;
        if (features.additivesCount() != null && features.additivesCount() <= 2) score++;

        // Negative factors
        if (features.sugars100g() != null && features.sugars100g() >= 15.0) score--;
        if (features.salt100g() != null && features.salt100g() >= 1.5) score--;
        if (features.novaGroups() != null && features.novaGroups() == 4) score--;

        FoodScore finalScore = FoodScore.fromIntegerScore(Math.max(1, Math.min(5, score)));
        logger.info("✅ Heuristic predicted score: {} for {}", finalScore, features.productName());

        return finalScore;
    }
}