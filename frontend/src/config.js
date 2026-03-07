// API Configuration
// For production on Railway, set REACT_APP_API_URL environment variable to your backend URL
// Example: REACT_APP_API_URL=https://bloodbridge-backend-production-xxxx.up.railway.app

const API_BASE_URL = process.env.REACT_APP_API_URL || 
  (process.env.NODE_ENV === 'production' 
    ? '' // Use relative path in production if REACT_APP_API_URL not set
    : 'http://localhost:8081' // Dev default
  );

export const API_URL = API_BASE_URL;
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${API_BASE_URL}/api/auth/login`,
    REGISTER: `${API_BASE_URL}/api/auth/register`,
    OAUTH_GOOGLE: `${API_BASE_URL}/oauth2/authorization/google`,
  },
  DONORS: {
    LIST: `${API_BASE_URL}/api/donors`,
    CREATE: `${API_BASE_URL}/api/donors`,
    DELETE: (id) => `${API_BASE_URL}/api/donors/${id}`,
    SEARCH_ELIGIBILITY: `${API_BASE_URL}/api/donors/search/eligibility-criteria`,
    SEARCH_NEARBY: `${API_BASE_URL}/api/donors/search/nearby`,
  },
  NOTIFICATIONS: {
    SEND: `${API_BASE_URL}/api/notifications/send`,
  },
};

export default API_URL;

