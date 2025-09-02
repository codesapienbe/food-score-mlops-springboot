package com.university.foodml.ml.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * JPA entity for storing model versions in the database.
 * Provides persistent storage for MLOps model lifecycle management.
 */
@Entity
@Table(name = "model_versions")
public class ModelVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false, unique = true)
    private String modelId;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_version", nullable = false)
    private String modelVersion;

    @Column(name = "algorithm", nullable = false)
    private String algorithm;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "model_path", nullable = false)
    private String modelPath;

    @Column(name = "training_samples", nullable = false)
    private Integer trainingSamples;

    @Column(name = "accuracy", nullable = false)
    private Double accuracy;

    @Column(name = "f1_score", nullable = false)
    private Double f1Score;

    @Column(name = "precision", nullable = false)
    private Double precision;

    @Column(name = "recall", nullable = false)
    private Double recall;

    @Column(name = "training_time_seconds", nullable = false)
    private Double trainingTimeSeconds;

    @Column(name = "hyperparameters", columnDefinition = "TEXT")
    private String hyperparametersJson;

    @Column(name = "feature_importance", columnDefinition = "TEXT")
    private String featureImportanceJson;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;

    @Column(name = "is_deployed", nullable = false)
    private Boolean isDeployed = false;

    @Column(name = "deployment_environment")
    private String deploymentEnvironment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "trained_at", nullable = false)
    private LocalDateTime trainedAt;

    @Column(name = "deployed_at")
    private LocalDateTime deployedAt;

    // Constructors
    public ModelVersionEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ModelVersionEntity(String modelId, String modelName, String modelVersion, String algorithm) {
        this();
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.algorithm = algorithm;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getModelPath() { return modelPath; }
    public void setModelPath(String modelPath) { this.modelPath = modelPath; }

    public Integer getTrainingSamples() { return trainingSamples; }
    public void setTrainingSamples(Integer trainingSamples) { this.trainingSamples = trainingSamples; }

    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

    public Double getF1Score() { return f1Score; }
    public void setF1Score(Double f1Score) { this.f1Score = f1Score; }

    public Double getPrecision() { return precision; }
    public void setPrecision(Double precision) { this.precision = precision; }

    public Double getRecall() { return recall; }
    public void setRecall(Double recall) { this.recall = recall; }

    public Double getTrainingTimeSeconds() { return trainingTimeSeconds; }
    public void setTrainingTimeSeconds(Double trainingTimeSeconds) { this.trainingTimeSeconds = trainingTimeSeconds; }

    public String getHyperparametersJson() { return hyperparametersJson; }
    public void setHyperparametersJson(String hyperparametersJson) { this.hyperparametersJson = hyperparametersJson; }

    public String getFeatureImportanceJson() { return featureImportanceJson; }
    public void setFeatureImportanceJson(String featureImportanceJson) { this.featureImportanceJson = featureImportanceJson; }

    public Boolean getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }

    public Boolean getIsDeployed() { return isDeployed; }
    public void setIsDeployed(Boolean isDeployed) { this.isDeployed = isDeployed; }

    public String getDeploymentEnvironment() { return deploymentEnvironment; }
    public void setDeploymentEnvironment(String deploymentEnvironment) { this.deploymentEnvironment = deploymentEnvironment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getTrainedAt() { return trainedAt; }
    public void setTrainedAt(LocalDateTime trainedAt) { this.trainedAt = trainedAt; }

    public LocalDateTime getDeployedAt() { return deployedAt; }
    public void setDeployedAt(LocalDateTime deployedAt) { this.deployedAt = deployedAt; }

    // Business methods
    public void markAsCurrent() {
        this.isCurrent = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsDeployed(String environment) {
        this.isDeployed = true;
        this.deploymentEnvironment = environment;
        this.deployedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMetrics(Double accuracy, Double f1Score, Double precision, Double recall) {
        this.accuracy = accuracy;
        this.f1Score = f1Score;
        this.precision = precision;
        this.recall = recall;
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 