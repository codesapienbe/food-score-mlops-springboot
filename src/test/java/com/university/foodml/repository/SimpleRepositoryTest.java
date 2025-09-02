package com.university.foodml.repository;

import com.university.foodml.ml.entity.TrainingPipelineEntity;
import com.university.foodml.ml.repository.TrainingPipelineRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple repository test focused on core JPA functionality.
 * Tests the fixed repository methods without complex logging configuration.
 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "logging.level.root=WARN",
    "logging.level.org.hibernate.SQL=DEBUG"
})
class SimpleRepositoryTest {

    @Autowired
    private TrainingPipelineRepository repository;

    @Test
    void testRepositoryCreation() {
        // Test that the repository can be created without errors
        assertNotNull(repository);
    }

    @Test
    void testFindByStatusMethods() {
        // Test that the fixed findByStatus methods work correctly
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.PENDING));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.RUNNING));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.COMPLETED));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.FAILED));
        assertDoesNotThrow(() -> repository.findByStatus(TrainingPipelineEntity.TrainingStatus.CANCELLED));
    }

    @Test
    void testDefaultMethods() {
        // Test that the default methods work correctly
        assertDoesNotThrow(() -> repository.findRunningPipelines());
        assertDoesNotThrow(() -> repository.findCompletedPipelines());
        assertDoesNotThrow(() -> repository.findFailedPipelines());
    }
} 