package com.university.foodml.repository;

import com.university.foodml.ml.entity.TrainingPipelineEntity;
import com.university.foodml.ml.repository.TrainingPipelineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TrainingPipelineRepository to verify fixed methods work correctly.
 */
@DataJpaTest
@ActiveProfiles("test")
class TrainingPipelineRepositoryTest {

    @Autowired
    private TrainingPipelineRepository repository;

    @Test
    void testRepositoryMethods() {
        // Test that the repository can be created without errors
        assertNotNull(repository);
        
        // Test that the fixed methods exist and can be called
        assertDoesNotThrow(() -> repository.findRunningPipelines());
        assertDoesNotThrow(() -> repository.findCompletedPipelines());
        assertDoesNotThrow(() -> repository.findFailedPipelines());
        
        // Test that the findByStatus method works
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.PENDING));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.RUNNING));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.COMPLETED));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.FAILED));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.CANCELLED));
    }
} 