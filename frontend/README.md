# Food Score MLOps Frontend

This directory contains the frontend source code for the Food Score MLOps application built with Vaadin and Spring Boot.

## Project Structure

```
frontend/
├── index.html              # Main HTML entry point
├── manifest.json           # PWA manifest file
├── pwa-register.js        # PWA registration script
├── sw.js                  # Service worker for offline functionality
├── vite.config.ts         # Vite build configuration
├── generated/              # Vaadin-generated files (auto-generated)
│   ├── index.ts           # Main TypeScript entry point
│   ├── vaadin.ts          # Vaadin configuration
│   └── jar-resources/     # Vaadin component resources
└── theme/                  # Custom theme files
    └── food-score-mlops/
        ├── styles.css      # Custom CSS styles
        └── theme.json      # Theme configuration
```

## Development Workflow

### 1. Development Mode
```bash
# Start development server with hot reload
npm run dev

# Or use Maven
mvn vaadin:prepare-frontend
mvn vaadin:start-frontend
```

### 2. Building for Production
```bash
# Build frontend assets
npm run build

# Or use Maven
mvn vaadin:build-frontend
```

### 3. Clean Build
```bash
# Clean generated files
npm run clean

# Or use Maven
mvn clean
```

## Key Points

### ✅ Correct Structure
- **Source files** go in `frontend/` directory
- **Built files** are automatically generated in `target/frontend/`
- **Spring Boot serves** from `target/frontend/` during runtime

### ❌ Avoid This
- **Don't write source code** in `src/main/resources/META-INF/resources/`
- **Don't manually edit** files in `target/frontend/`
- **Don't commit** generated files to version control

## Vaadin + Spring Boot Integration

1. **Development**: Edit files in `frontend/` directory
2. **Build**: Vaadin builds from `frontend/` → `target/frontend/`
3. **Runtime**: Spring Boot serves from `target/frontend/`
4. **Hot Reload**: Available during development with `npm run dev`

## PWA Features

- **Service Worker**: Offline functionality and caching
- **Manifest**: App installation and home screen support
- **Install Prompt**: Native app-like installation experience
- **Offline Support**: Cached resources for offline use

## Customization

- **Theme**: Modify `theme/food-score-mlops/styles.css`
- **PWA**: Update `manifest.json` and `sw.js`
- **Components**: Add custom TypeScript/JavaScript in `frontend/`
- **Assets**: Place images, icons, etc. in `frontend/` directory

## Troubleshooting

### Build Issues
```bash
# Clean and rebuild
mvn clean
mvn vaadin:prepare-frontend
mvn vaadin:build-frontend
```

### Development Server Issues
```bash
# Clear npm cache
npm cache clean --force

# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

### File Not Found Errors
- Ensure files are in `frontend/` directory, not `META-INF/resources/`
- Check that build process completed successfully
- Verify file paths in `index.html` are correct 