package com.university.foodml.service;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;
import dev.langchain4j.model.chat.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    
    private final ChatModel chatModel;

    @Autowired
    public AIService(ChatModel chatModel) {
        this.chatModel = chatModel;
        logger.info("🤖 AIService initialized with Ollama model");
    }

    public String explainFoodScore(FoodScore foodScore, FoodFeatures features) {
        try {
            logger.info("🧠 AI analyzing food score {} for product: {}", foodScore, features.productName());
            
            String prompt = String.format("""
                A food product received a quality score of %s:
                
                Product: %s
                Category: %s
                
                Nutrition per 100g:
                - Energy: %s kcal
                - Proteins: %s g
                - Sugars: %s g
                - Salt: %s g
                
                Processing:
                - NOVA Group: %s (1=unprocessed, 4=ultra-processed)
                - Additives: %s
                
                Please explain why this food received this quality score in simple terms.
                """, 
                foodScore, features.productName(), features.categories(),
                features.energyKcal100g(), features.proteins100g(),
                features.sugars100g(), features.salt100g(),
                features.novaGroups(), features.additivesCount());
            
            String explanation = chatModel.chat(prompt);
            logger.info("✅ AI analysis completed for product: {}", features.productName());
            return explanation;
        } catch (Exception e) {
            logger.error("❌ Error during AI analysis for product: {}", features.productName(), e);
            return "Unable to analyze this food product at the moment. Please try again later.";
        }
    }

    public String answerNutritionQuestion(String question) {
        try {
            logger.info("❓ AI answering nutrition question: {}", question.substring(0, Math.min(question.length(), 50)) + "...");
            String answer = chatModel.chat(question);
            logger.info("✅ AI nutrition question answered successfully");
            return answer;
        } catch (Exception e) {
            logger.error("❌ Error answering nutrition question", e);
            return "I'm unable to answer your nutrition question at the moment. Please try again later.";
        }
    }

    public String getSystemPrompt() {
        try {
            return "You are an expert nutritionist and food quality analyst. Explain food quality scores and nutrition in simple, helpful terms. Focus on practical advice for healthier eating choices.";
        } catch (Exception e) {
            logger.error("❌ Error getting system prompt", e);
            return "You are an expert nutritionist and food quality analyst.";
        }
    }
} 