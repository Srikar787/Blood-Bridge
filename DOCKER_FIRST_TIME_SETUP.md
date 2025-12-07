# 🐳 Docker First-Time Setup - Complete Beginner Guide

## You Just Downloaded Docker - Let's Get Started!

This guide will walk you through **everything** step by step.

## ✅ Step 1: Verify Docker Desktop is Running

### Windows/Mac:
1. **Open Docker Desktop** (the application you just installed)
2. **Wait for it to start** - You'll see a whale icon 🐳 in your system tray (bottom right on Windows, top bar on Mac)
3. **Check the status** - It should say "Docker Desktop is running"

**If Docker Desktop is NOT running:**
- Open it from your Start Menu (Windows) or Applications (Mac)
- Wait 1-2 minutes for it to fully start
- You'll know it's ready when the whale icon appears

### Verify Docker is Working:
Open a terminal/command prompt and type:
```bash
docker --version
```

**You should see something like:**
```
Docker version 24.0.0, build abc123
```

If you get an error, Docker Desktop isn't running yet. Wait a bit longer.

## ✅ Step 2: Find Your Project Folder

Your project is located at:
```
C:\Users\srika\Downloads\z2
```

**Let's verify this:**

### Option A: Using File Explorer (Windows)
1. Open File Explorer (Windows key + E)
2. Navigate to: `C:\Users\srika\Downloads\z2`
3. **Look for these files:**
   - ✅ `docker-compose.yml` (this is the important one!)
   - ✅ `backend` folder
   - ✅ `frontend` folder

### Option B: Using Command Prompt
1. Press `Windows Key + R`
2. Type `cmd` and press Enter
3. Type this command:
   ```cmd
   cd C:\Users\srika\Downloads\z2
   ```
4. Then type:
   ```cmd
   dir docker-compose.yml
   ```

**You should see:**
```
docker-compose.yml
```

If you see "File Not Found", you're in the wrong folder!

## ✅ Step 3: Open Terminal in the Right Location

### Method 1: From File Explorer (Easiest!)
1. Open File Explorer
2. Navigate to: `C:\Users\srika\Downloads\z2`
3. **Click in the address bar** (where it shows the path)
4. Type `cmd` and press Enter
5. A command prompt will open **in that exact folder** ✅

### Method 2: From Command Prompt
1. Open Command Prompt (Windows Key + R, type `cmd`)
2. Type:
   ```cmd
   cd C:\Users\srika\Downloads\z2
   ```
3. Verify you're in the right place:
   ```cmd
   dir docker-compose.yml
   ```

## ✅ Step 4: Verify docker-compose.yml Exists

In your terminal/command prompt, type:

**Windows Command Prompt:**
```cmd
dir docker-compose.yml
```

**Windows PowerShell:**
```powershell
Test-Path docker-compose.yml
```

**You should see:**
- Command Prompt: The file listed
- PowerShell: `True`

**If you see "File Not Found" or "False":**
- You're in the wrong directory
- Go back to Step 2 and find the correct folder

## ✅ Step 5: Check Docker Compose Command

Docker has two ways to run compose:

### Try Method 1 First (Older Docker):
```bash
docker-compose --version
```

### If That Doesn't Work, Try Method 2 (Newer Docker):
```bash
docker compose version
```

**One of these should work!** Note which one works - you'll use that command.

## ✅ Step 6: Run Docker Compose

**Now that you're in the right directory and Docker is running:**

### If `docker-compose` worked:
```bash
docker-compose up -d
```

### If `docker compose` worked:
```bash
docker compose up -d
```

**What this does:**
- `up` = Start the containers
- `-d` = Run in background (detached mode)

**First time will take 5-10 minutes** because it needs to:
1. Download base images (Java, Node.js, Nginx)
2. Build your application
3. Start everything

**You'll see lots of output** - this is normal! Wait for it to finish.

## ✅ Step 7: Check if It's Working

After the build completes, check if containers are running:

```bash
docker ps
```

**You should see two containers:**
- `bloodbridge-backend`
- `bloodbridge-frontend`

Both should show "Up" status.

## ✅ Step 8: Access Your Application

Once containers are running:

1. **Open your web browser**
2. **Go to:** http://localhost:3000
3. **You should see your BloodBridge application!** 🎉

## 🐛 Troubleshooting

### Error: "no configuration file found"

**This means you're in the wrong directory!**

**Fix:**
1. Check your current directory:
   ```cmd
   cd
   ```
2. Navigate to the project:
   ```cmd
   cd C:\Users\srika\Downloads\z2
   ```
3. Verify file exists:
   ```cmd
   dir docker-compose.yml
   ```
4. Try again:
   ```bash
   docker-compose up -d
   ```

### Error: "Cannot connect to Docker daemon"

**Docker Desktop isn't running!**

**Fix:**
1. Open Docker Desktop application
2. Wait for it to fully start (whale icon appears)
3. Try again

### Error: "docker-compose: command not found"

**Try the newer command:**
```bash
docker compose up -d
```

(Note: no hyphen between docker and compose)

### Error: "Port already in use"

**Something else is using port 3000 or 8081**

**Fix:**
1. Stop other applications using those ports
2. Or change ports in `docker-compose.yml`

### Build Takes Forever

**This is normal the first time!**
- Docker needs to download ~1GB of base images
- Then it builds your application
- Subsequent builds will be much faster

**Just wait - it will finish!**

## 📋 Quick Reference Commands

```bash
# Navigate to project
cd C:\Users\srika\Downloads\z2

# Start containers
docker-compose up -d
# OR
docker compose up -d

# View logs
docker-compose logs -f

# Stop containers
docker-compose down

# Check status
docker ps

# Rebuild after code changes
docker-compose up -d --build
```

## 🎯 Complete Checklist

Before running docker-compose, make sure:

- [ ] Docker Desktop is installed
- [ ] Docker Desktop is running (whale icon visible)
- [ ] You're in the correct directory: `C:\Users\srika\Downloads\z2`
- [ ] `docker-compose.yml` file exists in that directory
- [ ] You can see `backend/` and `frontend/` folders
- [ ] Terminal/Command Prompt is open in the project folder

## 🚀 Still Having Issues?

### Share This Information:

1. **Your current directory:**
   ```cmd
   cd
   ```

2. **Check if file exists:**
   ```cmd
   dir docker-compose.yml
   ```

3. **Docker version:**
   ```bash
   docker --version
   ```

4. **Docker Compose version:**
   ```bash
   docker-compose --version
   # OR
   docker compose version
   ```

5. **The exact error message** you're seeing

## 💡 Pro Tip

**The most common issue:** You're not in the directory where `docker-compose.yml` is located!

**Solution:** Always navigate to the project folder first:
```cmd
cd C:\Users\srika\Downloads\z2
```

Then run docker-compose commands.

---

**Follow these steps exactly, and you'll be up and running!** 🎉

