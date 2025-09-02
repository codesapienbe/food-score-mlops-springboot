# Food Score MLOps Pipeline 🍎

A complete **MLOps pipeline** for food quality prediction using **Spring Boot 3.3+**, **JDK 24 Virtual Threads**, **LangChain4J**, **Ollama Local AI**, and **OpenFoodFacts** data.

## 🎯 Overview

This project predicts food quality scores (A, B, C, D, E) based on nutritional information and ingredients using:

- **🧵 JDK 24 Virtual Threads**: High-performance concurrent processing
- **🤖 LangChain4J 1.4.0 + Ollama**: Local AI-powered nutrition expertise and explanations  
- **🌲 Weka ML**: Java-native classification models
- **🥗 OpenFoodFacts**: Access to 2.8M+ food products database
- **☁️ Production Ready**: Complete monitoring and deployment setup

## 🚀 Quick Start

### Prerequisites

- **JDK 24** (with preview features enabled)
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

- **Spring Boot 3.3+**: Enterprise application framework
- **JDK 24**: Latest Java with enhanced Virtual Threads
- **LangChain4J 1.4.0**: AI integration for nutrition expertise
- **Ollama**: Local AI model deployment (qwen3:1.7b)
- **Weka 3.8.6**: Machine learning classification
- **OpenFoodFacts SDK**: Food database integration
- **H2 Database**: In-memory database for development
- **Maven**: Dependency management and build tool

## 📁 Project Structure

```tree
food-score-mlops-springboot/
├── src/main/java/com/university/foodml/
│   ├── FoodScoreApplication.java          # Main Spring Boot app
│   ├── config/                            # Configuration classes
│   │   ├── VirtualThreadConfig.java      # JDK 24 configuration
│   │   └── OllamaConfig.java             # Ollama AI configuration
│   ├── controller/                        # REST API endpoints
│   ├── service/                           # Business logic
│   │   ├── AIService.java                # AI service implementation
│   │   ├── PredictionService.java        # ML prediction service
│   │   └── OpenFoodFactsService.java     # Food data service
│   ├── model/                             # DTOs and entities
│   └── langchain/                         # AI nutrition agents
├── src/main/resources/
│   └── application.yml                    # Configuration
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
      enabled: true              # Enable JDK 24 virtual threads

langchain4j:
  ollama:
    chat-model:
      base-url: http://localhost:11434
      model-name: qwen3:1.7b
      timeout: 60s

ollama:
  base-url: http://localhost:11434
  model-name: qwen3:1.7b
  timeout: 60s
```

## 🤖 AI Integration

The application uses **Ollama** with the **qwen3:1.7b** model for:

- **Food quality explanations**: AI-powered analysis of nutrition scores
- **Nutrition Q&A**: Expert answers to health and nutrition questions
- **Local processing**: No external API dependencies, complete privacy

See `OLLAMA_SETUP.md` for detailed setup instructions.

## 🎓 Educational Value

Perfect for university projects demonstrating:

- **Modern Java Development**: JDK 24 with virtual threads
- **Local AI Integration**: Ollama with LangChain4J for conversational AI
- **MLOps Practices**: Complete ML lifecycle management
- **External API Integration**: OpenFoodFacts SDK usage
- **Health Technology**: Nutrition analysis applications

## 📊 Features

- ✅ **Real-time food quality prediction** (A-E grading)
- ✅ **Local AI-powered nutrition explanations** via Ollama + LangChain4J
- ✅ **High-performance virtual thread processing**
- ✅ **OpenFoodFacts integration** for real food data
- ✅ **Production-ready monitoring** and health checks
- ✅ **Docker containerization** for easy deployment
- ✅ **Comprehensive API** with validation and error handling
- ✅ **Privacy-focused**: All AI processing happens locally

---

**Built with ❤️ for healthier food choices using Java, Spring Boot, LangChain4J, Ollama, and OpenFoodFacts data.**

Ready to analyze food quality with local AI! 🍎🤖🚀

