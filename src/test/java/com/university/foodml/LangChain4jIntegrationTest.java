package com.university.foodml;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LangChain4jIntegrationTest {

    @Test
    void testOllamaChatModelCreation() {
        // Test that we can create an OllamaChatModel instance
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("qwen3:1.7b")
                .temperature(0.7)
                .timeout(Duration.ofSeconds(30))
                .build();
        
        assertNotNull(model);
    }
    
    @Test
    void testOllamaChatModelWithAllParameters() {
        // Test that we can create an OllamaChatModel with all available parameters
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3.1")
                .temperature(0.8)
                .topK(40)
                .topP(0.9)
                .repeatPenalty(1.1)
                .seed(42)
                .timeout(Duration.ofSeconds(60))
                .maxRetries(3)
                .logRequests(true)
                .logResponses(true)
                .build();
        
        assertNotNull(model);
    }
} 