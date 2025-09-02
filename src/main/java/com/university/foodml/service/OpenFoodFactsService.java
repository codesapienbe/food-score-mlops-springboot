package com.university.foodml.service;

import com.university.foodml.model.dto.FoodFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenFoodFactsService {

    private static final Logger logger = LoggerFactory.getLogger(OpenFoodFactsService.class);

    @Cacheable(value = "product-search")
    public List<FoodFeatures> searchProductsByName(String productName, int limit) {
        logger.info("🔍 Searching for products: '{}'", productName);

        // Return sample data for demonstration
        // In full implementation, would use OpenFoodFacts Java SDK
        return List.of(
            FoodFeatures.createSample(),
            new FoodFeatures(
                "Organic Greek Yogurt", "Organic Brand", "yogurt",
                59.0, 10.3, 3.2, 0.36,
                "Organic milk, live cultures", 1, 0,
                "a", List.of("organic"), 0.98,
                // Derived features
                0.69, 0.89, 0.93, 0.80, 4, 1.0, 0.82
            )
        );
    }

    @Cacheable(value = "product-barcode")
    public FoodFeatures getProductByBarcode(String barcode) {
        logger.info("🏷️ Fetching product by barcode: {}", barcode);
        return FoodFeatures.createSample();
    }

    public List<FoodFeatures> getRandomProducts(int count) {
        logger.info("🎲 Fetching {} random products", count);
        return List.of(FoodFeatures.createSample());
    }
}