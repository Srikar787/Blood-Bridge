# 🔧 Docker Troubleshooting - "No Configuration File Found"

## Problem
You're getting an error: `no configuration file found` or `Can't find a suitable configuration file`

## Solution Step-by-Step

### Step 1: Verify You're in the Right Directory

The `docker-compose.yml` file must be in the **root directory** of your project.

**Check your current location:**
```bash
# Windows (PowerShell)
pwd
# or
Get-Location

# Windows (CMD)
cd

# Mac/Linux
pwd
```

**You should see a path like:**
- `C:\Users\srika\Downloads\z2` (Windows)
- `/Users/yourname/Downloads/z2` (Mac)
- `/home/yourname/Downloads/z2` (Linux)

### Step 2: Verify docker-compose.yml Exists

**Check if the file exists:**
```bash
# Windows (PowerShell)
Test-Path docker-compose.yml

# Windows (CMD)
dir docker-compose.yml

# Mac/Linux
ls -la docker-compose.yml
```

**You should see:**
- `True` (PowerShell) or the file listed (CMD/Linux)

### Step 3: Navigate to the Correct Directory

If you're not in the right directory:

**Windows:**
```powershell
cd C:\Users\srika\Downloads\z2
```

**Mac/Linux:**
```bash
cd ~/Downloads/z2
# or wherever you extracted the project
```

### Step 4: List All Files to Confirm

**Verify you can see these files:**
```bash
# Windows (PowerShell)
Get-ChildItem

# Windows (CMD)
dir

# Mac/Linux
ls -la
```

**You should see:**
- `docker-compose.yml` ✅
- `backend/` folder ✅
- `frontend/` folder ✅
- `DOCKER_SETUP.md` ✅

### Step 5: Run Docker Compose

**Once you're in the correct directory:**
```bash
docker-compose up -d
```

**Or if that doesn't work, try:**
```bash
docker compose up -d
```

(Note: Newer Docker versions use `docker compose` without the hyphen)

## Common Issues

### Issue 1: Wrong Directory
**Error:** `no configuration file found: docker-compose.yml`

**Solution:**
1. Find where you downloaded/extracted the project
2. Navigate to that folder
3. Make sure `docker-compose.yml` is in that folder
4. Run `docker-compose up -d` from there

### Issue 2: File Not Found
**Error:** File doesn't exist

**Solution:**
1. Check if you have all project files
2. Make sure `docker-compose.yml` is in the root (same level as `backend/` and `frontend/` folders)
3. If missing, create it (see below)

### Issue 3: Docker Not Running
**Error:** `Cannot connect to the Docker daemon`

**Solution:**
1. **Windows/Mac:** Start Docker Desktop application
2. Wait for it to fully start (whale icon in system tray)
3. Try again

### Issue 4: Wrong Command
**Error:** `docker-compose: command not found`

**Solution:**
- Try `docker compose` (without hyphen) - newer Docker versions
- Or install docker-compose separately

## Visual Guide

Your project structure should look like this:

```
z2/                          ← YOU MUST BE HERE
├── docker-compose.yml       ← This file must exist here
├── backend/
│   ├── Dockerfile
│   └── src/
├── frontend/
│   ├── Dockerfile
│   └── src/
├── DOCKER_SETUP.md
└── ...other files
```

## Step-by-Step for Complete Beginners

### 1. Open Terminal/Command Prompt

**Windows:**
- Press `Win + R`
- Type `cmd` or `powershell`
- Press Enter

**Mac:**
- Press `Cmd + Space`
- Type `Terminal`
- Press Enter

**Linux:**
- Press `Ctrl + Alt + T`

### 2. Navigate to Your Project

**Find your project folder first:**
- It's probably in: `C:\Users\srika\Downloads\z2` (Windows)
- Or: `~/Downloads/z2` (Mac/Linux)

**Then navigate:**
```bash
# Windows
cd C:\Users\srika\Downloads\z2

# Mac/Linux
cd ~/Downloads/z2
```

### 3. Verify You're in the Right Place

```bash
# Windows
dir docker-compose.yml

# Mac/Linux
ls docker-compose.yml
```

**If you see the file listed, you're good!**

### 4. Start Docker Desktop

**Windows/Mac:**
- Open Docker Desktop application
- Wait until it says "Docker is running"
- You'll see a whale icon in your system tray

**Linux:**
- Docker should start automatically
- Check with: `sudo systemctl status docker`

### 5. Run Docker Compose

```bash
# Try this first
docker-compose up -d

# If that doesn't work, try this (newer Docker)
docker compose up -d
```

### 6. Check if It's Working

```bash
# Check running containers
docker ps

# Check logs
docker-compose logs
```

## Still Having Issues?

### Create docker-compose.yml Manually

If the file is missing, create it in the root directory with this content:

```yaml
version: '3.8'

services:
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: bloodbridge-backend
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATA_MONGODB_URI=mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority&ssl=true
      - SPRING_DATA_MONGODB_DATABASE=bloodbridge
      - SERVER_PORT=8081
      - JWT_SECRET=BloodBridgeSecretKeyForJWTTokenGeneration2024SecureKeyReplaceThisWithASecureRandomStringInProduction
      - SPRING_WEB_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:80
      - SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
      - SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=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
      - EMAIL_ENABLED=true
      - SPRING_MAIL_HOST=smtp.gmail.com
      - SPRING_MAIL_PORT=587
      - SPRING_MAIL_USERNAME=srikarreddy3666@gmail.com
      - SPRING_MAIL_PASSWORD=wpxp hamq utvt yasb
      - SMS_ENABLED=true
      - SMS_USE_FREE_GATEWAY=true
    networks:
      - bloodbridge-network
    restart: unless-stopped

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: bloodbridge-frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
    networks:
      - bloodbridge-network
    restart: unless-stopped

networks:
  bloodbridge-network:
    driver: bridge
```

Save it as `docker-compose.yml` in the root directory.

## Quick Test

Run this to verify everything:

```bash
# 1. Check you're in right directory
pwd  # or Get-Location on Windows PowerShell

# 2. List files
ls docker-compose.yml  # or dir docker-compose.yml on Windows

# 3. Check Docker is running
docker ps

# 4. Run docker-compose
docker-compose up -d
```

## Need More Help?

1. **Share your current directory:**
   ```bash
   pwd  # Mac/Linux
   Get-Location  # Windows PowerShell
   ```

2. **Share the exact error message:**
   - Copy the full error text

3. **Check Docker Desktop:**
   - Is it running?
   - Any error messages?

4. **Verify file location:**
   - Can you see `docker-compose.yml` in File Explorer/Finder?

---

**Most common issue: You're not in the directory where docker-compose.yml is located!**

Make sure you're in the root folder (where `backend/` and `frontend/` folders are).

