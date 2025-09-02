package com.university.foodml.controller;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.service.OpenFoodFactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
@CrossOrigin(origins = "*")
public class FoodController {

    private final OpenFoodFactsService openFoodFactsService;

    @Autowired
    public FoodController(OpenFoodFactsService openFoodFactsService) {
        this.openFoodFactsService = openFoodFactsService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodFeatures>> searchProducts(
            @RequestParam String name,
            @RequestParam(defaultValue = "10") int limit) {

        List<FoodFeatures> products = openFoodFactsService.searchProductsByName(name, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<FoodFeatures> getProductByBarcode(@PathVariable String barcode) {
        FoodFeatures product = openFoodFactsService.getProductByBarcode(barcode);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/random")
    public ResponseEntity<List<FoodFeatures>> getRandomProducts(
            @RequestParam(defaultValue = "5") int count) {

        List<FoodFeatures> products = openFoodFactsService.getRandomProducts(count);
        return ResponseEntity.ok(products);
    }
}