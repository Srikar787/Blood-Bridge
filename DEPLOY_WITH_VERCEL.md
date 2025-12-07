# ▲ Deploy with Vercel - Frontend Only (Backend Needs Separate Hosting)

## What is Vercel?

Vercel is excellent for **frontend deployment** but has limitations for full-stack apps:
- ✅ **Perfect for React frontends**
- ✅ **Free tier** with great performance
- ✅ **Automatic HTTPS**
- ✅ **Global CDN**
- ❌ **Not ideal for Spring Boot backends** (needs separate hosting)

## Recommended Setup

**Best Approach:**
- **Frontend:** Deploy to Vercel ✅
- **Backend:** Deploy to Railway/Render/Fly.io ✅
- **Connect them:** Update frontend API URLs

## Step-by-Step: Deploy Frontend to Vercel

### Step 1: Prepare Frontend for Production

Update frontend to use environment variables for API URL.

**Create `frontend/.env.production`:**
```env
REACT_APP_API_URL=https://your-backend-url.railway.app
```

**Or update `frontend/src/config.js`** to use:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 
  (process.env.NODE_ENV === 'production' ? 'https://your-backend-url.railway.app' : 'http://localhost:8081');
```

### Step 2: Sign Up for Vercel

1. Go to: https://vercel.com/
2. Sign up with GitHub (recommended)

### Step 3: Deploy Frontend

1. Click **"Add New"** → **"Project"**
2. Import your GitHub repository
3. Configure:
   - **Framework Preset:** Create React App
   - **Root Directory:** `frontend`
   - **Build Command:** `npm run build`
   - **Output Directory:** `build`
   - **Install Command:** `npm install`

### Step 4: Add Environment Variables

In Vercel project settings → **Environment Variables**:

```env
REACT_APP_API_URL=https://your-backend-url.railway.app
```

### Step 5: Deploy Backend Separately

Deploy backend to Railway/Render (see other guides):
- Railway: `DEPLOY_TO_RAILWAY.md`
- Render: `DEPLOY_TO_RENDER.md`

### Step 6: Update CORS

In your backend (Railway/Render), add:
```env
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend.vercel.app
```

### Step 7: Get Your URLs

- **Frontend:** `https://your-app.vercel.app` (from Vercel)
- **Backend:** `https://your-backend.railway.app` (from Railway)

**Share the frontend URL!** 🎉

## Vercel Configuration File

Create `vercel.json` in your project root:

```json
{
  "version": 2,
  "builds": [
    {
      "src": "frontend/package.json",
      "use": "@vercel/static-build",
      "config": {
        "distDir": "build"
      }
    }
  ],
  "routes": [
    {
      "src": "/api/(.*)",
      "dest": "https://your-backend-url.railway.app/api/$1"
    },
    {
      "src": "/(.*)",
      "dest": "/frontend/$1"
    }
  ]
}
```

## Vercel Free Tier

- ✅ **Unlimited** deployments
- ✅ **100GB bandwidth/month**
- ✅ **HTTPS** included
- ✅ **Custom domains**
- ✅ **Perfect for frontends**

## Limitations

- ❌ **No backend hosting** (Spring Boot doesn't work on Vercel)
- ✅ **Solution:** Use Railway/Render for backend

## Alternative: Vercel + ngrok

If you want to keep backend local:
1. Deploy frontend to Vercel
2. Use ngrok for backend (see `DEPLOY_WITH_NGROK.md`)
3. Update frontend API URL to ngrok backend URL

---

**Best Practice:** Deploy frontend to Vercel + backend to Railway = Best of both worlds! 🚀

