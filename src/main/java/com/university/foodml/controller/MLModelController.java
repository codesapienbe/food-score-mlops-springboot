package com.university.foodml.controller;

import com.university.foodml.ml.ModelTrainingService;
import com.university.foodml.ml.ModelRegistryService;
import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for machine learning model management and training operations.
 * Provides endpoints for model training, evaluation, and monitoring.
 */
@RestController
@RequestMapping("/api/v1/ml")
@CrossOrigin(origins = "*")
public class MLModelController {

    private static final Logger logger = LoggerFactory.getLogger(MLModelController.class);
    
    private final ModelTrainingService modelTrainingService;
    private final ModelRegistryService modelRegistryService;

    @Autowired
    public MLModelController(ModelTrainingService modelTrainingService, 
                           ModelRegistryService modelRegistryService) {
        this.modelTrainingService = modelTrainingService;
        this.modelRegistryService = modelRegistryService;
    }

    /**
     * Train a new ML model with provided training data
     */
    @PostMapping("/train")
    public ResponseEntity<TrainingResponse> trainModel(@Valid @RequestBody TrainingRequest request) {
        try {
            logger.info("🚀 Starting model training with {} samples", request.trainingData().size());
            
            // Train the model
            var trainingResult = modelTrainingService.trainModel(request.trainingData(), request.labels());
            
            TrainingResponse response = new TrainingResponse(
                trainingResult,
                "Model training completed successfully",
                System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Model training failed", e);
            return ResponseEntity.badRequest().body(new TrainingResponse(
                null, "Model training failed: " + e.getMessage(), System.currentTimeMillis()
            ));
        }
    }

    /**
     * Train a new ML model asynchronously
     */
    @PostMapping("/train/async")
    public ResponseEntity<AsyncTrainingResponse> trainModelAsync(@Valid @RequestBody TrainingRequest request) {
        try {
            logger.info("🚀 Starting async model training with {} samples", request.trainingData().size());
            
            // Start async training
            CompletableFuture<com.university.foodml.ml.TrainingResult> future = 
                modelTrainingService.trainModelAsync(request.trainingData(), request.labels());
            
            AsyncTrainingResponse response = new AsyncTrainingResponse(
                "Model training started asynchronously",
                future.isDone(),
                System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Async model training failed", e);
            return ResponseEntity.badRequest().body(new AsyncTrainingResponse(
                "Async model training failed: " + e.getMessage(), false, System.currentTimeMillis()
            ));
        }
    }

    /**
     * Retrain model with new data
     */
    @PostMapping("/retrain")
    public ResponseEntity<TrainingResponse> retrainModel(@Valid @RequestBody TrainingRequest request) {
        try {
            logger.info("🔄 Starting model retraining with {} new samples", request.trainingData().size());
            
            // Retrain the model
            var trainingResult = modelTrainingService.retrainModel(request.trainingData(), request.labels());
            
            TrainingResponse response = new TrainingResponse(
                trainingResult,
                "Model retraining completed successfully",
                System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Model retraining failed", e);
            return ResponseEntity.badRequest().body(new TrainingResponse(
                null, "Model retraining failed: " + e.getMessage(), System.currentTimeMillis()
            ));
        }
    }

    /**
     * Get current model status
     */
    @GetMapping("/status")
    public ResponseEntity<ModelStatusResponse> getModelStatus() {
        try {
            var status = modelTrainingService.getTrainingStatus();
            var registryStats = modelRegistryService.getRegistryStats();
            
            ModelStatusResponse response = new ModelStatusResponse(
                status.isTrained(),
                status.metadata(),
                registryStats,
                System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Failed to get model status", e);
            return ResponseEntity.internalServerError().body(new ModelStatusResponse(
                false, null, null, System.currentTimeMillis()
            ));
        }
    }

    /**
     * Get all model versions
     */
    @GetMapping("/versions")
    public ResponseEntity<ModelVersionsResponse> getModelVersions() {
        try {
            var versions = modelRegistryService.getAllModelVersions();
            var currentVersion = modelRegistryService.getCurrentModelVersion();
            
            ModelVersionsResponse response = new ModelVersionsResponse(
                versions,
                currentVersion,
                System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Failed to get model versions", e);
            return ResponseEntity.internalServerError().body(new ModelVersionsResponse(
                List.of(), null, System.currentTimeMillis()
            ));
        }
    }

    // Request/Response records

    public record TrainingRequest(
        List<FoodFeatures> trainingData,
        List<FoodScore> labels
    ) {}

    public record TrainingResponse(
        com.university.foodml.ml.TrainingResult trainingResult,
        String message,
        long timestamp
    ) {}

    public record AsyncTrainingResponse(
        String message,
        boolean completed,
        long timestamp
    ) {}

    public record ModelStatusResponse(
        boolean isTrained,
        com.university.foodml.ml.ModelMetadata metadata,
        ModelRegistryService.ModelRegistryStats registryStats,
        long timestamp
    ) {}

    public record ModelVersionsResponse(
        List<ModelRegistryService.ModelVersion> versions,
        ModelRegistryService.ModelVersion currentVersion,
        long timestamp
    ) {}
} 