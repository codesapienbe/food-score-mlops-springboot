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
  }

  hideInstallButton() {
    if (this.installButton) {
      this.installButton.remove();
      this.installButton = null;
    }
  }

  async installPWA() {
    if (this.deferredPrompt) {
      this.deferredPrompt.prompt();
      const { outcome } = await this.deferredPrompt.userChoice;
      
      if (outcome === 'accepted') {
        console.log('User accepted the install prompt');
      } else {
        console.log('User dismissed the install prompt');
      }
      
      this.deferredPrompt = null;
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
    // Show update notification when new version is available
    const updateNotification = document.createElement('div');
    updateNotification.className = 'update-notification';
    updateNotification.innerHTML = `
      <div style="
        position: fixed;
        top: 20px;
        left: 20px;
        background: #4caf50;
        color: white;
        padding: 16px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 1001;
        max-width: 300px;
      ">
        <div style="font-weight: 500; margin-bottom: 8px;">🔄 Update Available</div>
        <div style="font-size: 14px; margin-bottom: 12px;">A new version is available. Refresh to update.</div>
        <button onclick="location.reload()" style="
          background: white;
          color: #4caf50;
          border: none;
          padding: 8px 16px;
          border-radius: 4px;
          cursor: pointer;
          font-weight: 500;
        ">Refresh Now</button>
        <button onclick="this.parentElement.remove()" style="
          background: transparent;
          color: white;
          border: 1px solid white;
          padding: 8px 16px;
          border-radius: 4px;
          cursor: pointer;
          margin-left: 8px;
        ">Dismiss</button>
      </div>
    `;
    
    document.body.appendChild(updateNotification);
    
    // Auto-remove after 10 seconds
    setTimeout(() => {
      if (updateNotification.parentElement) {
        updateNotification.remove();
      }
    }, 10000);
  }

  showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.style.cssText = `
      position: fixed;
      top: 20px;
      left: 50%;
      transform: translateX(-50%);
      background: ${type === 'success' ? '#4caf50' : type === 'error' ? '#f44336' : '#2196f3'};
      color: white;
      padding: 12px 20px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      z-index: 1002;
      font-size: 14px;
      font-weight: 500;
    `;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    // Auto-remove after 3 seconds
    setTimeout(() => {
      if (notification.parentElement) {
        notification.remove();
      }
    }, 3000);
  }
}

// Initialize PWA registration when DOM is loaded
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    new PWARegistration();
  });
} else {
  new PWARegistration();
} 