# 🚀 Complete Guide: Deploy Both Backend & Frontend on Render

## Overview
You need to create **TWO separate services** on Render:
1. **Backend Service** (Spring Boot API)
2. **Frontend Service** (React App)

They will communicate via public URLs (not Docker service names).

---

## 📋 Step 1: Deploy Backend First

### 1.1 Create Backend Service
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository: `Srikar787/Blood-Bridge`
4. Select branch: `branch1` (or `main`)

### 1.2 Configure Backend Service

**Basic Settings:**
- **Name:** `bloodbridge-backend`
- **Region:** `Virginia (US East)` (or closest to you)
- **Instance Type:** `Free`
- **Branch:** `branch1` (or your branch name)

**Build & Deploy Settings:**
- **Root Directory:** (leave empty)
- **Dockerfile Path:** `backend/Dockerfile`
- **Docker Build Context Directory:** `backend`
- **Docker Command:** (leave EMPTY)
- **Health Check Path:** `/api/donors/health`

### 1.3 Set Backend Environment Variables

Click **"Environment"** tab and add these:

```
SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
SERVER_PORT=8081
JWT_SECRET=BloodBridgeSecretKeyForJWTTokenGeneration2024SecureKeyReplaceThisWithASecureRandomStringInProduction
JWT_EXPIRATION=86400000
EMAIL_ENABLED=true
EMAIL_FROM_NAME=BloodBridge
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb
SMS_ENABLED=true
SMS_USE_FREE_GATEWAY=true
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=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
```

**⚠️ IMPORTANT:** Don't set `SPRING_WEB_CORS_ALLOWED_ORIGINS` yet - we'll set it after frontend is deployed!

### 1.4 Deploy Backend
1. Click **"Create Web Service"**
2. Wait for build to complete (5-10 minutes)
3. **Copy the backend URL** (e.g., `https://bloodbridge-backend-xxxx.onrender.com`)

---

## 📋 Step 2: Update Google OAuth Redirect URI

### 2.1 Update in Google Cloud Console
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Navigate to **APIs & Services** → **Credentials**
3. Find your OAuth 2.0 Client ID
4. Add **Authorized redirect URIs:**
   ```
   https://YOUR-BACKEND-URL.onrender.com/login/oauth2/code/google
   ```
   (Replace `YOUR-BACKEND-URL` with your actual backend URL)

### 2.2 Update Backend Environment Variable
Add this to your backend service environment variables:
```
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://YOUR-BACKEND-URL.onrender.com/login/oauth2/code/google
```

---

## 📋 Step 3: Deploy Frontend

### 3.1 Create Frontend Service
1. In Render Dashboard, click **"New +"** → **"Web Service"**
2. Connect same repository: `Srikar787/Blood-Bridge`
3. Select branch: `branch1` (or `main`)

### 3.2 Configure Frontend Service

**Basic Settings:**
- **Name:** `bloodbridge-frontend`
- **Region:** `Virginia (US East)` (same as backend)
- **Instance Type:** `Free`
- **Branch:** `branch1` (or your branch name)

**Build & Deploy Settings:**
- **Root Directory:** (leave empty)
- **Dockerfile Path:** `frontend/Dockerfile`
- **Docker Build Context Directory:** `frontend`
- **Docker Command:** (leave EMPTY)
- **Health Check Path:** `/`

### 3.3 Set Frontend Environment Variables

Click **"Environment"** tab and add:

```
REACT_APP_API_URL=https://YOUR-BACKEND-URL.onrender.com
BACKEND_URL=https://YOUR-BACKEND-URL.onrender.com
```

**⚠️ Replace `YOUR-BACKEND-URL` with your actual backend URL from Step 1.4!**

### 3.4 Deploy Frontend
1. Click **"Create Web Service"**
2. Wait for build to complete (5-10 minutes)
3. **Copy the frontend URL** (e.g., `https://bloodbridge-frontend-xxxx.onrender.com`)

---

## 📋 Step 4: Connect Backend & Frontend

### 4.1 Update Backend CORS
1. Go to your **backend service** in Render
2. Click **"Environment"** tab
3. Add/Update this variable:
   ```
   SPRING_WEB_CORS_ALLOWED_ORIGINS=https://YOUR-FRONTEND-URL.onrender.com
   ```
   (Replace `YOUR-FRONTEND-URL` with your actual frontend URL)

### 4.2 Redeploy Backend
1. Click **"Manual Deploy"** → **"Deploy latest commit"**
2. Wait for deployment to complete

---

## 📋 Step 5: Test Your Deployment

### 5.1 Test Frontend
1. Open your frontend URL: `https://bloodbridge-frontend-xxxx.onrender.com`
2. Try to register/login
3. Check browser console for any errors

### 5.2 Test Backend API
1. Open: `https://YOUR-BACKEND-URL.onrender.com/api/donors/health`
2. Should return: `{"status":"UP"}`

### 5.3 Test Full Flow
1. Register a new user
2. Login
3. Add a donor (if logged in as DONOR)
4. Search for donors (if logged in as REQUESTOR)
5. Send notification

---

## 🔧 Troubleshooting

### Backend Build Fails
- ✅ Check **Docker Build Context Directory** is set to `backend`
- ✅ Check **Dockerfile Path** is `backend/Dockerfile`
- ✅ Check **Docker Command** is EMPTY

### Frontend Build Fails
- ✅ Check **Docker Build Context Directory** is set to `frontend`
- ✅ Check **Dockerfile Path** is `frontend/Dockerfile`
- ✅ Check **Docker Command** is EMPTY

### Frontend Can't Connect to Backend
- ✅ Check `BACKEND_URL` environment variable is set correctly
- ✅ Check backend CORS allows frontend URL
- ✅ Check backend is running (visit health endpoint)

### CORS Errors
- ✅ Make sure `SPRING_WEB_CORS_ALLOWED_ORIGINS` includes your frontend URL
- ✅ Include `https://` (not `http://`) for Render URLs
- ✅ No trailing slash in URLs

### OAuth Not Working
- ✅ Check redirect URI in Google Cloud Console matches backend URL
- ✅ Check `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI` environment variable

---

## 📝 Quick Checklist

**Backend Service:**
- [ ] Dockerfile Path: `backend/Dockerfile`
- [ ] Docker Context: `backend`
- [ ] Docker Command: (empty)
- [ ] MongoDB URI set
- [ ] JWT Secret set
- [ ] Email credentials set
- [ ] Google OAuth credentials set
- [ ] CORS origin set to frontend URL (after frontend deployed)

**Frontend Service:**
- [ ] Dockerfile Path: `frontend/Dockerfile`
- [ ] Docker Context: `frontend`
- [ ] Docker Command: (empty)
- [ ] `BACKEND_URL` environment variable set
- [ ] `REACT_APP_API_URL` environment variable set

---

## 🎉 You're Done!

Your app should now be live at:
- **Frontend:** `https://bloodbridge-frontend-xxxx.onrender.com`
- **Backend:** `https://bloodbridge-backend-xxxx.onrender.com`

Share the **frontend URL** with others to test your app!

---

## 💡 Pro Tips

1. **Free Tier Limits:** Render free tier spins down after 15 minutes of inactivity. First request after spin-down takes ~30 seconds.

2. **Environment Variables:** Keep sensitive data (passwords, API keys) in environment variables, not in code.

3. **Monitoring:** Check Render logs if something doesn't work - they show detailed error messages.

4. **Custom Domain:** You can add a custom domain in Render settings (paid feature).

5. **Database:** Your MongoDB Atlas is already configured and should work from Render.

---

**Need help?** Check the logs in Render dashboard for detailed error messages!

