package com.university.foodml.model.enums;

public enum FoodScore {

    A("Excellent", "High nutritional value, minimal processing", 5, "#4CAF50"),
    B("Good", "Good nutritional value with minor concerns", 4, "#8BC34A"),
    C("Average", "Moderate nutritional value", 3, "#FFC107"),
    D("Poor", "Limited nutritional value, high in concerning ingredients", 2, "#FF9800"),
    E("Very Poor", "Very low nutritional value, ultra-processed", 1, "#F44336");

    private final String displayName;
    private final String description;
    private final int numericValue;
    private final String colorCode;

    FoodScore(String displayName, String description, int numericValue, String colorCode) {
        this.displayName = displayName;
        this.description = description;
        this.numericValue = numericValue;
        this.colorCode = colorCode;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getNumericValue() { return numericValue; }
    public String getColorCode() { return colorCode; }

    public static FoodScore fromNumericScore(double score) {
        if (score <= 0.2) return E;
        if (score <= 0.4) return D;
        if (score <= 0.6) return C;
        if (score <= 0.8) return B;
        return A;
    }

    public static FoodScore fromIntegerScore(int score) {
        return switch (score) {
            case 5 -> A;
            case 4 -> B;  
            case 3 -> C;
            case 2 -> D;
            default -> E;
        };
    }

    public String getRecommendationMessage() {
        return switch (this) {
            case A -> "Excellent choice! This food provides great nutritional value.";
            case B -> "Good choice! This food offers good nutrition with minor concerns.";
            case C -> "Moderate choice. Consider balancing with healthier options.";
            case D -> "Poor choice. Consider healthier alternatives.";
            case E -> "Avoid if possible. This food offers little nutritional value.";
        };
    }

    public boolean isHealthy() {
        return this == A || this == B;
    }
}