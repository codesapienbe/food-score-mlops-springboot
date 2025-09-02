package com.university.foodml.ml;

import com.university.foodml.model.dto.FoodFeatures;
import com.university.foodml.model.enums.FoodScore;

/**
 * Interface for machine learning models used in food quality prediction.
 * Supports both training and inference operations.
 */
public interface MLModel {

    /**
     * Train the model with the given training data
     * @param trainingData Array of training examples
     * @param labels Corresponding food quality scores
     * @return Training metrics and performance indicators
     */
    TrainingResult train(FoodFeatures[] trainingData, FoodScore[] labels);

    /**
     * Predict food quality score for given features
     * @param features Food features to predict
     * @return Predicted food quality score
     */
    FoodScore predict(FoodFeatures features);

    /**
     * Predict food quality score with confidence
     * @param features Food features to predict
     * @return Prediction with confidence score
     */
    PredictionWithConfidence predictWithConfidence(FoodFeatures features);

    /**
     * Evaluate model performance on test data
     * @param testData Array of test examples
     * @param trueLabels Corresponding true food quality scores
     * @return Model evaluation metrics
     */
    ModelEvaluation evaluate(FoodFeatures[] testData, FoodScore[] trueLabels);

    /**
     * Get model metadata and version information
     * @return Model metadata
     */
    ModelMetadata getMetadata();

    /**
     * Save the trained model to storage
     * @param path Path where to save the model
     */
    void saveModel(String path);

    /**
     * Load a trained model from storage
     * @param path Path from where to load the model
     */
    void loadModel(String path);

    /**
     * Check if the model is trained and ready for inference
     * @return true if model is ready, false otherwise
     */
    boolean isTrained();
} 