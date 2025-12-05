# 🚀 New Features Implementation Guide

This document explains the new features added to BloodBridge:

## ✅ Implemented Features

### 1. Authentication System
- **Sign Up**: Users can register with email/password
- **Login**: Email/password authentication
- **Google OAuth**: Sign in with Google account
- **JWT Tokens**: Secure token-based authentication

### 2. User Roles
- **DONOR**: Users who can donate blood
- **REQUESTOR**: Users who need blood urgently

### 3. Location-Based Donor Search
- **Proximity Search**: Find donors within 5-10km radius
- **Location Input**: Manual entry or GPS location
- **Distance Calculation**: Haversine formula for accurate distance

### 4. Eligibility Criteria
- **Age Check**: 18-65 years
- **Donation Interval**: Minimum 56 days since last donation
- **Required Fields**: Name, email, phone, blood type, gender, location

---

## 📋 Setup Instructions

### Backend Setup

1. **Update application.properties** with your Google OAuth credentials:
   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
   ```

2. **Get Google OAuth Credentials**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing
   - Enable Google+ API
   - Create OAuth 2.0 credentials
   - Add authorized redirect URI: `http://localhost:8081/login/oauth2/code/google`

3. **Restart Backend**:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup

The frontend needs to be updated with:
- Login/Signup pages
- Google OAuth button
- Role selection (Donor/Requestor)
- Requestor dashboard with location input
- Donor search with proximity filter

---

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with email/password
- `GET /api/auth/oauth2/success` - Google OAuth callback
- `GET /api/auth/me` - Get current user info

### Donor Search
- `POST /api/donors/search/nearby` - Search nearby donors
- `GET /api/donors/search/eligibility-criteria` - Get eligibility criteria

### Request Body Examples

**Register:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe",
  "role": "DONOR",
  "phone": "1234567890"
}
```

**Search Nearby Donors:**
```json
{
  "bloodType": "O+",
  "latitude": 12.9716,
  "longitude": 77.5946,
  "radiusKm": 10,
  "gender": "M",
  "minAge": 18,
  "maxAge": 65
}
```

---

## 🎯 Next Steps

1. **Create Frontend Components**:
   - Login page
   - Signup page
   - Role selection
   - Requestor dashboard
   - Location picker (manual/GPS)

2. **Test Authentication**:
   - Test signup/login
   - Test Google OAuth
   - Test JWT token validation

3. **Test Location Search**:
   - Add donors with location
   - Search nearby donors
   - Verify distance calculation

---

## 📝 Notes

- JWT tokens expire after 24 hours
- Location search uses bounding box + distance filter for accuracy
- Eligibility is automatically checked when creating/updating donors
- Google OAuth requires valid credentials in application.properties

