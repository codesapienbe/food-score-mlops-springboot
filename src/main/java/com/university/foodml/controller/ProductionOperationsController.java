package com.university.foodml.controller;

import com.university.foodml.ml.service.ModelMonitoringService;
import com.university.foodml.ml.service.AutomatedRetrainingService;
import com.university.foodml.ml.service.DataDriftDetectionService;
import com.university.foodml.model.dto.FoodFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.university.foodml.ml.TrainingResult;

/**
 * REST controller for production MLOps operations.
 * Provides endpoints for monitoring, drift detection, and automated retraining.
 */
@RestController
@RequestMapping("/api/mlops/production")
public class ProductionOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(ProductionOperationsController.class);

    @Autowired
    private ModelMonitoringService modelMonitoringService;

    @Autowired
    private AutomatedRetrainingService automatedRetrainingService;

    @Autowired
    private DataDriftDetectionService dataDriftDetectionService;

    /**
     * Trigger manual model monitoring
     */
    @PostMapping("/monitor/{modelId}")
    public ResponseEntity<Map<String, Object>> monitorModel(@PathVariable String modelId) {
        try {
            logger.info("Manual model monitoring triggered for model: {}", modelId);
            
            modelMonitoringService.monitorModelPerformance(modelId);
            
            Map<String, Object> response = Map.of(
                "status", "SUCCESS",
                "message", "Model monitoring completed",
                "modelId", modelId,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error during manual model monitoring: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Model monitoring failed: " + e.getMessage()
            ));
        }
    }

    /**
     * Trigger automated retraining for new data
     * This is the CRITICAL endpoint for automated retraining when new data arrives
     */
    @PostMapping("/retrain/new-data/{modelId}")
    public ResponseEntity<Map<String, Object>> triggerRetrainingForNewData(
            @PathVariable String modelId,
            @RequestBody List<FoodFeatures> newData) {
        
        try {
            logger.info("🚀 Automated retraining triggered for model {} with {} new samples", 
                modelId, newData.size());
            
            if (newData.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "ERROR",
                    "message", "No new data provided for retraining"
                ));
            }

            // Start async retraining
            CompletableFuture<TrainingResult> retrainingFuture = automatedRetrainingService
                .triggerRetrainingForNewData(modelId, newData);

            Map<String, Object> response = Map.of(
                "status", "SUCCESS",
                "message", "Automated retraining initiated",
                "modelId", modelId,
                "newDataSamples", newData.size(),
                "retrainingStatus", "IN_PROGRESS",
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            logger.error("Error triggering automated retraining: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to trigger retraining: " + e.getMessage()
            ));
        }
    }

    /**
     * Trigger performance-based retraining
     */
    @PostMapping("/retrain/performance/{modelId}")
    public ResponseEntity<Map<String, Object>> triggerPerformanceRetraining(@PathVariable String modelId) {
        try {
            logger.info("📉 Performance-based retraining triggered for model: {}", modelId);
            
            CompletableFuture<TrainingResult> retrainingFuture = automatedRetrainingService
                .triggerRetrainingForPerformance(modelId);

            Map<String, Object> response = Map.of(
                "status", "SUCCESS",
                "message", "Performance-based retraining initiated",
                "modelId", modelId,
                "retrainingStatus", "IN_PROGRESS",
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            logger.error("Error triggering performance retraining: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to trigger performance retraining: " + e.getMessage()
            ));
        }
    }

    /**
     * Trigger drift-based retraining
     */
    @PostMapping("/retrain/drift/{modelId}")
    public ResponseEntity<Map<String, Object>> triggerDriftRetraining(@PathVariable String modelId) {
        try {
            logger.info("🌊 Drift-based retraining triggered for model: {}", modelId);
            
            CompletableFuture<TrainingResult> retrainingFuture = automatedRetrainingService
                .triggerRetrainingForDrift(modelId);

            Map<String, Object> response = Map.of(
                "status", "SUCCESS",
                "message", "Drift-based retraining initiated",
                "modelId", modelId,
                "retrainingStatus", "IN_PROGRESS",
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.accepted().body(response);
            
        } catch (Exception e) {
            logger.error("Error triggering drift retraining: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to trigger drift retraining: " + e.getMessage()
            ));
        }
    }

    /**
     * Analyze data drift in features
     */
    @PostMapping("/drift/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDataDrift(@RequestBody List<FoodFeatures> features) {
        try {
            logger.info("🔍 Data drift analysis requested for {} samples", features.size());
            
            Map<String, Object> driftReport = dataDriftDetectionService.detectDataDrift(features);
            
            return ResponseEntity.ok(driftReport);
            
        } catch (Exception e) {
            logger.error("Error during data drift analysis: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Data drift analysis failed: " + e.getMessage()
            ));
        }
    }

    /**
     * Get drift detection statistics
     */
    @GetMapping("/drift/stats")
    public ResponseEntity<Map<String, Object>> getDriftStats() {
        try {
            Map<String, Object> stats = dataDriftDetectionService.getDriftDetectionStats();
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error getting drift stats: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to get drift statistics: " + e.getMessage()
            ));
        }
    }

    /**
     * Reset drift detection baseline
     */
    @PostMapping("/drift/reset-baseline")
    public ResponseEntity<Map<String, Object>> resetDriftBaseline() {
        try {
            logger.info("🔄 Drift detection baseline reset requested");
            
            dataDriftDetectionService.resetBaseline();
            
            Map<String, Object> response = Map.of(
                "status", "SUCCESS",
                "message", "Drift detection baseline reset successfully",
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error resetting drift baseline: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to reset drift baseline: " + e.getMessage()
            ));
        }
    }

    /**
     * Get retraining statistics for a model
     */
    @GetMapping("/retrain/stats/{modelId}")
    public ResponseEntity<Map<String, Object>> getRetrainingStats(@PathVariable String modelId) {
        try {
            Map<String, Object> stats = automatedRetrainingService.getRetrainingStats(modelId);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error getting retraining stats: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "Failed to get retraining statistics: " + e.getMessage()
            ));
        }
    }

    /**
     * Get production operations health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealthStatus() {
        try {
            Map<String, Object> healthStatus = Map.of(
                "status", "HEALTHY",
                "service", "Production Operations",
                "timestamp", System.currentTimeMillis(),
                "components", Map.of(
                    "modelMonitoring", "ACTIVE",
                    "automatedRetraining", "ACTIVE",
                    "driftDetection", "ACTIVE"
                )
            );
            
            return ResponseEntity.ok(healthStatus);
            
        } catch (Exception e) {
            logger.error("Error getting health status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "UNHEALTHY",
                "message", "Health check failed: " + e.getMessage()
            ));
        }
    }
} 