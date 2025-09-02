package com.university.foodml.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

@Configuration
public class OllamaConfig {

    private static final Logger logger = LoggerFactory.getLogger(OllamaConfig.class);

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model-name:qwen3:1.7b}")
    private String modelName;

    @Value("${ollama.timeout:60s}")
    private String timeout;

    @Bean
    @Primary
    public ChatModel ollamaChatModel() {
        try {
            Duration timeoutDuration = Duration.parse("PT" + timeout);
            
            ChatModel model = OllamaChatModel.builder()
                    .baseUrl(ollamaBaseUrl)
                    .modelName(modelName)
                    .timeout(timeoutDuration)
                    .temperature(0.7)
                    .logRequests(true)
                    .logResponses(true)
                    .maxRetries(3)
                    .build();
            
            logger.info("🤖 Ollama chat model configured successfully: {} at {} with timeout: {}", 
                       modelName, ollamaBaseUrl, timeoutDuration);
            
            return model;
        } catch (Exception e) {
            logger.error("❌ Failed to configure Ollama chat model: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to configure Ollama chat model", e);
        }
    }
} 