package com.university.foodml.ml.repository;

import com.university.foodml.ml.entity.TrainingPipelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for training pipeline management.
 * Provides database operations for ML training lifecycle.
 */
@Repository
public interface TrainingPipelineRepository extends JpaRepository<TrainingPipelineEntity, Long> {

    /**
     * Find pipeline by pipeline ID
     */
    Optional<TrainingPipelineEntity> findByPipelineId(String pipelineId);

    /**
     * Find pipelines by model ID
     */
    List<TrainingPipelineEntity> findByModelId(String modelId);

    /**
     * Find pipelines by status
     */
    List<TrainingPipelineEntity> findByStatus(TrainingPipelineEntity.TrainingStatus status);

    /**
     * Find pipelines by trigger type
     */
    List<TrainingPipelineEntity> findByTriggerType(TrainingPipelineEntity.TriggerType triggerType);

    /**
     * Find running pipelines
     */
    default List<TrainingPipelineEntity> findRunningPipelines() {
        return findByStatus(TrainingPipelineEntity.TrainingStatus.RUNNING);
    }

    /**
     * Find completed pipelines
     */
    default List<TrainingPipelineEntity> findCompletedPipelines() {
        return findByStatus(TrainingPipelineEntity.TrainingStatus.COMPLETED);
    }

    /**
     * Find failed pipelines
     */
    default List<TrainingPipelineEntity> findFailedPipelines() {
        return findByStatus(TrainingPipelineEntity.TrainingStatus.FAILED);
    }

    /**
     * Find pipelines by environment
     */
    List<TrainingPipelineEntity> findByEnvironment(String environment);

    /**
     * Find pipelines started after a specific date
     */
    List<TrainingPipelineEntity> findByStartedAtAfter(LocalDateTime date);

    /**
     * Find pipelines completed after a specific date
     */
    List<TrainingPipelineEntity> findByCompletedAtAfter(LocalDateTime date);

    /**
     * Find pipelines with high accuracy
     */
    List<TrainingPipelineEntity> findByAccuracyGreaterThan(Double threshold);

    /**
     * Find pipelines with high F1 score
     */
    List<TrainingPipelineEntity> findByF1ScoreGreaterThan(Double threshold);

    /**
     * Find pipelines by duration range
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.durationSeconds BETWEEN :minDuration AND :maxDuration")
    List<TrainingPipelineEntity> findByDurationRange(@Param("minDuration") Long minDuration, 
                                                   @Param("maxDuration") Long maxDuration);

    /**
     * Find pipelines with performance issues
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.accuracy < :minAccuracy OR p.f1Score < :minF1Score")
    List<TrainingPipelineEntity> findPipelinesWithPerformanceIssues(@Param("minAccuracy") Double minAccuracy, 
                                                                   @Param("minF1Score") Double minF1Score);

    /**
     * Find pipelines by hyperparameters pattern
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.hyperparametersJson LIKE %:pattern%")
    List<TrainingPipelineEntity> findByHyperparametersPattern(@Param("pattern") String pattern);

    /**
     * Count pipelines by model ID
     */
    long countByModelId(String modelId);

    /**
     * Count pipelines by status
     */
    @Query("SELECT COUNT(p) FROM TrainingPipelineEntity p WHERE p.status = :status")
    long countByStatus(@Param("status") TrainingPipelineEntity.TrainingStatus status);

    /**
     * Find pipelines by model and status
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.modelId = :modelId AND p.status = :status")
    List<TrainingPipelineEntity> findByModelIdAndStatus(@Param("modelId") String modelId, 
                                                       @Param("status") TrainingPipelineEntity.TrainingStatus status);

    /**
     * Find recent pipelines
     */
    @Query("SELECT p FROM TrainingPipelineEntity p ORDER BY p.createdAt DESC")
    List<TrainingPipelineEntity> findRecentPipelines();

    /**
     * Find pipelines with errors
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.errorMessage IS NOT NULL")
    List<TrainingPipelineEntity> findPipelinesWithErrors();

    /**
     * Find pipelines by training samples range
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.trainingSamples BETWEEN :minSamples AND :maxSamples")
    List<TrainingPipelineEntity> findByTrainingSamplesRange(@Param("minSamples") Integer minSamples, 
                                                           @Param("maxSamples") Integer maxSamples);

    /**
     * Find best performing pipeline
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.status = 'COMPLETED' ORDER BY p.accuracy DESC, p.f1Score DESC LIMIT 1")
    Optional<TrainingPipelineEntity> findBestPerformingPipeline();

    /**
     * Find pipelines by time range
     */
    @Query("SELECT p FROM TrainingPipelineEntity p WHERE p.startedAt BETWEEN :startDate AND :endDate")
    List<TrainingPipelineEntity> findPipelinesByTimeRange(@Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);
} 