package com.university.foodml.controller;

import com.university.foodml.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final AIService aiService;

    @Autowired
    public ChatController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {

        String answer = aiService.answerNutritionQuestion(request.question());

        ChatResponse response = new ChatResponse(
            answer, request.question(),
            System.currentTimeMillis(), Thread.currentThread().toString()
        );

        return ResponseEntity.ok(response);
    }

    public record ChatRequest(@NotBlank String question) {}

    public record ChatResponse(String answer, String question, long timestamp, String processedByThread) {}
}