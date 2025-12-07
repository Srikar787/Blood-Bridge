// API Configuration
// In Docker, this will be handled by nginx proxy
// For local development, use http://localhost:8081
// For production, use relative paths or your domain

const API_BASE_URL = process.env.REACT_APP_API_URL || 
  (process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8081');

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

