# 🌐 Deploy with ngrok - Share Your Local Docker App Instantly

## What is ngrok?

ngrok creates a **secure tunnel** from the internet to your local machine. Perfect for:
- ✅ Sharing your app with others instantly
- ✅ Testing webhooks
- ✅ Demo purposes
- ✅ **FREE** tier available

## Why ngrok for Your Docker App?

- ✅ **Instant** - Works in 2 minutes
- ✅ **Free** - Free tier available
- ✅ **No code changes** - Your Docker setup stays the same
- ✅ **HTTPS included** - Secure connection
- ✅ **Perfect for testing** - Share with testers immediately

## Quick Start (5 Minutes)

### Step 1: Sign Up for ngrok

1. Go to: https://ngrok.com/
2. Sign up for free account
3. Verify your email

### Step 2: Get Your Auth Token

1. Go to: https://dashboard.ngrok.com/get-started/your-authtoken
2. Copy your authtoken (looks like: `2abc123def456ghi789jkl012mno345pq_6r7s8t9u0v1w2x3y4z5`)

### Step 3: Install ngrok

**Windows:**
1. Download: https://ngrok.com/download
2. Extract `ngrok.exe` to a folder (e.g., `C:\ngrok\`)
3. Or use Chocolatey: `choco install ngrok`

**Mac:**
```bash
brew install ngrok/ngrok/ngrok
```

**Linux:**
```bash
# Download
curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list
sudo apt update && sudo apt install ngrok
```

### Step 4: Configure ngrok

**Windows (Command Prompt):**
```cmd
ngrok config add-authtoken YOUR_AUTH_TOKEN_HERE
```

**Mac/Linux:**
```bash
ngrok config add-authtoken YOUR_AUTH_TOKEN_HERE
```

### Step 5: Start Your Docker Containers

Make sure your Docker containers are running:
```bash
docker-compose up -d
```

Verify:
- Frontend: http://localhost:3000
- Backend: http://localhost:8081

### Step 6: Create ngrok Tunnels

**Option A: Tunnel Frontend Only (Simplest)**

Open a new terminal and run:
```bash
ngrok http 3000
```

You'll get a URL like: `https://abc123.ngrok-free.app`

**Share this URL!** It forwards to your local frontend.

**Option B: Tunnel Both Frontend and Backend (Recommended)**

**Terminal 1 - Frontend:**
```bash
ngrok http 3000
```
Copy the URL (e.g., `https://frontend-abc123.ngrok-free.app`)

**Terminal 2 - Backend:**
```bash
ngrok http 8081
```
Copy the URL (e.g., `https://backend-xyz789.ngrok-free.app`)

### Step 7: Update Configuration

Since your frontend uses hardcoded `http://localhost:8081`, you need to update it for ngrok.

**Quick Fix - Update Frontend API URLs:**

The frontend code has hardcoded URLs. For ngrok, you have two options:

**Option 1: Use ngrok's request rewriting (Easier)**
- Keep frontend code as-is
- Use ngrok's web interface to rewrite requests
- Or update frontend to use relative paths

**Option 2: Update Frontend Code (Better)**
- Change `http://localhost:8081/api/...` to use ngrok backend URL
- Or use environment variables

### Step 8: Update OAuth Redirect URI

1. Go to Google Cloud Console
2. Edit OAuth Client
3. Add redirect URI: `https://your-backend-ngrok-url.ngrok-free.app/login/oauth2/code/google`

### Step 9: Update CORS

Update your backend environment variable:
```env
SPRING_WEB_CORS_ALLOWED_ORIGINS=https://your-frontend-ngrok-url.ngrok-free.app
```

Then restart backend:
```bash
docker-compose restart backend
```

## ngrok Free Tier

- ✅ **1 tunnel** at a time
- ✅ **HTTPS** included
- ✅ **Random subdomain** (changes each time)
- ✅ **40 connections/minute** limit
- ✅ **Perfect for testing/demos**

## ngrok Paid Features (Optional)

- **Static domain** (same URL every time)
- **Multiple tunnels**
- **More connections**
- **Custom domains**

## Quick Script for Windows

Create `START_NGROK.bat`:

```batch
@echo off
echo Starting ngrok tunnel for frontend...
echo.
echo Your app will be available at the ngrok URL shown below
echo.
echo Make sure Docker containers are running first!
echo.
ngrok http 3000
pause
```

## Important Notes

### ⚠️ ngrok URLs Change Each Time

- Free tier gives you a **random URL** each time you start ngrok
- You'll need to:
  - Update OAuth redirect URI each time
  - Update CORS each time
  - Share new URL with testers

**Solution:** Use ngrok's static domain (paid) or update configs each time

### ⚠️ Your Computer Must Stay On

- ngrok tunnels require your local machine to be running
- If you close ngrok or shut down, the link stops working

### ⚠️ Frontend API Calls

Your frontend has hardcoded `http://localhost:8081` URLs. For ngrok to work properly:

**Quick Fix:**
1. Use ngrok for backend too
2. Update frontend to use ngrok backend URL
3. Or use relative paths (`/api/...`) if nginx proxy is configured

## Better Solution: Use ngrok with Docker Compose

I'll create a docker-compose setup that includes ngrok!

