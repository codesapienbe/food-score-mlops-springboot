package com.university.foodml.ml.repository;

import com.university.foodml.ml.entity.ModelVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for model version management.
 * Provides database operations for MLOps model lifecycle.
 */
@Repository
public interface ModelVersionRepository extends JpaRepository<ModelVersionEntity, Long> {

    /**
     * Find model version by model ID
     */
    Optional<ModelVersionEntity> findByModelId(String modelId);

    /**
     * Find current model version
     */
    Optional<ModelVersionEntity> findByIsCurrentTrue();

    /**
     * Find all deployed models
     */
    List<ModelVersionEntity> findByIsDeployedTrue();

    /**
     * Find models by algorithm
     */
    List<ModelVersionEntity> findByAlgorithm(String algorithm);

    /**
     * Find models created after a specific date
     */
    List<ModelVersionEntity> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find models with accuracy above threshold
     */
    List<ModelVersionEntity> findByAccuracyGreaterThan(Double threshold);

    /**
     * Find models with F1 score above threshold
     */
    List<ModelVersionEntity> findByF1ScoreGreaterThan(Double threshold);

    /**
     * Find models by deployment environment
     */
    List<ModelVersionEntity> findByDeploymentEnvironment(String environment);

    /**
     * Find models trained in the last N days
     */
    @Query("SELECT m FROM ModelVersionEntity m WHERE m.trainedAt >= :daysAgo")
    List<ModelVersionEntity> findModelsTrainedInLastDays(@Param("daysAgo") LocalDateTime daysAgo);

    /**
     * Find models with performance degradation
     */
    @Query("SELECT m FROM ModelVersionEntity m WHERE m.accuracy < :minAccuracy OR m.f1Score < :minF1Score")
    List<ModelVersionEntity> findModelsWithPerformanceIssues(@Param("minAccuracy") Double minAccuracy, 
                                                           @Param("minF1Score") Double minF1Score);

    /**
     * Count models by status
     */
    @Query("SELECT COUNT(m) FROM ModelVersionEntity m WHERE m.isCurrent = :isCurrent")
    long countByCurrentStatus(@Param("isCurrent") Boolean isCurrent);

    /**
     * Find best performing model
     */
    @Query("SELECT m FROM ModelVersionEntity m ORDER BY m.accuracy DESC, m.f1Score DESC LIMIT 1")
    Optional<ModelVersionEntity> findBestPerformingModel();

    /**
     * Find models by version pattern
     */
    @Query("SELECT m FROM ModelVersionEntity m WHERE m.modelVersion LIKE %:versionPattern%")
    List<ModelVersionEntity> findByVersionPattern(@Param("versionPattern") String versionPattern);
} 