# Food Score MLOps Pipeline 🍎

A complete **MLOps pipeline** for food quality prediction using **Spring Boot 3.2.12**, **JDK 21**, **LangChain4J**, **Ollama Local AI**, and **OpenFoodFacts** data.

## 🎯 Overview

This project predicts food quality scores (A, B, C, D, E) based on nutritional information and ingredients using:

- **🧵 JDK 21 Virtual Threads**: High-performance concurrent processing
- **🤖 LangChain4J 1.4.0 + Ollama**: Local AI-powered nutrition expertise and explanations  
- **📊 Statistical ML Models**: Java-native classification algorithms
- **🥗 OpenFoodFacts**: Access to 2.8M+ food products database
- **☁️ Production Ready**: Complete monitoring, structured logging, and deployment setup

## 🚀 Quick Start

### Prerequisites

- **JDK 21** (with virtual threads support)
- **Maven 3.9+**
- **Ollama** (for local AI model qwen3:1.7b)

### 1. Setup Ollama

```bash
# Install Ollama from https://ollama.ai
# Start Ollama service
ollama serve

# Pull the required model
ollama pull qwen3:1.7b
```

### 2. Run the Application

```bash
# Run the application
./mvnw spring-boot:run

# Or using Docker
docker-compose up -d
```

### 3. Access Services

- **API**: <http://localhost:8080/food-ml>
- **Health Check**: <http://localhost:8080/food-ml/actuator/health>
- **H2 Database Console**: <http://localhost:8080/food-ml/h2-console>

## 🎮 API Examples

### Search Food Products

```bash
curl -X GET "http://localhost:8080/food-ml/api/v1/foods/search?name=yogurt&limit=5"
```

### Predict Food Score

```bash
curl -X POST http://localhost:8080/food-ml/api/v1/predictions/predict \
  -H "Content-Type: application/json" \
  -d '{
    "features": {
      "product_name": "Greek Yogurt",
      "proteins_100g": 10,
      "sugars_100g": 4,
      "nova_groups": 1,
      "additives_n": 0
    }
  }'
```

### Get AI Nutrition Explanation

```bash
curl -X POST http://localhost:8080/food-ml/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"question": "What makes Greek yogurt healthy?"}'
```

## 🍎 Food Quality Scoring

- **🟢 Grade A**: Excellent nutritional value, minimal processing
- **🟡 Grade B**: Good nutrition with minor concerns  
- **🟠 Grade C**: Moderate nutritional value
- **🔴 Grade D**: Limited nutrition, concerning ingredients
- **⚫ Grade E**: Ultra-processed, minimal nutritional value

## 🛠️ Technology Stack

- **Spring Boot 3.2.12**: Enterprise application framework
- **JDK 21**: Java with Virtual Threads support
- **LangChain4J 1.4.0**: AI integration for nutrition expertise
- **Ollama**: Local AI model deployment (qwen3:1.7b)
- **Statistical ML Models**: Java-native classification algorithms
- **OpenFoodFacts SDK**: Food database integration
- **H2 Database**: In-memory database for development
- **Structured Logging**: JSON format with monitoring capabilities
- **Maven**: Dependency management and build tool

## 📁 Project Structure

```tree
food-score-mlops-springboot/
├── src/main/java/com/university/foodml/
│   ├── FoodScoreApplication.java          # Main Spring Boot app
│   ├── config/                            # Configuration classes
│   │   ├── VirtualThreadConfig.java      # JDK 21 configuration
│   │   ├── OllamaConfig.java             # Ollama AI configuration
│   │   └── LoggingConfig.java            # Structured logging configuration
│   ├── controller/                        # REST API endpoints
│   ├── service/                           # Business logic
│   │   ├── AIService.java                # AI service implementation
│   │   ├── PredictionService.java        # ML prediction service
│   │   ├── OpenFoodFactsService.java     # Food data service
│   │   └── LoggingService.java           # Structured logging service
│   ├── model/                             # DTOs and entities
│   ├── ml/                                # ML models and services
│   │   ├── StatisticalMLModel.java       # Statistical ML implementation
│   │   ├── ModelTrainingService.java     # Model training pipeline
│   │   └── ModelRegistryService.java     # Model version management
│   └── langchain/                         # AI nutrition agents
├── src/main/resources/
│   ├── application.yml                    # Configuration
│   └── logback-spring.xml                # Structured logging configuration
├── docker-compose.yml                     # Local development stack
├── Dockerfile                             # Container configuration
├── OLLAMA_SETUP.md                        # Ollama setup guide
└── pom.xml                               # Maven dependencies
```

