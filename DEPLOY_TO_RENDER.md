# 🎨 Deploy to Render - Step-by-Step Guide

## Why Render?
- ✅ **Free tier:** 750 hours/month (enough for 24/7)
- ✅ **Easy Docker deployment**
- ✅ **HTTPS included**
- ✅ **Auto-deploy from GitHub**

## Step-by-Step Deployment

### Step 1: Push to GitHub

1. Create GitHub repository
2. Push your code:
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/yourrepo.git
   git push -u origin main
   ```

### Step 2: Sign Up for Render

1. Go to: https://render.com/
2. Click **"Get Started for Free"**
3. Sign up with **GitHub** (recommended)

### Step 3: Create Backend Service

1. Click **"New"** → **"Web Service"**
2. Connect your GitHub repository
3. Configure:
   - **Name:** `bloodbridge-backend`
   - **Environment:** `Docker`
   - **Dockerfile Path:** `backend/Dockerfile`
   - **Docker Context:** `backend/`
   - **Plan:** `Free`
   - **Port:** `8081`

### Step 4: Add Environment Variables (Backend)

Click **"Advanced"** → **"Add Environment Variable"**:

```env
SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
SPRING_DATA_MONGODB_DATABASE=bloodbridge
SERVER_PORT=8081
JWT_SECRET=YourSecretKeyHere
EMAIL_ENABLED=true
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb
SMS_ENABLED=true
SMS_USE_FREE_GATEWAY=true
```

**After deployment, add:**
```env
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend.onrender.com
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://your-backend.onrender.com/login/oauth2/code/google
```

### Step 5: Create Frontend Service

1. Click **"New"** → **"Web Service"**
2. Same repository
3. Configure:
   - **Name:** `bloodbridge-frontend`
   - **Environment:** `Docker`
   - **Dockerfile Path:** `frontend/Dockerfile`
   - **Docker Context:** `frontend/`
   - **Plan:** `Free`
   - **Port:** `80`

### Step 6: Add Environment Variables (Frontend)

```env
REACT_APP_API_URL=https://your-backend.onrender.com
```

### Step 7: Deploy

1. Click **"Create Web Service"**
2. Render will build and deploy
3. Wait 5-10 minutes for first deployment

### Step 8: Get Your URLs

After deployment:
- Backend: `https://bloodbridge-backend.onrender.com`
- Frontend: `https://bloodbridge-frontend.onrender.com`

### Step 9: Update OAuth

1. Google Cloud Console → OAuth Client
2. Add redirect URI: `https://your-backend.onrender.com/login/oauth2/code/google`

### Step 10: Update CORS

1. Backend service → Environment → Add:
   ```env
   SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend.onrender.com
   ```
2. Save (auto-redeploys)

## Render Free Tier Notes

⚠️ **Important:** Free tier services:
- **Spin down** after 15 minutes of inactivity
- Take ~30 seconds to wake up when accessed
- **750 hours/month** total (enough for 24/7 if you have 1 service)

**Solution:** Keep services active with a ping service (free) or upgrade to paid.

## Cost

- **Free:** 750 hours/month per service
- **Paid:** $7/month per service (no spin-down)

---

**Your app is now live on Render!** 🎉

