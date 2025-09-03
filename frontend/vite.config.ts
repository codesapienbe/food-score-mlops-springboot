import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  root: 'frontend',
  build: {
    outDir: '../target/frontend',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'frontend/index.html')
      }
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/food-ml': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  plugins: []
}); 