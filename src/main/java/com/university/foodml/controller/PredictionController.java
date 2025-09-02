package com.university.foodml.controller;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import com.university.foodml.service.PredictionService;
import com.university.foodml.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/predictions")
@CrossOrigin(origins = "*")
public class PredictionController {

    private final PredictionService predictionService;
    private final AIService aiService;

    @Autowired
    public PredictionController(PredictionService predictionService, 
                              AIService aiService) {
        this.predictionService = predictionService;
        this.aiService = aiService;
    }

    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predictFoodScore(@Valid @RequestBody PredictionRequest request) {

        FoodScore score = predictionService.predictFoodScore(request.features());
        String explanation = aiService.explainFoodScore(score, request.features());

        PredictionResponse response = new PredictionResponse(
            score, explanation, request.features(),
            System.currentTimeMillis(), Thread.currentThread().toString()
        );

        return ResponseEntity.ok(response);
    }

    public record PredictionRequest(@Valid FoodFeatures features) {}

    public record PredictionResponse(
        FoodScore score, String explanation, FoodFeatures features,
        long timestamp, String processedByThread
    ) {}
}