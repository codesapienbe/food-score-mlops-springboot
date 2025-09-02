// PWA Registration and Installation Script
class PWARegistration {
  constructor() {
    this.deferredPrompt = null;
    this.installButton = null;
    this.isInstalled = false;
    
    this.init();
  }

  init() {
    this.registerServiceWorker();
    this.setupInstallPrompt();
    this.checkInstallationStatus();
    this.setupUpdateNotification();
  }

  async registerServiceWorker() {
    if ('serviceWorker' in navigator) {
      try {
        const registration = await navigator.serviceWorker.register('/sw.js');
        console.log('Service Worker registered successfully:', registration);

        // Handle updates
        registration.addEventListener('updatefound', () => {
          const newWorker = registration.installing;
          newWorker.addEventListener('statechange', () => {
            if (newWorker.state === 'installed' && navigator.serviceWorker.controller) {
              this.showUpdateNotification();
            }
          });
        });

        // Handle service worker messages
        navigator.serviceWorker.addEventListener('message', (event) => {
          if (event.data.type === 'BACKGROUND_SYNC') {
            this.showNotification('Background sync completed', 'success');
          }
        });

      } catch (error) {
        console.error('Service Worker registration failed:', error);
      }
    } else {
      console.log('Service Worker not supported');
    }
  }

  setupInstallPrompt() {
    window.addEventListener('beforeinstallprompt', (event) => {
      event.preventDefault();
      this.deferredPrompt = event;
      
      // Show install button if not already installed
      if (!this.isInstalled) {
        this.showInstallButton();
      }
    });

    window.addEventListener('appinstalled', () => {
      this.isInstalled = true;
      this.hideInstallButton();
      this.deferredPrompt = null;
      
      // Track installation
      if (typeof gtag !== 'undefined') {
        gtag('event', 'pwa_install', {
          event_category: 'engagement',
          event_label: 'Food Score MLOps'
        });
      }
      
      console.log('PWA installed successfully');
    });
  }

  showInstallButton() {
    // Create install button if it doesn't exist
    if (!this.installButton) {
      this.installButton = document.createElement('button');
      this.installButton.textContent = '📱 Install App';
      this.installButton.className = 'pwa-install-button';
      this.installButton.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 1000;
        padding: 12px 20px;
        background: #1976d2;
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        transition: all 0.3s ease;
      `;
      
      this.installButton.addEventListener('click', () => {
        this.installPWA();
      });
      
      document.body.appendChild(this.installButton);
    }
    
    this.installButton.style.display = 'block';
  }

  hideInstallButton() {
    if (this.installButton) {
      this.installButton.style.display = 'none';
    }
  }

  async installPWA() {
    if (this.deferredPrompt) {
      this.deferredPrompt.prompt();
      
      const { outcome } = await this.deferredPrompt.userChoice;
      
      if (outcome === 'accepted') {
        console.log('User accepted PWA installation');
      } else {
        console.log('User declined PWA installation');
      }
      
      this.deferredPrompt = null;
      this.hideInstallButton();
    }
  }

  checkInstallationStatus() {
    // Check if app is running in standalone mode (installed)
    if (window.matchMedia('(display-mode: standalone)').matches || 
        window.navigator.standalone === true) {
      this.isInstalled = true;
      console.log('PWA is running in standalone mode');
    }
  }

  setupUpdateNotification() {
    // Check for app updates
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.ready.then((registration) => {
        registration.update();
      });
    }
  }

  showUpdateNotification() {
    const notification = document.createElement('div');
    notification.className = 'pwa-update-notification';
    notification.style.cssText = `
      position: fixed;
      top: 20px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 1000;
      padding: 16px 24px;
      background: #4caf50;
      color: white;
      border-radius: 8px;
      font-size: 14px;
      font-weight: 500;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      display: flex;
      align-items: center;
      gap: 12px;
    `;
    
    notification.innerHTML = `
      <span>🔄 New version available</span>
      <button onclick="this.parentElement.remove()" style="
        background: rgba(255,255,255,0.2);
        border: none;
        color: white;
        padding: 4px 8px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 12px;
      ">Reload</button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto-remove after 10 seconds
    setTimeout(() => {
      if (notification.parentElement) {
        notification.remove();
      }
    }, 10000);
  }

  showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `pwa-notification pwa-notification-${type}`;
    notification.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 1000;
      padding: 12px 20px;
      background: ${type === 'success' ? '#4caf50' : '#2196f3'};
      color: white;
      border-radius: 8px;
      font-size: 14px;
      font-weight: 500;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      animation: slideIn 0.3s ease;
    `;
    
    notification.textContent = message;
    document.body.appendChild(notification);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
      if (notification.parentElement) {
        notification.remove();
      }
    }, 5000);
  }

  // Request notification permission
  async requestNotificationPermission() {
    if ('Notification' in window) {
      const permission = await Notification.requestPermission();
      if (permission === 'granted') {
        console.log('Notification permission granted');
        return true;
      } else {
        console.log('Notification permission denied');
        return false;
      }
    }
    return false;
  }

  // Send push notification
  sendNotification(title, options = {}) {
    if ('Notification' in window && Notification.permission === 'granted') {
      const notification = new Notification(title, {
        icon: '/icons/icon-192x192.png',
        badge: '/icons/icon-72x72.png',
        ...options
      });
      
      notification.onclick = () => {
        window.focus();
        notification.close();
      };
      
      return notification;
    }
  }
}

// Initialize PWA when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
  window.pwaRegistration = new PWARegistration();
});

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
  @keyframes slideIn {
    from {
      transform: translateX(100%);
      opacity: 0;
    }
    to {
      transform: translateX(0);
      opacity: 1;
    }
  }
  
  .pwa-install-button:hover {
    background: #1565c0 !important;
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0,0,0,0.2) !important;
  }
  
  .pwa-install-button:active {
    transform: translateY(0);
  }
`;
document.head.appendChild(style); 