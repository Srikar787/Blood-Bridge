# ⚡ ngrok Quick Start - Share Your App in 2 Minutes

## What You'll Get

A public URL like: `https://abc123.ngrok-free.app` that anyone can access!

## 3 Simple Steps

### Step 1: Install ngrok

**Windows:**
1. Download: https://ngrok.com/download
2. Extract `ngrok.exe`
3. Open Command Prompt in that folder

**Mac:**
```bash
brew install ngrok/ngrok/ngrok
```

### Step 2: Sign Up & Get Token

1. Go to: https://dashboard.ngrok.com/signup
2. Sign up (free)
3. Copy your authtoken from: https://dashboard.ngrok.com/get-started/your-authtoken

### Step 3: Configure & Start

**Configure:**
```bash
ngrok config add-authtoken YOUR_TOKEN_HERE
```

**Start tunnel (make sure Docker is running first!):**
```bash
ngrok http 3000
```

**That's it!** You'll see:
```
Forwarding  https://abc123.ngrok-free.app -> http://localhost:3000
```

**Share that URL!** 🎉

## For Both Frontend & Backend

**Terminal 1:**
```bash
ngrok http 3000
```
Copy frontend URL

**Terminal 2:**
```bash
ngrok http 8081
```
Copy backend URL

**Then:**
1. Update frontend code to use ngrok backend URL
2. Update OAuth redirect URI
3. Update CORS

## Important Notes

- ⚠️ **URL changes each time** (free tier)
- ⚠️ **Your computer must stay on**
- ⚠️ **Perfect for testing/demos**

## Free Tier Limits

- 1 tunnel at a time
- Random subdomain
- 40 connections/minute
- **Perfect for sharing with testers!**

---

**See `DEPLOY_WITH_NGROK.md` for complete guide!**

