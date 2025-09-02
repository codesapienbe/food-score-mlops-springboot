# Ollama Setup Guide for Food Score MLOps

## Prerequisites

1. **Install Ollama**: Download and install Ollama from [https://ollama.ai](https://ollama.ai)
2. **Java 24**: Ensure you have Java 24 installed (required for virtual threads)

## Setup Steps

### 1. Install Ollama

#### Windows
```bash
# Download from https://ollama.ai/download
# Run the installer and follow the prompts
```

#### macOS/Linux
```bash
curl -fsSL https://ollama.ai/install.sh | sh
```

### 2. Start Ollama Service

```bash
ollama serve
```

### 3. Pull the qwen3:1.7b Model

```bash
ollama pull qwen3:1.7b
```

### 4. Verify Model Installation

```bash
ollama list
```

You should see `qwen3:1.7b` in the list.

### 5. Test the Model

```bash
ollama run qwen3:1.7b "Hello, how are you?"
```

## Configuration

The application is configured to use Ollama with these default settings:

- **Base URL**: `http://localhost:11434`
- **Model**: `qwen3:1.7b`
- **Timeout**: `60s`

You can override these in `application.yml`:

```yaml
ollama:
  base-url: http://localhost:11434
  model-name: qwen3:1.7b
  timeout: 60s
```

## Running the Application

1. Ensure Ollama is running: `ollama serve`
2. Start the Spring Boot application: `mvn spring-boot:run`
3. The application will automatically connect to Ollama

## Troubleshooting

### Common Issues

1. **Connection Refused**: Ensure Ollama is running on port 11434
2. **Model Not Found**: Run `ollama pull qwen3:1.7b`
3. **Timeout Errors**: Increase timeout in configuration or check Ollama performance

### Health Check

Check if Ollama is responding:
```bash
curl http://localhost:11434/api/tags
```

### Logs

Monitor application logs for AI service initialization:
```
🤖 Ollama chat model configured: qwen3:1.7b at http://localhost:11434 with timeout: PT60S
🤖 AIService initialized with Ollama model
```

## Performance Notes

- **qwen3:1.7b** is a 1.7B parameter model, suitable for local deployment
- First response may be slower due to model loading
- Consider using a more powerful model (e.g., qwen2.5:7b) for production if needed
- Monitor memory usage as Ollama loads models into RAM 