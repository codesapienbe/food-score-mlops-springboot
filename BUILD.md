# Food Score MLOps Build Process

## ✅ **Integrated Build - No More Separate Commands!**

The frontend is now automatically built during the standard Maven lifecycle. You no longer need separate commands for frontend and backend.

## 🚀 **Simple Build Commands**

### **Development Build (Default)**

```bash
mvn clean install
```

This will:

1. Clean previous builds
2. Prepare frontend dependencies
3. Build frontend assets
4. Compile Java backend
5. Run tests
6. Package everything into a single JAR

### **Production Build**

```bash
mvn clean install -Pprod
```

This will:

1. Build with production optimizations
2. Enable minification and compression
3. Use production Vaadin mode

### **CI/CD Build (Minimal Memory)**

```bash
mvn clean install -Pci
```

This will:

1. Use minimal memory settings
2. Optimize for CI/CD environments
3. Reduce memory footprint

## 🔧 **What Happens During Build**

### **Phase: validate**

- Vaadin prepares frontend dependencies
- Downloads required npm packages

### **Phase: compile**

- Vaadin builds frontend assets
- Compiles TypeScript/JavaScript
- Generates optimized bundles

### **Phase: process-resources**

- Frontend assets copied to `target/frontend/`
- Assets packaged into final JAR

### **Phase: clean**

- Frontend build artifacts cleaned
- No manual cleanup needed

## 📁 **Build Output Structure**

```tree
target/
├── classes/                          ← Compiled Java classes
├── frontend/                         ← Built frontend assets
│   ├── index.html                   ← Main HTML
│   ├── manifest.json                ← PWA manifest
│   ├── pwa-register.js             ← PWA registration
│   ├── sw.js                       ← Service worker
│   ├── theme/                       ← Compiled theme
│   └── generated/                   ← Vaadin components
└── food-score-mlops-1.0.0.jar      ← Final executable JAR
```

## 🎯 **Memory Management**

### **Development Profile (Default)**

- Initial memory: 256MB
- Maximum memory: 512MB
- Fast builds, no optimization

### **Production Profile**

- Initial memory: 1GB
- Maximum memory: 2GB
- Full optimization, slower builds

### **CI/CD Profile**

- Initial memory: 128MB
- Maximum memory: 256MB
- Minimal memory usage

## ✅ **What You Get**

- 🎯 **Single command**: `mvn clean install`
- 🔄 **Automatic frontend builds**
- 📦 **Everything packaged in one JAR**
- 🚀 **Ready to run immediately**
- 💾 **Memory-optimized builds**
- 🔧 **Profile-based configuration**

## 🐛 **Troubleshooting**

### **Memory Issues**

```bash
# Use CI profile for low memory
mvn clean install -Pci

# Or increase Maven memory
export MAVEN_OPTS="-Xms512m -Xmx1g"
mvn clean install
```

### **Build Failures**

```bash
# Clean everything and rebuild
mvn clean
mvn vaadin:clean-frontend
mvn clean install
```

### **Frontend Not Updating**

```bash
# Force clean rebuild
mvn clean install -U
```

## 🎉 **Result**

Now you can build your entire application (frontend + backend) with a single command:

```bash
mvn clean install
```

The frontend is automatically built, optimized, and packaged with your Spring Boot application. No more separate environments or build steps!
