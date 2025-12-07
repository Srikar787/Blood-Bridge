# ⚡ Quick Deploy - Get Your App Online in 5 Minutes

## 🚂 Railway (Easiest - Recommended)

### 1. Sign Up
- Go to: https://railway.app/
- Sign up with GitHub

### 2. Deploy
- Click **"New Project"** → **"Deploy from GitHub repo"**
- Select your repository
- Railway auto-detects Docker! ✅

### 3. Add Environment Variables
Backend service → Variables → Add:
```
SPRING_DATA_MONGODB_URI=your-mongodb-uri
SPRING_MAIL_USERNAME=your-email
SPRING_MAIL_PASSWORD=your-password
```

### 4. Get Your Link
- Railway gives you: `https://your-app.railway.app`
- **Share this link!** 🎉

**That's it!** Your app is live.

---

## 🎨 Render (Alternative)

### 1. Sign Up
- Go to: https://render.com/
- Sign up with GitHub

### 2. Create Web Service
- **New** → **Web Service**
- Connect repo
- Set: **Environment = Docker**
- **Dockerfile Path:** `backend/Dockerfile`

### 3. Deploy
- Add environment variables
- Click **Create**
- Get URL: `https://your-app.onrender.com`

---

## 📋 What You Need

Before deploying, make sure you have:
- [ ] Code pushed to GitHub (recommended)
- [ ] MongoDB Atlas connection string
- [ ] Gmail App Password (for email)
- [ ] Google OAuth credentials (optional)

---

## 🔗 After Deployment

1. **Get your URLs** from the platform
2. **Update OAuth redirect URI** in Google Console
3. **Update CORS** in environment variables
4. **Share the frontend URL** with testers!

---

## 💰 Cost

- **Railway:** $5 free credit/month (usually enough)
- **Render:** 750 hours/month free (enough for 24/7)

**Both are free for small apps!**

---

**See detailed guides:**
- `DEPLOY_TO_RAILWAY.md` - Complete Railway guide
- `DEPLOY_TO_RENDER.md` - Complete Render guide

