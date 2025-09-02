package com.university.foodml.ml.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * JPA entity for storing ML features in the feature store.
 * Provides centralized feature management for MLOps.
 */
@Entity
@Table(name = "feature_store")
public class FeatureStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_id", nullable = false, unique = true)
    private String featureId;

    @Column(name = "feature_name", nullable = false)
    private String featureName;

    @Column(name = "feature_type", nullable = false)
    private String featureType;

    @Column(name = "feature_description", columnDefinition = "TEXT")
    private String featureDescription;

    @Column(name = "feature_version", nullable = false)
    private String featureVersion;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "is_derived", nullable = false)
    private Boolean isDerived = false;

    @Column(name = "derivation_formula", columnDefinition = "TEXT")
    private String derivationFormula;

    @Column(name = "feature_statistics", columnDefinition = "TEXT")
    private String featureStatisticsJson;

    @Column(name = "data_quality_score")
    private Double dataQualityScore;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructors
    public FeatureStoreEntity() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    public FeatureStoreEntity(String featureId, String featureName, String featureType, String dataType) {
        this();
        this.featureId = featureId;
        this.featureName = featureName;
        this.featureType = featureType;
        this.dataType = dataType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFeatureId() { return featureId; }
    public void setFeatureId(String featureId) { this.featureId = featureId; }

    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }

    public String getFeatureType() { return featureType; }
    public void setFeatureType(String featureType) { this.featureType = featureType; }

    public String getFeatureDescription() { return featureDescription; }
    public void setFeatureDescription(String featureDescription) { this.featureDescription = featureDescription; }

    public String getFeatureVersion() { return featureVersion; }
    public void setFeatureVersion(String featureVersion) { this.featureVersion = featureVersion; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public Boolean getIsDerived() { return isDerived; }
    public void setIsDerived(Boolean isDerived) { this.isDerived = isDerived; }

    public String getDerivationFormula() { return derivationFormula; }
    public void setDerivationFormula(String derivationFormula) { this.derivationFormula = derivationFormula; }

    public String getFeatureStatisticsJson() { return featureStatisticsJson; }
    public void setFeatureStatisticsJson(String featureStatisticsJson) { this.featureStatisticsJson = featureStatisticsJson; }

    public Double getDataQualityScore() { return dataQualityScore; }
    public void setDataQualityScore(Double dataQualityScore) { this.dataQualityScore = dataQualityScore; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // Business methods
    public void updateFeature(String description, String version) {
        this.featureDescription = description;
        this.featureVersion = version;
        this.lastUpdated = LocalDateTime.now();
    }

    public void markAsDerived(String formula) {
        this.isDerived = true;
        this.derivationFormula = formula;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateDataQuality(Double qualityScore) {
        this.dataQualityScore = qualityScore;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
} 