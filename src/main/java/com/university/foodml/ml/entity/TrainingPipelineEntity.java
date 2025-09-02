package com.university.foodml.ml.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity for tracking ML training pipeline executions.
 * Provides audit trail for MLOps training operations.
 */
@Entity
@Table(name = "training_pipelines")
public class TrainingPipelineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pipeline_id", nullable = false, unique = true)
    private String pipelineId;

    @Column(name = "pipeline_name", nullable = false)
    private String pipelineName;

    @Column(name = "model_id", nullable = false)
    private String modelId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingStatus status;

    @Column(name = "trigger_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TriggerType triggerType;

    @Column(name = "training_samples", nullable = false)
    private Integer trainingSamples;

    @Column(name = "validation_samples", nullable = false)
    private Integer validationSamples;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_seconds")
    private Long durationSeconds;

    @Column(name = "accuracy")
    private Double accuracy;

    @Column(name = "f1_score")
    private Double f1Score;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "hyperparameters", columnDefinition = "TEXT")
    private String hyperparametersJson;

    @Column(name = "environment", nullable = false)
    private String environment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum TrainingStatus {
        PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    }

    public enum TriggerType {
        MANUAL, SCHEDULED, DATA_DRIFT, PERFORMANCE_DEGRADATION, NEW_DATA
    }

    // Constructors
    public TrainingPipelineEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TrainingStatus.PENDING;
    }

    public TrainingPipelineEntity(String pipelineId, String pipelineName, String modelId, TriggerType triggerType) {
        this();
        this.pipelineId = pipelineId;
        this.pipelineName = pipelineName;
        this.modelId = modelId;
        this.triggerType = triggerType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPipelineId() { return pipelineId; }
    public void setPipelineId(String pipelineId) { this.pipelineId = pipelineId; }

    public String getPipelineName() { return pipelineName; }
    public void setPipelineName(String pipelineName) { this.pipelineName = pipelineName; }

    public String getModelId() { return modelId; }
    public void setModelId(String modelId) { this.modelId = modelId; }

    public TrainingStatus getStatus() { return status; }
    public void setStatus(TrainingStatus status) { this.status = status; }

    public TriggerType getTriggerType() { return triggerType; }
    public void setTriggerType(TriggerType triggerType) { this.triggerType = triggerType; }

    public Integer getTrainingSamples() { return trainingSamples; }
    public void setTrainingSamples(Integer trainingSamples) { this.trainingSamples = trainingSamples; }

    public Integer getValidationSamples() { return validationSamples; }
    public void setValidationSamples(Integer validationSamples) { this.validationSamples = validationSamples; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public Long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Long durationSeconds) { this.durationSeconds = durationSeconds; }

    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

    public Double getF1Score() { return f1Score; }
    public void setF1Score(Double f1Score) { this.f1Score = f1Score; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getHyperparametersJson() { return hyperparametersJson; }
    public void setHyperparametersJson(String hyperparametersJson) { this.hyperparametersJson = hyperparametersJson; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Business methods
    public void startTraining() {
        this.status = TrainingStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void completeTraining(Double accuracy, Double f1Score) {
        this.status = TrainingStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.accuracy = accuracy;
        this.f1Score = f1Score;
        this.durationSeconds = java.time.Duration.between(this.startedAt, this.completedAt).getSeconds();
        this.updatedAt = LocalDateTime.now();
    }

    public void failTraining(String errorMessage) {
        this.status = TrainingStatus.FAILED;
        this.completedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.durationSeconds = java.time.Duration.between(this.startedAt, this.completedAt).getSeconds();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelTraining() {
        this.status = TrainingStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
        this.durationSeconds = java.time.Duration.between(this.startedAt, this.completedAt).getSeconds();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 