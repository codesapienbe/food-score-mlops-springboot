// Service Worker for Food Score MLOps PWA
const CACHE_NAME = 'food-score-mlops-v1.0.0';
const STATIC_CACHE_NAME = 'food-score-mlops-static-v1.0.0';
const DYNAMIC_CACHE_NAME = 'food-score-mlops-dynamic-v1.0.0';

// Files to cache immediately
const STATIC_FILES = [
  '/',
  '/index.html',
  '/manifest.json',
  '/pwa-register.js',
  '/theme/food-score-mlops/styles.css',
  '/generated/index.ts',
  '/generated/vaadin.ts',
  '/generated/vaadin-featureflags.ts'
];

// API endpoints to cache
const API_CACHE_PATTERNS = [
  '/food-ml/api/',
  '/food-ml/actuator/',
  '/food-ml/h2-console/'
];

// Install event - cache static files
self.addEventListener('install', (event) => {
  console.log('Service Worker installing...');
  
  event.waitUntil(
    caches.open(STATIC_CACHE_NAME)
      .then((cache) => {
        console.log('Caching static files');
        return cache.addAll(STATIC_FILES);
      })
      .then(() => {
        console.log('Static files cached successfully');
        return self.skipWaiting();
      })
      .catch((error) => {
        console.error('Failed to cache static files:', error);
      })
  );
});

// Activate event - clean up old caches
self.addEventListener('activate', (event) => {
  console.log('Service Worker activating...');
  
  event.waitUntil(
    caches.keys()
      .then((cacheNames) => {
        return Promise.all(
          cacheNames.map((cacheName) => {
            if (cacheName !== STATIC_CACHE_NAME && 
                cacheName !== DYNAMIC_CACHE_NAME &&
                cacheName !== CACHE_NAME) {
              console.log('Deleting old cache:', cacheName);
              return caches.delete(cacheName);
            }
          })
        );
      })
      .then(() => {
        console.log('Service Worker activated successfully');
        return self.clients.claim();
      })
  );
});

// Fetch event - serve from cache when possible
self.addEventListener('fetch', (event) => {
  const { request } = event;
  const url = new URL(request.url);
  
  // Skip non-GET requests
  if (request.method !== 'GET') {
    return;
  }
  
  // Skip non-HTTP(S) requests
  if (!url.protocol.startsWith('http')) {
    return;
  }
  
  // Handle different types of requests
  if (isStaticFile(url.pathname)) {
    event.respondWith(handleStaticFile(request));
  } else if (isApiRequest(url.pathname)) {
    event.respondWith(handleApiRequest(request));
  } else {
    event.respondWith(handleDynamicRequest(request));
  }
});

// Check if request is for a static file
function isStaticFile(pathname) {
  return STATIC_FILES.some(file => pathname.endsWith(file)) ||
         pathname.startsWith('/generated/') ||
         pathname.startsWith('/theme/') ||
         pathname.includes('.js') ||
         pathname.includes('.css') ||
         pathname.includes('.png') ||
         pathname.includes('.jpg') ||
         pathname.includes('.svg');
}

// Check if request is for an API endpoint
function isApiRequest(pathname) {
  return API_CACHE_PATTERNS.some(pattern => pathname.startsWith(pattern));
}

// Handle static file requests
async function handleStaticFile(request) {
  try {
    // Try to serve from cache first
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }
    
    // If not in cache, fetch from network and cache
    const networkResponse = await fetch(request);
    if (networkResponse.ok) {
      const cache = await caches.open(STATIC_CACHE_NAME);
      cache.put(request, networkResponse.clone());
    }
    
    return networkResponse;
  } catch (error) {
    console.error('Failed to handle static file request:', error);
    // Return a fallback response
    return new Response('Static file not available', { status: 404 });
  }
}

// Handle API requests
async function handleApiRequest(request) {
  try {
    // Try network first for API requests
    const networkResponse = await fetch(request);
    
    if (networkResponse.ok) {
      // Cache successful API responses
      const cache = await caches.open(DYNAMIC_CACHE_NAME);
      cache.put(request, networkResponse.clone());
    }
    
    return networkResponse;
  } catch (error) {
    console.error('API request failed, trying cache:', error);
    
    // Try to serve from cache if network fails
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }
    
    // Return error response
    return new Response('API not available offline', { status: 503 });
  }
}

// Handle dynamic requests (HTML pages)
async function handleDynamicRequest(request) {
  try {
    // Try network first
    const networkResponse = await fetch(request);
    
    if (networkResponse.ok) {
      // Cache successful responses
      const cache = await caches.open(DYNAMIC_CACHE_NAME);
      cache.put(request, networkResponse.clone());
    }
    
    return networkResponse;
  } catch (error) {
    console.error('Dynamic request failed, trying cache:', error);
    
    // Try to serve from cache if network fails
    const cachedResponse = await caches.match(request);
    if (cachedResponse) {
      return cachedResponse;
    }
    
    // Return offline page
    return caches.match('/index.html');
  }
}

// Background sync for offline functionality
self.addEventListener('sync', (event) => {
  if (event.tag === 'background-sync') {
    console.log('Background sync triggered');
    
    event.waitUntil(
      // Perform background sync operations
      performBackgroundSync()
        .then(() => {
          // Notify clients of successful sync
          self.clients.matchAll().then((clients) => {
            clients.forEach((client) => {
              client.postMessage({
                type: 'BACKGROUND_SYNC',
                status: 'completed'
              });
            });
          });
        })
        .catch((error) => {
          console.error('Background sync failed:', error);
        })
    );
  }
});

// Perform background sync operations
async function performBackgroundSync() {
  // Example: Sync offline data when connection is restored
  const offlineData = await getOfflineData();
  
  if (offlineData.length > 0) {
    console.log('Syncing offline data:', offlineData.length, 'items');
    
    // Process offline data
    for (const data of offlineData) {
      try {
        await syncDataItem(data);
      } catch (error) {
        console.error('Failed to sync data item:', error);
      }
    }
    
    // Clear synced data
    await clearOfflineData();
  }
}

// Get offline data from IndexedDB or other storage
async function getOfflineData() {
  // This would typically use IndexedDB
  // For now, return empty array
  return [];
}

// Sync individual data item
async function syncDataItem(data) {
  // Implement data synchronization logic
  console.log('Syncing data item:', data);
}

// Clear offline data after successful sync
async function clearOfflineData() {
  // Clear synced data from storage
  console.log('Clearing offline data');
}

// Handle push notifications
self.addEventListener('push', (event) => {
  console.log('Push notification received:', event);
  
  if (event.data) {
    const data = event.data.json();
    
    const options = {
      body: data.body || 'New notification from Food Score MLOps',
      icon: '/icons/icon-192x192.png',
      badge: '/icons/icon-72x72.png',
      tag: 'food-score-mlops-notification',
      data: data.data || {},
      actions: data.actions || []
    };
    
    event.waitUntil(
      self.registration.showNotification(data.title || 'Food Score MLOps', options)
    );
  }
});

// Handle notification clicks
self.addEventListener('notificationclick', (event) => {
  console.log('Notification clicked:', event);
  
  event.notification.close();
  
  if (event.action) {
    // Handle specific action
    console.log('Action clicked:', event.action);
  } else {
    // Default action - open the app
    event.waitUntil(
      self.clients.openWindow('/')
    );
  }
});

// Handle notification close
self.addEventListener('notificationclose', (event) => {
  console.log('Notification closed:', event);
});

// Message handling for communication with main thread
self.addEventListener('message', (event) => {
  console.log('Service Worker received message:', event.data);
  
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting();
  }
}); 