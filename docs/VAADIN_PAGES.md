# Vaadin Frontend for Food Score MLOps

This document describes the Vaadin frontend that has been added to your Spring Boot MLOps application, providing a modern, responsive web interface with Progressive Web App (PWA) capabilities.

## 🚀 Features

### Core Functionality

- **Single Page Application (SPA)** with client-side routing
- **Responsive Design** that works on desktop, tablet, and mobile devices
- **Modern UI Components** using Vaadin Flow and Lumo theme
- **Real-time Updates** for system monitoring and metrics

### Views Available

1. **Dashboard** - System overview with key metrics and quick actions
2. **Food Search** - Search and browse food products with quality scores
3. **Predictions** - Make new food quality predictions and view history
4. **ML Models** - Monitor model performance and training status
5. **AI Chat** - Interactive chat interface for questions about the system
6. **Monitoring** - System health, performance metrics, and alerts

### PWA Features

- **Offline Support** with service worker caching
- **Installable** - Can be installed as a native app
- **Push Notifications** for system alerts and updates
- **Background Sync** for offline actions
- **Responsive Design** optimized for all screen sizes

## 🛠️ Building the Application

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Node.js 18 or higher (for frontend build)

### Build Commands

1. **Clean and build the entire project:**

   ```bash
   mvn clean install
   ```

2. **Build only the frontend:**

   ```bash
   mvn vaadin:prepare-frontend
   mvn vaadin:build-frontend
   ```

3. **Development mode with hot reload:**

   ```bash
   mvn spring-boot:run
   ```

### Frontend Development

The Vaadin frontend is automatically built during the Maven build process. The frontend files are generated in:

- `target/frontend/` - Generated frontend files
- `src/main/resources/META-INF/resources/` - Static resources

## 🎨 Customization

### Theme Customization

The application uses a custom theme located at:

```
src/main/resources/META-INF/resources/frontend/theme/food-score-mlops/
```

Key customization files:

- `theme.json` - Theme configuration
- `styles.css` - Custom CSS styles
- `styles.js` - Theme JavaScript (if needed)

### Color Scheme

The theme uses a food-inspired color palette:

- **Primary**: Green (#4caf50) - Represents healthy food
- **Secondary**: Orange (#ff9800) - Represents processed foods
- **Success**: Green (#4caf50) - Healthy status
- **Warning**: Orange (#ff9800) - Caution status
- **Error**: Red (#f44336) - Error status

### Component Styling

All Vaadin components can be customized using:

- CSS custom properties (CSS variables)
- Lumo utility classes
- Custom CSS classes defined in `styles.css`

## 📱 PWA Configuration

### Manifest File

The PWA manifest is located at:

```
src/main/resources/META-INF/resources/manifest.json
```

Key PWA settings:

- **App Name**: Food Score MLOps
- **Display Mode**: Standalone (app-like experience)
- **Theme Color**: #1976d2 (Vaadin blue)
- **Icons**: Multiple sizes for different devices

### Service Worker

The service worker (`sw.js`) provides:

- **Offline caching** of static assets and API responses
- **Background sync** for offline actions
- **Push notification** handling
- **Update notifications** for new versions

### PWA Registration

The PWA registration script (`pwa-register.js`) handles:

- **Service worker registration**
- **Install prompts**
- **Update notifications**
- **Notification permissions**

## 🔧 Configuration

### Application Properties

Add these properties to `application.yml` for PWA configuration:

```yaml
vaadin:
  productionMode: true
  frontend:
    theme: food-score-mlops
  pwa:
    enabled: true
    manifest-path: /manifest.json
    service-worker-path: /sw.js
```

### Security Configuration

The frontend integrates with your existing Spring Security configuration. Ensure CORS is properly configured if needed:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
```

## 🚀 Running the Application

### Development Mode

1. Start the application:

   ```bash
   mvn spring-boot:run
   ```

2. Open your browser and navigate to:

   ```
   http://localhost:8080
   ```

3. The Vaadin frontend will automatically load with hot reload enabled.

### Production Mode

1. Build the application:

   ```bash
   mvn clean package
   ```

2. Run the JAR file:

   ```bash
   java -jar target/food-score-mlops-1.0.0.jar
   ```

3. Access the application at:

   ```
   http://localhost:8080
   ```

## 📊 Frontend Architecture

### Component Structure

```
MainView (AppLayout)
├── DashboardView
├── FoodSearchView
├── PredictionsView
├── MLModelsView
├── ChatView
└── MonitoringView
```

### Routing

- **Main Route**: `/` - Main application layout
- **Dashboard**: `/dashboard` - System overview
- **Food Search**: `/food-search` - Product search
- **Predictions**: `/predictions` - Quality predictions
- **ML Models**: `/ml-models` - Model monitoring
- **Chat**: `/chat` - AI assistant
- **Monitoring**: `/monitoring` - System health

### State Management

The application uses Vaadin's built-in state management:

- **Component State**: Managed by Vaadin Flow
- **Session Data**: Stored in Spring session
- **Real-time Updates**: WebSocket connections for live data

## 🎯 Integration Points

### Backend Services

The frontend integrates with your existing Spring Boot services:

- **FoodController** - Food search and data
- **PredictionController** - Quality predictions
- **MLModelController** - Model management
- **ChatController** - AI chat functionality
- **ProductionOperationsController** - System operations

### Data Flow

1. **User Interaction** → Vaadin Component Event
2. **Component Event** → Service Method Call
3. **Service Method** → Backend API Call
4. **API Response** → Component Update
5. **UI Update** → User Feedback

## 🔍 Troubleshooting

### Common Issues

1. **Frontend not loading:**
   - Check if Vaadin dependencies are properly added to `pom.xml`
   - Ensure Node.js is installed and accessible
   - Run `mvn vaadin:prepare-frontend` to regenerate frontend files

2. **PWA not working:**
   - Verify `manifest.json` and `sw.js` are accessible
   - Check browser console for service worker errors
   - Ensure HTTPS is used in production (PWA requirement)

3. **Styling issues:**
   - Check if custom CSS is properly loaded
   - Verify theme configuration in `theme.json`
   - Use browser dev tools to inspect component styles

4. **Build errors:**
   - Clean project: `mvn clean`
   - Delete `target/` directory
   - Rebuild: `mvn clean install`

### Debug Mode

Enable debug mode by adding to `application.yml`:

```yaml
logging:
  level:
    com.vaadin: DEBUG
    com.university.foodml: DEBUG
```

## 📚 Additional Resources

### Vaadin Documentation

- [Vaadin Flow Documentation](https://vaadin.com/docs/latest/flow)
- [Vaadin Components](https://vaadin.com/components)
- [Lumo Theme](https://vaadin.com/themes/lumo)

### PWA Resources

- [PWA Documentation](https://web.dev/progressive-web-apps/)
- [Service Worker API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API)
- [Web App Manifest](https://developer.mozilla.org/en-US/docs/Web/Manifest)

### Spring Boot Integration

- [Spring Boot Vaadin Starter](https://vaadin.com/start/latest/spring-boot)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## 🤝 Contributing

When adding new features to the frontend:

1. **Follow Vaadin best practices** for component creation
2. **Use the existing theme system** for consistent styling
3. **Implement responsive design** for all new components
4. **Add proper error handling** and user feedback
5. **Test on multiple devices** and screen sizes
6. **Update this documentation** for new features

## 📄 License

This frontend is part of your Food Score MLOps application and follows the same licensing terms as your main project.
