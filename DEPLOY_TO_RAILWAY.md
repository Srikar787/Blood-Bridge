# 🚂 Deploy to Railway - Step-by-Step Guide

## Why Railway?
- ✅ **Easiest** free deployment
- ✅ **Automatic** Docker detection
- ✅ **Free $5 credit/month**
- ✅ **HTTPS included** (free SSL)
- ✅ **Custom domains** supported
- ✅ **5-minute setup**

## Prerequisites

1. GitHub account (recommended) OR Railway account
2. Your code pushed to GitHub (optional but recommended)

## Step-by-Step Deployment

### Step 1: Prepare Your Code

#### Option A: Push to GitHub (Recommended)
1. Create a new repository on GitHub
2. Push your code:
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/yourrepo.git
   git push -u origin main
   ```

#### Option B: Deploy Directly
- You can deploy without GitHub, but GitHub is easier

### Step 2: Sign Up for Railway

1. Go to: https://railway.app/
2. Click **"Start a New Project"**
3. Sign up with **GitHub** (easiest) or email
4. Authorize Railway to access your GitHub (if using GitHub)

### Step 3: Create New Project

1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Choose your repository
4. Railway will automatically detect your `docker-compose.yml`!

### Step 4: Railway Auto-Setup

Railway will:
- ✅ Detect `docker-compose.yml`
- ✅ Create services for backend and frontend
- ✅ Start building automatically

**Wait for the build to complete** (5-10 minutes first time)

### Step 5: Configure Environment Variables

#### For Backend Service:

1. Click on **backend** service
2. Go to **Variables** tab
3. Add these environment variables:

```env
SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
SPRING_DATA_MONGODB_DATABASE=bloodbridge
SERVER_PORT=8081
JWT_SECRET=BloodBridgeSecretKeyForJWTTokenGeneration2024SecureKeyReplaceThisWithASecureRandomStringInProduction
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb
EMAIL_ENABLED=true
SMS_ENABLED=true
SMS_USE_FREE_GATEWAY=true
```

**Important:** After deployment, you'll get URLs. Then add:
```env
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend-url.railway.app
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://your-backend-url.railway.app/login/oauth2/code/google
```

#### For Frontend Service:

1. Click on **frontend** service
2. Go to **Variables** tab
3. Add (after backend is deployed):
```env
REACT_APP_API_URL=https://your-backend-url.railway.app
```

### Step 6: Get Your URLs

After deployment completes:

1. Click on each service
2. Go to **Settings** tab
3. Under **"Networking"**, you'll see:
   - **Public Domain:** `https://bloodbridge-backend-production-xxxx.up.railway.app`
   - **Public Domain:** `https://bloodbridge-frontend-production-xxxx.up.railway.app`

**Copy these URLs!**

### Step 7: Update OAuth Configuration

1. Go to Google Cloud Console: https://console.cloud.google.com/
2. Go to **APIs & Services** → **Credentials**
3. Edit your OAuth 2.0 Client ID
4. Add **Authorized redirect URI:**
   ```
   https://your-backend-url.railway.app/login/oauth2/code/google
   ```
5. Save

### Step 8: Update CORS

1. Go back to Railway
2. Backend service → **Variables**
3. Update:
   ```env
   SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend-url.railway.app
   ```
4. Redeploy (Railway auto-redeploys on variable changes)

### Step 9: Share Your Link!

Your app is now live at:
- **Frontend:** `https://your-frontend-url.railway.app`
- **Backend:** `https://your-backend-url.railway.app`

**Share the frontend URL with others to test!** 🎉

## Custom Domain (Optional)

1. Go to service → **Settings** → **Networking**
2. Click **"Generate Domain"** or **"Custom Domain"**
3. Add your domain (e.g., `bloodbridge.yourdomain.com`)
4. Follow DNS setup instructions

## Monitoring

### View Logs:
- Click on service → **Deployments** → Click deployment → **View Logs**

### Check Status:
- Dashboard shows service status
- Green = Running
- Yellow = Building
- Red = Error

## Troubleshooting

### Build Fails
- Check logs in Railway dashboard
- Verify Dockerfile is correct
- Check environment variables

### App Not Loading
- Check backend logs
- Verify MongoDB connection
- Check CORS settings

### OAuth Not Working
- Verify redirect URI matches exactly
- Check backend logs for OAuth errors
- Ensure CORS allows frontend URL

## Cost

- **Free Tier:** $5 credit/month
- **Usually enough for:** 1-2 small apps running 24/7
- **If you exceed:** Railway will pause services (you'll get notified)

## Next Steps

1. ✅ Deploy to Railway
2. ✅ Get your URLs
3. ✅ Update OAuth redirect URI
4. ✅ Update CORS
5. ✅ Test the app
6. ✅ Share the link! 🚀

---

**Your app will be live and accessible to anyone with the link!**

