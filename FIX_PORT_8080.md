# 🔧 Fix: Port 8080 Already in Use

Your backend can't start because port 8080 is already being used by another process (probably from a previous backend run).

## Quick Fix Steps:

### Step 1: Find What's Using Port 8080

1. Open **Command Prompt** (new window)
2. Run this command:
   ```bash
   netstat -ano | findstr :8080
   ```
3. You'll see output like:
   ```
   TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       12345
   ```
   The last number (12345) is the **Process ID (PID)**

### Step 2: Kill the Process

1. Use the PID from Step 1
2. Run this command (replace 12345 with your actual PID):
   ```bash
   taskkill /PID 12345 /F
   ```
3. You should see: `SUCCESS: The process with PID 12345 has been terminated.`

### Step 3: Start Backend Again

1. Go back to your backend terminal
2. Run:
   ```bash
   mvn spring-boot:run
   ```
3. Now it should start successfully!

---

## Alternative: One-Line Solution

If you want to do it all at once:

```bash
for /f "tokens=5" %a in ('netstat -ano ^| findstr :8080') do taskkill /PID %a /F
```

Then restart your backend.

---

## Why This Happens

- You started the backend before
- You closed the terminal window but the process kept running
- Another application is using port 8080

---

## Prevention

Always stop the backend properly:
- Press `Ctrl + C` in the backend terminal
- Wait for it to stop completely
- Then close the terminal



