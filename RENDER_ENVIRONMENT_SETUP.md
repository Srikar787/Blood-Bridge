# 🔧 Render Environment Variables Setup Guide

This guide will help you configure your Render deployment with the correct environment variables for OAuth and CORS.

## 🎯 Your Render URLs

Based on your deployment:
- **Frontend URL**: `https://blood-bridge-2-omij.onrender.com`
- **Backend URL**: `https://blood-bridge-1-5g02.onrender.com`

## 📋 Step 1: Set Backend Environment Variables

Go to your **Backend Service** in Render dashboard:

1. Click on your backend service
2. Go to **Environment** tab
3. Add/Update these environment variables:

### Required Environment Variables:

```bash
# Frontend URL (for OAuth redirects)
APP_FRONTEND_URL=https://blood-bridge-2-omij.onrender.com

# CORS - Allow your frontend to access backend
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://blood-bridge-2-omij.onrender.com

# OAuth Redirect URI
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google
```

### Optional (if you want to override other settings):

```bash
# MongoDB (if different from application.properties)
SPRING_DATA_MONGODB_URI=your_mongodb_uri

# JWT Secret (use a strong random string in production)
JWT_SECRET=your_secure_jwt_secret

# Email Configuration
SPRING_MAIL_USERNAME=your_email@gmail.com
SPRING_MAIL_PASSWORD=your_app_password
```

## 📋 Step 2: Set Frontend Environment Variables

Go to your **Frontend Service** in Render dashboard:

1. Click on your frontend service
2. Go to **Environment** tab
3. Add/Update this environment variable:

```bash
# Backend API URL
BACKEND_URL=https://blood-bridge-1-5g02.onrender.com
REACT_APP_API_URL=https://blood-bridge-1-5g02.onrender.com
```

**Note**: You already have these set correctly! ✅

## 🔐 Step 3: Update Google OAuth Console

You need to update your Google OAuth credentials to allow your Render URLs:

1. **Go to Google Cloud Console**
   - Visit: https://console.cloud.google.com/
   - Sign in with your Google account

2. **Navigate to OAuth Credentials**
   - Go to **APIs & Services** → **Credentials**
   - Find your OAuth 2.0 Client ID (the one with ID: `212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop`)
   - Click on it to edit

3. **Update Authorized JavaScript origins**
   - Add:
     ```
     https://blood-bridge-2-omij.onrender.com
     https://blood-bridge-1-5g02.onrender.com
     ```
   - Keep `http://localhost:3000` and `http://localhost:8081` for local development

4. **Update Authorized redirect URIs**
   - Add:
     ```
     https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google
     ```
   - Keep `http://localhost:8081/login/oauth2/code/google` for local development

5. **Click Save**

## 🔄 Step 4: Redeploy Services

After setting environment variables:

1. **Backend**: Render will auto-redeploy when you save environment variables
   - Or manually trigger: Go to **Manual Deploy** → **Deploy latest commit**

2. **Frontend**: Render will auto-redeploy when you save environment variables
   - Or manually trigger: Go to **Manual Deploy** → **Deploy latest commit**

## ✅ Step 5: Verify Configuration

### Test Regular Login:
1. Go to: `https://blood-bridge-2-omij.onrender.com/login`
2. Enter your email and password
3. Should login successfully

### Test Google OAuth:
1. Go to: `https://blood-bridge-2-omij.onrender.com/login`
2. Click "Continue with Google"
3. Should redirect to Google login
4. After login, should redirect back to your app

## 🐛 Troubleshooting

### Issue: "Login failed" with correct credentials

**Possible causes:**
1. **CORS not configured**: Check `SPRING_WEB_CORS_ALLOWED_ORIGINS` includes your frontend URL
2. **Backend not accessible**: Verify backend URL is correct in frontend `BACKEND_URL`
3. **Database connection**: Check MongoDB connection string is correct

**Solution:**
- Check backend logs in Render dashboard
- Verify environment variables are set correctly
- Test backend health endpoint: `https://blood-bridge-1-5g02.onrender.com/api/donors/health`

### Issue: "Redirect URI mismatch" for Google OAuth

**Solution:**
- Verify redirect URI in Google Console exactly matches:
  ```
  https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google
  ```
- Check `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI` in backend environment variables

### Issue: "CORS error" in browser console

**Solution:**
- Verify `SPRING_WEB_CORS_ALLOWED_ORIGINS` includes your frontend URL
- Make sure there are no trailing slashes
- Format: `https://blood-bridge-2-omij.onrender.com` (no trailing slash)

### Issue: Frontend can't connect to backend

**Solution:**
- Verify `BACKEND_URL` in frontend environment variables (should be `https://blood-bridge-1-5g02.onrender.com`)
- Check backend is running and accessible
- Test backend URL directly in browser: `https://blood-bridge-1-5g02.onrender.com/api/donors/health`

## 📝 Quick Checklist

- [ ] Backend `APP_FRONTEND_URL` set to frontend URL
- [ ] Backend `SPRING_WEB_CORS_ALLOWED_ORIGINS` includes frontend URL
- [ ] Backend `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI` set to backend OAuth endpoint
- [ ] Frontend `BACKEND_URL` set to backend URL
- [ ] Google OAuth Console updated with Render URLs
- [ ] Both services redeployed
- [ ] Tested regular login
- [ ] Tested Google OAuth login

## 🎉 Success!

Once all environment variables are set and Google OAuth is updated, your application should work correctly on Render!

