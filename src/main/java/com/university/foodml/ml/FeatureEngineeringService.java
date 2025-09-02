package com.university.foodml.ml;

import com.university.foodml.model.dto.FoodFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for feature engineering and data preprocessing for ML models.
 * Enhances raw food data with derived features and ensures data quality.
 */
@Service
public class FeatureEngineeringService {

    private static final Logger logger = LoggerFactory.getLogger(FeatureEngineeringService.class);
    
    // Nutritional thresholds for feature engineering
    private static final double HIGH_PROTEIN_THRESHOLD = 15.0;
    private static final double HIGH_SUGAR_THRESHOLD = 20.0;
    private static final double HIGH_SALT_THRESHOLD = 2.0;
    private static final double HIGH_ENERGY_THRESHOLD = 300.0;
    
    /**
     * Enhance food features with derived attributes for better ML performance
     */
    public FoodFeatures enhanceFeatures(FoodFeatures originalFeatures) {
        if (originalFeatures == null) {
            throw new IllegalArgumentException("Food features cannot be null");
        }
        
        try {
            // Create enhanced features with derived attributes
            return new FoodFeatures(
                originalFeatures.productName(),
                originalFeatures.brands(),
                originalFeatures.categories(),
                originalFeatures.energyKcal100g(),
                originalFeatures.proteins100g(),
                originalFeatures.sugars100g(),
                originalFeatures.salt100g(),
                originalFeatures.ingredientsText(),
                originalFeatures.novaGroups(),
                originalFeatures.additivesCount(),
                originalFeatures.nutritionGradeFr(),
                originalFeatures.organicTags(),
                originalFeatures.completeness(),
                // Derived features
                calculateProteinQuality(originalFeatures.proteins100g()),
                calculateSugarQuality(originalFeatures.sugars100g()),
                calculateSaltQuality(originalFeatures.salt100g()),
                calculateEnergyDensity(originalFeatures.energyKcal100g()),
                calculateNovaQuality(originalFeatures.novaGroups()),
                calculateAdditiveQuality(originalFeatures.additivesCount()),
                calculateOverallNutritionalBalance(originalFeatures)
            );
            
        } catch (Exception e) {
            logger.error("❌ Error enhancing features for product: {}", originalFeatures.productName(), e);
            return originalFeatures; // Return original if enhancement fails
        }
    }
    
    /**
     * Validate and clean food features for ML training
     */
    public FoodFeatures validateAndClean(FoodFeatures features) {
        if (features == null) {
            throw new IllegalArgumentException("Food features cannot be null");
        }
        
        try {
            return new FoodFeatures(
                features.productName() != null ? features.productName().trim() : "Unknown",
                features.brands(),
                features.categories(),
                validateNumericValue(features.energyKcal100g(), 0.0, 1000.0),
                validateNumericValue(features.proteins100g(), 0.0, 100.0),
                validateNumericValue(features.sugars100g(), 0.0, 100.0),
                validateNumericValue(features.salt100g(), 0.0, 50.0),
                features.ingredientsText(),
                validateNovaGroup(features.novaGroups()),
                validateIntegerValue(features.additivesCount(), 0, 100),
                features.nutritionGradeFr(),
                features.organicTags(),
                features.completeness(),
                // Derived features with validation
                calculateProteinQuality(validateNumericValue(features.proteins100g(), 0.0, 100.0)),
                calculateSugarQuality(validateNumericValue(features.sugars100g(), 0.0, 100.0)),
                calculateSaltQuality(validateNumericValue(features.salt100g(), 0.0, 50.0)),
                calculateEnergyDensity(validateNumericValue(features.energyKcal100g(), 0.0, 1000.0)),
                calculateNovaQuality(validateNovaGroup(features.novaGroups())),
                calculateAdditiveQuality(validateIntegerValue(features.additivesCount(), 0, 100)),
                calculateOverallNutritionalBalance(features)
            );
            
        } catch (Exception e) {
            logger.error("❌ Error validating features for product: {}", features.productName(), e);
            throw new RuntimeException("Feature validation failed", e);
        }
    }
    
