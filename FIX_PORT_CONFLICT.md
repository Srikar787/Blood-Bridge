# 🔧 Fix Port 8081 Conflict Error

## Error Message
```
Error response from daemon: Ports are not available: exposing port TCP 0.0.0.0:8081 -> 127.0.0.1:0: listen tcp 0.0.0.0:8081: bind: Only one usage of each socket address (protocol/network address/port) is normally permitted.
```

## What This Means
Port 8081 is already being used by another application. Docker can't use it because it's occupied.

## Quick Fixes

### Solution 1: Stop What's Using Port 8081 (Recommended)

**Windows:**
1. Open Command Prompt as Administrator
2. Find what's using the port:
   ```cmd
   netstat -ano | findstr :8081
   ```
3. You'll see output like:
   ```
   TCP    0.0.0.0:8081    0.0.0.0:0    LISTENING    12345
   ```
4. Note the PID (last number, e.g., 12345)
5. Kill that process:
   ```cmd
   taskkill /PID 12345 /F
   ```

**Or use the helper script:**
- Double-click `FIX_PORT_8081.bat`
- It will find and offer to kill the process

**Mac/Linux:**
```bash
# Find what's using port 8081
lsof -i :8081

# Kill it (replace PID with the number from above)
kill -9 <PID>
```

### Solution 2: Stop Docker Containers

If you have Docker containers already running:

```bash
# Stop all containers
docker-compose down

# Or stop specific container
docker stop bloodbridge-backend
```

### Solution 3: Change Docker Port (Alternative)

If you want to keep the other application running on 8081:

1. Edit `docker-compose.yml`
2. Find this line:
   ```yaml
   ports:
     - "8081:8081"
   ```
3. Change to a different port (e.g., 8082):
   ```yaml
   ports:
     - "8082:8081"
   ```
4. Now backend will be accessible at http://localhost:8082
5. Update frontend API URLs if needed

### Solution 4: Stop Your Backend (If Running Locally)

If you have the backend running outside Docker:

**Windows:**
```cmd
# Find Java processes
tasklist | findstr java

# Kill Java processes (if it's your backend)
taskkill /F /IM java.exe
```

**Mac/Linux:**
```bash
# Find Java processes
ps aux | grep java

# Kill specific process
kill -9 <PID>
```

## Step-by-Step Fix

### Step 1: Check What's Using Port 8081

**Windows PowerShell:**
```powershell
Get-NetTCPConnection -LocalPort 8081 | Select-Object OwningProcess
```

**Windows CMD:**
```cmd
netstat -ano | findstr :8081
```

**Mac/Linux:**
```bash
lsof -i :8081
```

### Step 2: Stop the Process

**If it's your backend running locally:**
- Stop it from your IDE/terminal
- Or kill the process using the PID from Step 1

**If it's another Docker container:**
```bash
docker ps
docker stop <container-name>
```

### Step 3: Verify Port is Free

**Windows:**
```cmd
netstat -ano | findstr :8081
```

**Should return nothing** (port is free)

**Mac/Linux:**
```bash
lsof -i :8081
```

**Should return nothing** (port is free)

### Step 4: Start Docker Again

```bash
docker-compose up -d
```

## Common Scenarios

### Scenario 1: Backend Running in IDE
**Problem:** You started the backend from IntelliJ/Eclipse/VS Code
**Solution:** Stop it from the IDE or kill the Java process

### Scenario 2: Previous Docker Container
**Problem:** Old Docker container still running
**Solution:** 
```bash
docker-compose down
docker-compose up -d
```

### Scenario 3: Another Application
**Problem:** Some other app is using port 8081
**Solution:** Change Docker port to 8082 (see Solution 3 above)

## Quick Commands Reference

```bash
# Check what's using port 8081
netstat -ano | findstr :8081  # Windows
lsof -i :8081                  # Mac/Linux

# Stop Docker containers
docker-compose down

# Kill process by PID (Windows)
taskkill /PID <PID> /F

# Kill process by PID (Mac/Linux)
kill -9 <PID>

# Start Docker again
docker-compose up -d
```

## Prevention

To avoid this in the future:
1. **Always stop Docker containers** before starting backend locally:
   ```bash
   docker-compose down
   ```

2. **Or use different ports** for local development vs Docker

3. **Check before starting:**
   ```bash
   # Windows
   netstat -ano | findstr :8081
   
   # Mac/Linux
   lsof -i :8081
   ```

---

**Most likely cause:** Your backend is still running from a previous session. Stop it first, then run Docker!

