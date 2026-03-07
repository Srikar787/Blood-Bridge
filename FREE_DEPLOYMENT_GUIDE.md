# 🚀 Free Deployment Guide - Deploy Your Docker App for Free

## Overview

This guide shows you how to deploy your containerized BloodBridge application to free cloud platforms so others can access it via a link.

## 🎯 Best Free Options

### Option 1: Railway (Recommended - Easiest) ⭐
- **Free Tier:** $5 credit/month (enough for small apps)
- **Docker Support:** ✅ Native
- **Setup Time:** 5 minutes
- **Best For:** Quick deployment, easy setup

### Option 2: Render
- **Free Tier:** 750 hours/month
- **Docker Support:** ✅ Yes
- **Setup Time:** 10 minutes
- **Best For:** Reliable, good documentation

### Option 3: Fly.io
- **Free Tier:** 3 shared VMs
- **Docker Support:** ✅ Yes
- **Setup Time:** 15 minutes
- **Best For:** Global deployment

---

## 🚂 Option 1: Railway (Recommended)

### Why Railway?
- ✅ Easiest setup
- ✅ Automatic Docker detection
- ✅ Free $5 credit/month
- ✅ HTTPS included
- ✅ Custom domain support

### Step-by-Step Deployment

#### Step 1: Sign Up
1. Go to: https://railway.app/
2. Click **"Start a New Project"**
3. Sign up with GitHub (recommended) or email

#### Step 2: Create New Project
1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"** (if using GitHub)
   - OR **"Empty Project"** if deploying manually

#### Step 3: Connect Repository (If Using GitHub)
1. Authorize Railway to access your GitHub
2. Select your repository (`z2` or whatever you named it)
3. Railway will auto-detect Docker

#### Step 4: Configure Services

Railway will detect your `docker-compose.yml` and create services.

**For Manual Setup:**
1. Click **"New"** → **"Service"**
2. Select **"GitHub Repo"** or **"Dockerfile"**
3. Point to your `backend/Dockerfile`
4. Add environment variables (see below)

#### Step 5: Add Environment Variables

Click on your service → **Variables** tab → Add these:

```env
# MongoDB (use your existing Atlas connection)
SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
SPRING_DATA_MONGODB_DATABASE=bloodbridge

# Server
SERVER_PORT=8081

# JWT
JWT_SECRET=YourSecretKeyHere

# CORS - Update with Railway URL after deployment
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-app.railway.app

# Google OAuth - Update redirect URI
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=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
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://your-backend.railway.app/login/oauth2/code/google

# Email
EMAIL_ENABLED=true
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb

# SMS
SMS_ENABLED=true
SMS_USE_FREE_GATEWAY=true
```

#### Step 6: Deploy Frontend

1. Create another service for frontend
2. Point to `frontend/Dockerfile`
3. Add environment variable:
   ```env
   REACT_APP_API_URL=https://your-backend.railway.app
   ```

#### Step 7: Get Your URLs

After deployment:
- Railway will give you URLs like:
  - Backend: `https://bloodbridge-backend-production.up.railway.app`
  - Frontend: `https://bloodbridge-frontend-production.up.railway.app`

#### Step 8: Update OAuth Redirect URI

1. Go to Google Cloud Console
2. Edit your OAuth client
3. Add redirect URI: `https://your-backend.railway.app/login/oauth2/code/google`
4. Update CORS in Railway environment variables

### Railway Pricing
- **Free:** $5 credit/month (usually enough for 1-2 small apps)
- **Hobby:** $5/month (if you need more)

---

## 🎨 Option 2: Render

### Step-by-Step Deployment

#### Step 1: Sign Up
1. Go to: https://render.com/
2. Sign up with GitHub (recommended)

#### Step 2: Create Web Service
1. Click **"New"** → **"Web Service"**
2. Connect your GitHub repository
3. Render will detect Docker

#### Step 3: Configure Backend Service
- **Name:** `bloodbridge-backend`
- **Environment:** Docker
- **Dockerfile Path:** `backend/Dockerfile`
- **Docker Context:** `backend/`
- **Port:** 8081

#### Step 4: Add Environment Variables
Same as Railway (see above)

#### Step 5: Deploy Frontend
1. Create another Web Service
2. **Dockerfile Path:** `frontend/Dockerfile`
3. **Docker Context:** `frontend/`
4. **Port:** 80

#### Step 6: Get URLs
- Render gives you URLs like:
  - `https://bloodbridge-backend.onrender.com`
  - `https://bloodbridge-frontend.onrender.com`

### Render Pricing
- **Free Tier:** 750 hours/month (enough for 24/7 if you have 1 service)
- **Note:** Free tier spins down after 15 min inactivity (takes ~30s to wake up)

---

## ✈️ Option 3: Fly.io

### Step-by-Step Deployment

#### Step 1: Install Fly CLI
```bash
# Windows (PowerShell)
iwr https://fly.io/install.ps1 -useb | iex

# Mac/Linux
curl -L https://fly.io/install.sh | sh
```

#### Step 2: Sign Up
```bash
fly auth signup
```

#### Step 3: Create Fly App
```bash
# In your project root
fly launch
```

#### Step 4: Configure for Docker Compose
Create `fly.toml` for backend:
```toml
app = "bloodbridge-backend"
primary_region = "iad"

[build]
  dockerfile = "backend/Dockerfile"

[env]
  PORT = "8081"

[[services]]
  internal_port = 8081
  protocol = "tcp"
```

#### Step 5: Deploy
```bash
fly deploy
```

### Fly.io Pricing
- **Free:** 3 shared VMs (256MB RAM each)
- **Good for:** Small apps, global deployment

---

## 🔧 Quick Setup Script for Railway

I'll create a Railway-specific configuration file:




