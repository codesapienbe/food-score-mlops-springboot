package com.university.foodml.ml.repository;

import com.university.foodml.ml.entity.FeatureStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for feature store management.
 * Provides database operations for ML feature lifecycle.
 */
@Repository
public interface FeatureStoreRepository extends JpaRepository<FeatureStoreEntity, Long> {

    /**
     * Find feature by feature ID
     */
    Optional<FeatureStoreEntity> findByFeatureId(String featureId);

    /**
     * Find features by feature type
     */
    List<FeatureStoreEntity> findByFeatureType(String featureType);

    /**
     * Find active features
     */
    List<FeatureStoreEntity> findByIsActiveTrue();

    /**
     * Find derived features
     */
    List<FeatureStoreEntity> findByIsDerivedTrue();

    /**
     * Find features by data type
     */
    List<FeatureStoreEntity> findByDataType(String dataType);

    /**
     * Find features by version
     */
    List<FeatureStoreEntity> findByFeatureVersion(String featureVersion);

    /**
     * Find features updated after a specific date
     */
    List<FeatureStoreEntity> findByLastUpdatedAfter(LocalDateTime date);

    /**
     * Find features with high data quality
     */
    List<FeatureStoreEntity> findByDataQualityScoreGreaterThan(Double minScore);

    /**
     * Find features by name pattern
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.featureName LIKE %:namePattern%")
    List<FeatureStoreEntity> findByFeatureNamePattern(@Param("namePattern") String namePattern);

    /**
     * Find features by description pattern
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.featureDescription LIKE %:descPattern%")
    List<FeatureStoreEntity> findByDescriptionPattern(@Param("descPattern") String descPattern);

    /**
     * Count features by type
     */
    @Query("SELECT COUNT(f) FROM FeatureStoreEntity f WHERE f.featureType = :featureType")
    long countByFeatureType(@Param("featureType") String featureType);

    /**
     * Find features with low data quality
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.dataQualityScore < :maxScore")
    List<FeatureStoreEntity> findFeaturesWithLowQuality(@Param("maxScore") Double maxScore);

    /**
     * Find features created in the last N days
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.createdAt >= :daysAgo")
    List<FeatureStoreEntity> findFeaturesCreatedInLastDays(@Param("daysAgo") LocalDateTime daysAgo);

    /**
     * Find features by derivation formula pattern
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.derivationFormula LIKE %:formulaPattern%")
    List<FeatureStoreEntity> findByDerivationFormulaPattern(@Param("formulaPattern") String formulaPattern);

    /**
     * Find features with statistics
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.featureStatisticsJson IS NOT NULL")
    List<FeatureStoreEntity> findFeaturesWithStatistics();

    /**
     * Find features by quality score range
     */
    @Query("SELECT f FROM FeatureStoreEntity f WHERE f.dataQualityScore BETWEEN :minScore AND :maxScore")
    List<FeatureStoreEntity> findFeaturesByQualityRange(@Param("minScore") Double minScore, 
                                                       @Param("maxScore") Double maxScore);
} 