## 🔧 Configuration

Key configuration in `application.yml`:

```yaml
spring:
  threads:
    virtual:
      enabled: true              # Enable JDK 21 virtual threads

# Structured Logging Configuration
logging:
  level:
    com.university.foodml: INFO
    org.hibernate.SQL: DEBUG
  file:
    name: logs/application.log
    max-size: 100MB
    max-history: 30

ollama:
  base-url: http://localhost:11434
  model-name: qwen3:1.7b
  timeout: 60s
```

## 📊 Structured Logging & Monitoring

The application implements enterprise-grade structured logging with:

- **JSON Format**: Machine-readable log output for monitoring systems
- **Monitoring Fields**: `correlation_id`, `user_id`, `request_id` for request tracing
- **Log Rotation**: Automatic log file management with size and time-based rotation
- **Performance Metrics**: Built-in logging for business events and performance tracking
- **Production Ready**: Proper log levels and sanitization for production environments

## 🤖 AI Integration

The application uses **Ollama** with the **qwen3:1.7b** model for:

- **Food quality explanations**: AI-powered analysis of nutrition scores
- **Nutrition Q&A**: Expert answers to health and nutrition questions
- **Local processing**: No external API dependencies, complete privacy

See `OLLAMA_SETUP.md` for detailed setup instructions.

## 🧠 Machine Learning Architecture

The MLOps pipeline includes:

- **Statistical ML Models**: Java-native classification algorithms for food scoring
- **Feature Engineering**: Automated feature extraction and preprocessing
- **Model Training Pipeline**: Automated retraining and version management
- **Model Registry**: Version control and deployment management
- **Performance Monitoring**: Continuous model performance tracking
- **Data Drift Detection**: Automated detection of data distribution changes

## 🎓 Educational Value

Perfect for university projects demonstrating:

- **Modern Java Development**: JDK 21 with virtual threads
- **Local AI Integration**: Ollama with LangChain4J for conversational AI
- **MLOps Practices**: Complete ML lifecycle management
- **External API Integration**: OpenFoodFacts SDK usage
- **Health Technology**: Nutrition analysis applications
- **Enterprise Logging**: Production-ready structured logging implementation

## 📊 Features

- ✅ **Real-time food quality prediction** (A-E grading)
- ✅ **Local AI-powered nutrition explanations** via Ollama + LangChain4J
- ✅ **High-performance virtual thread processing**
- ✅ **OpenFoodFacts integration** for real food data
- ✅ **Production-ready monitoring** and health checks
- ✅ **Structured logging** with JSON format and monitoring fields
- ✅ **Docker containerization** for easy deployment
- ✅ **Comprehensive API** with validation and error handling
- ✅ **Privacy-focused**: All AI processing happens locally
- ✅ **MLOps pipeline** with automated training and deployment

## 🔄 Recent Updates

### Latest Changes (September 2025)
- **Removed WEKA dependency**: Replaced with Java-native statistical ML models
- **Updated JDK version**: Now using JDK 21 (from JDK 24)
- **Enhanced logging**: Implemented structured JSON logging with monitoring capabilities
- **Fixed repository issues**: Resolved Spring Data JPA method compatibility problems
- **Improved testing**: Added comprehensive repository and integration tests
- **Production readiness**: Enhanced configuration for enterprise deployment

---

**Built with ❤️ for healthier food choices using Java, Spring Boot, LangChain4J, Ollama, and OpenFoodFacts data.**

Ready to analyze food quality with local AI! 🍎🤖🚀

