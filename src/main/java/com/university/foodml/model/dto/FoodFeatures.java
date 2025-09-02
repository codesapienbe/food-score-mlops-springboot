package com.university.foodml.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

public record FoodFeatures(

    @NotBlank(message = "Product name is required")
    @JsonProperty("product_name")
    String productName,

    @JsonProperty("brands")
    String brands,

    @JsonProperty("categories")
    String categories,

    @DecimalMin("0.0") @DecimalMax("900.0")
    @JsonProperty("energy_kcal_100g")
    Double energyKcal100g,

    @DecimalMin("0.0") @DecimalMax("100.0")
    @JsonProperty("proteins_100g")
    Double proteins100g,

    @DecimalMin("0.0") @DecimalMax("100.0")
    @JsonProperty("sugars_100g")
    Double sugars100g,

    @DecimalMin("0.0") @DecimalMax("50.0")
    @JsonProperty("salt_100g")
    Double salt100g,

    @JsonProperty("ingredients_text")
    String ingredientsText,

    @Min(1) @Max(4)
    @JsonProperty("nova_groups")
    Integer novaGroups,

    @Min(0) @Max(100)
    @JsonProperty("additives_n")
    Integer additivesCount,

    @JsonProperty("nutrition_grade_fr")
    String nutritionGradeFr,

    @JsonProperty("organic_tags")
    List<String> organicTags,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("completeness")
    Double completeness,

    // Derived features for ML
    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("protein_quality")
    Double proteinQuality,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("sugar_quality")
    Double sugarQuality,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("salt_quality")
    Double saltQuality,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("energy_density")
    Double energyDensity,

    @Min(1) @Max(4)
    @JsonProperty("nova_quality")
    Integer novaQuality,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("additive_quality")
    Double additiveQuality,

    @DecimalMin("0.0") @DecimalMax("1.0")
    @JsonProperty("nutritional_balance")
    Double nutritionalBalance

) implements Serializable {

    public double[] toMLFeatureArray() {
        return new double[]{
            energyKcal100g != null ? energyKcal100g / 900.0 : 0.0,
            proteins100g != null ? proteins100g / 100.0 : 0.0,
            sugars100g != null ? sugars100g / 100.0 : 0.0,
            salt100g != null ? salt100g / 50.0 : 0.0,
            novaGroups != null ? novaGroups / 4.0 : 0.75,
            additivesCount != null ? Math.min(additivesCount / 20.0, 1.0) : 0.0,
            completeness != null ? completeness : 0.5,
            // Derived features
            proteinQuality != null ? proteinQuality : 0.5,
            sugarQuality != null ? sugarQuality : 0.5,
            saltQuality != null ? saltQuality : 0.5,
            energyDensity != null ? energyDensity : 0.5,
            novaQuality != null ? novaQuality / 4.0 : 0.75,
            additiveQuality != null ? additiveQuality : 0.5,
            nutritionalBalance != null ? nutritionalBalance : 0.5
        };
    }

    public static FoodFeatures createSample() {
        return new FoodFeatures(
            "Greek Yogurt", "Sample Brand", "dairy",
            100.0, 10.0, 4.0, 0.13,
            "Milk, live cultures", 1, 0, "b", 
            List.of("organic"), 0.95,
            // Derived features
            0.67, 0.8, 0.94, 0.67, 4, 1.0, 0.78
        );
    }
    
    /**
     * Create a basic FoodFeatures with default derived values
     */
    public static FoodFeatures createBasic(String productName, String categories, 
                                         Double energyKcal100g, Double proteins100g, 
                                         Double sugars100g, Double salt100g, 
                                         Integer novaGroups, Integer additivesCount) {
        return new FoodFeatures(
            productName, null, categories, energyKcal100g, proteins100g, 
            sugars100g, salt100g, null, novaGroups, additivesCount, 
            null, null, null,
            // Default derived features
            0.5, 0.5, 0.5, 0.5, 3, 0.5, 0.5
        );
    }
}