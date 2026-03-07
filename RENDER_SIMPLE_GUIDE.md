# 🚀 Simple Guide: Deploy Both Services on Render

## What You Need to Do
Create **2 separate services** on Render - one for backend, one for frontend.

---

## 📋 PART 1: Deploy Backend

### Step 1: Create Backend Service
1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"Web Service"**
3. Connect GitHub repo: `Srikar787/Blood-Bridge`
4. Branch: `branch1`

### Step 2: Configure Backend
**Settings:**
- Name: `bloodbridge-backend`
- Instance: `Free`
- **Dockerfile Path:** `backend/Dockerfile`
- **Docker Build Context Directory:** `backend`
- **Docker Command:** (leave EMPTY)

**Environment Variables** (click "Environment" tab):
```
SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
SERVER_PORT=8081
JWT_SECRET=BloodBridgeSecretKeyForJWTTokenGeneration2024SecureKeyReplaceThisWithASecureRandomStringInProduction
EMAIL_ENABLED=true
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb
SMS_ENABLED=true
SMS_USE_FREE_GATEWAY=true
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=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
```

### Step 3: Deploy & Copy URL
1. Click **"Create Web Service"**
2. Wait for build (5-10 min)
3. **Copy the backend URL** (e.g., `https://bloodbridge-backend-xxxx.onrender.com`)
4. **Save this URL** - you'll need it!

---

## 📋 PART 2: Deploy Frontend

### Step 1: Create Frontend Service
1. Click **"New +"** → **"Web Service"** again
2. Same repo: `Srikar787/Blood-Bridge`
3. Branch: `branch1`

### Step 2: Configure Frontend
**Settings:**
- Name: `bloodbridge-frontend`
- Instance: `Free`
- **Dockerfile Path:** `frontend/Dockerfile`
- **Docker Build Context Directory:** `frontend`
- **Docker Command:** (leave EMPTY)

**Environment Variables** (click "Environment" tab):
```
BACKEND_URL=https://YOUR-BACKEND-URL.onrender.com
REACT_APP_API_URL=https://YOUR-BACKEND-URL.onrender.com
```

**⚠️ Replace `YOUR-BACKEND-URL` with the actual backend URL from Part 1!**

### Step 3: Deploy Frontend
1. Click **"Create Web Service"**
2. Wait for build (5-10 min)
3. **Copy the frontend URL** (e.g., `https://bloodbridge-frontend-xxxx.onrender.com`)

---

## 📋 PART 3: Connect Them Together

### Update Backend CORS
1. Go back to **backend service**
2. Click **"Environment"** tab
3. Add this variable:
   ```
   SPRING_WEB_CORS_ALLOWED_ORIGINS=https://YOUR-FRONTEND-URL.onrender.com
   ```
   (Replace with your actual frontend URL)

### Update Google OAuth Redirect
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. **APIs & Services** → **Credentials**
3. Edit your OAuth Client ID
4. Add redirect URI:
   ```
   https://YOUR-BACKEND-URL.onrender.com/login/oauth2/code/google
   ```
5. Add to backend environment variables:
   ```
   SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://YOUR-BACKEND-URL.onrender.com/login/oauth2/code/google
   ```

### Redeploy Backend
1. Click **"Manual Deploy"** → **"Deploy latest commit"**
2. Wait for deployment

---

## ✅ Test Your App

1. Open frontend URL: `https://bloodbridge-frontend-xxxx.onrender.com`
2. Try to register/login
3. Test all features!

---

## 🎯 Quick Checklist

**Backend:**
- [ ] Dockerfile Path: `backend/Dockerfile`
- [ ] Docker Context: `backend`
- [ ] Docker Command: (empty)
- [ ] All environment variables set
- [ ] CORS origin set to frontend URL
- [ ] Google OAuth redirect URI updated

**Frontend:**
- [ ] Dockerfile Path: `frontend/Dockerfile`
- [ ] Docker Context: `frontend`
- [ ] Docker Command: (empty)
- [ ] `BACKEND_URL` environment variable set
- [ ] `REACT_APP_API_URL` environment variable set

---

## 🆘 Common Issues

**Build fails?**
- Check Docker Context is correct (`backend` or `frontend`)
- Check Dockerfile Path is correct
- Check Docker Command is EMPTY

**Frontend can't connect?**
- Check `BACKEND_URL` is correct (with `https://`)
- Check backend CORS allows frontend URL
- Check backend is running

**CORS errors?**
- Make sure frontend URL is in `SPRING_WEB_CORS_ALLOWED_ORIGINS`
- Use `https://` not `http://`
- No trailing slash

---

**That's it! Your app should be live! 🎉**



