package com.university.foodml.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class VirtualThreadConfig {

    @Bean
    @ConditionalOnProperty(value = "spring.threads.virtual.enabled", havingValue = "true")
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            System.out.println("🧵 Configuring Tomcat with Virtual Thread Executor");
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

    @Bean("taskExecutor")
    @ConditionalOnProperty(value = "spring.threads.virtual.enabled", havingValue = "true")
    public AsyncTaskExecutor asyncTaskExecutor() {
        System.out.println("🚀 Creating Virtual Thread Task Executor");
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean("virtualMlExecutor")
    @ConditionalOnProperty(value = "spring.threads.virtual.enabled", havingValue = "true")
    public AsyncTaskExecutor virtualMlTaskExecutor() {
        System.out.println("🤖 Creating ML Virtual Thread Executor");
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}