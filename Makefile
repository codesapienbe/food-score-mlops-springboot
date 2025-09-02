.PHONY: build test run deploy

# Detect OS and set colors accordingly
ifeq ($(OS),Windows_NT)
    # Windows - disable colors by default to avoid display issues
    GREEN=
    YELLOW=
    BLUE=
    RED=
    CYAN=
    NC=
else
    # Unix/Linux/Mac - with colors
    GREEN=\033[0;32m
    YELLOW=\033[1;33m
    BLUE=\033[1;34m
    RED=\033[0;31m
    CYAN=\033[0;36m
    NC=\033[0m
endif

# Simple symbols that work everywhere
ARROW=[>]
CHECK=[OK]
CROSS=[FAIL]
ROCKET=[>]
WRENCH=[>]
MICROSCOPE=[>]
TRAFFIC_LIGHT=[>]
SEED=[>]
PACKAGE=[>]
WHALE=[>]

# Get console width (default to 80 if not available)
CONSOLE_WIDTH := $(shell tput cols 2>/dev/null || echo 80)

build:
	@echo ""
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(ARROW) Starting Build Process [build] $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(BLUE)$(WRENCH) Running: mvn clean install -DskipTests -q$(NC)"
	@if mvn clean install -DskipTests -q; then \
		echo "$(GREEN)$(CHECK) Build completed successfully!$(NC)"; \
	else \
		echo "$(RED)$(CROSS) Build failed!$(NC)"; exit 1; \
	fi
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(ARROW) Build Process Finished $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo ""

test:
	@echo ""
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(MICROSCOPE) Running Tests [test] $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(YELLOW)📝 Executing: mvn verify -q$(NC)"
	@if mvn verify -q; then \
		echo "$(GREEN)$(CHECK) All tests passed!$(NC)"; \
	else \
		echo "$(RED)$(CROSS) Some tests failed!$(NC)"; exit 1; \
	fi
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(ARROW) Test Run Finished $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo ""

run:
	@echo ""
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(TRAFFIC_LIGHT) Starting Application [run] $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(GREEN)$(SEED) Launching: mvn spring-boot:run -q$(NC)"
	mvn spring-boot:run -q
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(ARROW) Application Stopped $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo ""

deploy:
	@echo ""
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(PACKAGE) Deploying Docker Image [deploy] $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(BLUE)$(WHALE) Building Docker image: foodmlops-app:latest$(NC)"
	@if docker build -t foodmlops-app:latest .; then \
		echo "$(GREEN)$(CHECK) Docker image built successfully!$(NC)"; \
		echo "$(CYAN)$(ARROW) To run: docker run -p 8080:8080 foodmlops-app:latest$(NC)"; \
	else \
		echo "$(RED)$(CROSS) Docker build failed!$(NC)"; exit 1; \
	fi
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo "$(CYAN) $(ARROW) Docker Deployment Finished $(NC)"
	@echo "$(CYAN)$(shell printf '=%.0s' {1..$(CONSOLE_WIDTH)})$(NC)"
	@echo ""

clean: ## Clean the project
	@echo "$(GREEN)$(SEED) Cleaning project...$(NC)"
	mvn clean -q
	@echo "$(GREEN)$(SEED) Project cleaned successfully$(NC)"