    /**
     * Create training dataset from raw food features
     */
    public List<FoodFeatures> prepareTrainingDataset(List<FoodFeatures> rawFeatures) {
        if (rawFeatures == null || rawFeatures.isEmpty()) {
            throw new IllegalArgumentException("Raw features list cannot be null or empty");
        }
        
        List<FoodFeatures> preparedFeatures = new ArrayList<>();
        int processed = 0;
        int skipped = 0;
        
        for (FoodFeatures features : rawFeatures) {
            try {
                FoodFeatures enhanced = enhanceFeatures(features);
                FoodFeatures validated = validateAndClean(enhanced);
                preparedFeatures.add(validated);
                processed++;
                
            } catch (Exception e) {
                logger.warn("⚠️ Skipping invalid features for product: {}", features.productName());
                skipped++;
            }
        }
        
        logger.info("📊 Training dataset preparation completed: {} processed, {} skipped", processed, skipped);
        return preparedFeatures;
    }
    
    // Private helper methods for feature engineering
    
    private Double calculateProteinQuality(Double proteins100g) {
        if (proteins100g == null) return 0.0;
        return proteins100g >= HIGH_PROTEIN_THRESHOLD ? 1.0 : proteins100g / HIGH_PROTEIN_THRESHOLD;
    }
    
    private Double calculateSugarQuality(Double sugars100g) {
        if (sugars100g == null) return 0.0;
        return sugars100g <= HIGH_SUGAR_THRESHOLD ? 1.0 : Math.max(0.0, 1.0 - (sugars100g - HIGH_SUGAR_THRESHOLD) / HIGH_SUGAR_THRESHOLD);
    }
    
    private Double calculateSaltQuality(Double salt100g) {
        if (salt100g == null) return 0.0;
        return salt100g <= HIGH_SALT_THRESHOLD ? 1.0 : Math.max(0.0, 1.0 - (salt100g - HIGH_SALT_THRESHOLD) / HIGH_SALT_THRESHOLD);
    }
    
    private Double calculateEnergyDensity(Double energyKcal100g) {
        if (energyKcal100g == null) return 0.0;
        return energyKcal100g <= HIGH_ENERGY_THRESHOLD ? 1.0 : Math.max(0.0, 1.0 - (energyKcal100g - HIGH_ENERGY_THRESHOLD) / HIGH_ENERGY_THRESHOLD);
    }
    
    private Integer calculateNovaQuality(Integer novaGroups) {
        if (novaGroups == null) return 1;
        // Lower NOVA groups are better (1=unprocessed, 4=ultra-processed)
        return Math.max(1, 5 - novaGroups);
    }
    
    private Double calculateAdditiveQuality(Integer additivesCount) {
        if (additivesCount == null) return 1.0;
        // Fewer additives are better
        return Math.max(0.0, 1.0 - (additivesCount / 10.0));
    }
    
    private Double calculateOverallNutritionalBalance(FoodFeatures features) {
        double proteinScore = calculateProteinQuality(features.proteins100g());
        double sugarScore = calculateSugarQuality(features.sugars100g());
        double saltScore = calculateSaltQuality(features.salt100g());
        double energyScore = calculateEnergyDensity(features.energyKcal100g());
        double novaScore = calculateNovaQuality(features.novaGroups()) / 4.0; // Normalize to 0-1
        double additiveScore = calculateAdditiveQuality(features.additivesCount());
        
        // Weighted average of all scores
        return (proteinScore * 0.25 + sugarScore * 0.2 + saltScore * 0.2 + 
                energyScore * 0.15 + novaScore * 0.15 + additiveScore * 0.05);
    }
    
    private Double validateNumericValue(Double value, double min, double max) {
        if (value == null) return min;
        return Math.max(min, Math.min(max, value));
    }
    
    private Integer validateNovaGroup(Integer novaGroups) {
        if (novaGroups == null) return 1;
        return Math.max(1, Math.min(4, novaGroups));
    }

    private Integer validateIntegerValue(Integer value, int min, int max) {
        if (value == null) return min;
        return Math.max(min, Math.min(max, value));
    }
} 