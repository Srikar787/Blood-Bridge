# 🔧 Troubleshooting: Backend Not Responding

If you see the error: **"Backend is not responding. Please ensure the backend is running on http://localhost:8080"**

Follow these steps to diagnose and fix the issue:

---

## ✅ Step 1: Check if Backend is Running

### Look at Your Backend Terminal Window

1. **Find the Command Prompt window** where you ran `mvn spring-boot:run`
2. **Check if you see this message:**
   ```
   Started BloodBridgeApplication in X.XXX seconds
   ```

### If You See "Started BloodBridgeApplication":
✅ **Backend IS running!** Go to Step 2.

### If You DON'T See "Started BloodBridgeApplication":
❌ **Backend is NOT running!** 

**What to do:**
1. Look for error messages in the terminal
2. Common errors:
   - **MongoDB connection error** → Check Step 3
   - **Port 8080 already in use** → Check Step 4
   - **Compilation errors** → Check Step 5

---

## ✅ Step 2: Test Backend Directly in Browser

Even if the backend terminal shows it started, test it directly:

1. **Open your web browser**
2. **Go to:** http://localhost:8080/api/donors/health
3. **What you should see:**
   - `"BloodBridge API is running!"`

### If You See the Message:
✅ **Backend is working!** The issue might be CORS or frontend configuration. Go to Step 6.

### If You DON'T See the Message:
❌ **Backend is not accessible!** Check:
- Is the backend terminal still open?
- Did you close the terminal window?
- Are there any error messages in the backend terminal?

---

## ✅ Step 3: Check MongoDB Connection

If the backend won't start, check MongoDB connection:

### Look for These Error Messages in Backend Terminal:
```
Exception in thread "main" com.mongodb.MongoException
Cannot connect to MongoDB
Authentication failed
```

### Fix MongoDB Connection:

1. **Check your connection string** in `backend\src\main\resources\application.properties`
2. **Make sure it looks like this:**
   ```properties
   spring.data.mongodb.uri=mongodb+srv://USERNAME:PASSWORD@cluster0.xxxxx.mongodb.net/bloodbridge?retryWrites=true&w=majority
   ```
3. **Common mistakes:**
   - ❌ Extra `/mongodb.net/` in the URL
   - ❌ Wrong username or password
   - ❌ Missing `/bloodbridge` before the `?`
   - ❌ Special characters in password not URL-encoded

4. **Test your connection string:**
   - Go to MongoDB Atlas
   - Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string
   - Make sure it matches what's in `application.properties`

---

## ✅ Step 4: Check if Port 8080 is Already in Use

### Error Message You Might See:
```
Port 8080 is already in use
Address already in use: bind
```

### Solution:

1. **Find what's using port 8080:**
   ```bash
   netstat -ano | findstr :8080
   ```
   
2. **Kill the process** (replace PID with the number you see):
   ```bash
   taskkill /PID <PID> /F
   ```

3. **Or change the port** in `application.properties`:
   ```properties
   server.port=8081
   ```
   Then update frontend to use port 8081 (or restart backend on new port)

---

## ✅ Step 5: Check for Compilation Errors

### Look for These in Backend Terminal:
```
[ERROR] COMPILATION ERROR
cannot find symbol
BUILD FAILURE
```

### Solution:

1. **Make sure all files are saved**
2. **Clean and rebuild:**
   ```bash
   cd C:\Users\srika\Downloads\z2\backend
   mvn clean
   mvn spring-boot:run
   ```

---

## ✅ Step 6: Restart Backend Properly

If backend was running but stopped:

1. **Stop the backend:**
   - Go to backend terminal
   - Press `Ctrl + C`
   - Wait for it to stop

2. **Start it again:**
   ```bash
   cd C:\Users\srika\Downloads\z2\backend
   mvn spring-boot:run
   ```

3. **Wait for:**
   ```
   Started BloodBridgeApplication in X.XXX seconds
   ```

4. **Test again:**
   - Browser: http://localhost:8080/api/donors/health
   - Frontend: Try adding a donor again

---

## ✅ Step 7: Check Backend Terminal for Errors

### Common Error Messages and Solutions:

#### "MongoSocketException: Connection refused"
- **Problem:** Can't connect to MongoDB
- **Solution:** Check MongoDB connection string and network access

#### "Port 8080 already in use"
- **Problem:** Another application is using port 8080
- **Solution:** See Step 4

#### "ClassNotFoundException" or "NoClassDefFoundError"
- **Problem:** Missing dependencies
- **Solution:** Run `mvn clean install` then `mvn spring-boot:run`

#### "Application failed to start"
- **Problem:** Configuration error
- **Solution:** Check `application.properties` for typos

---

## ✅ Step 8: Verify Both Are Running

You need **TWO terminals running:**

### Terminal 1 (Backend):
```bash
cd C:\Users\srika\Downloads\z2\backend
mvn spring-boot:run
```
**Should show:** `Started BloodBridgeApplication`

### Terminal 2 (Frontend):
```bash
cd C:\Users\srika\Downloads\z2\frontend
npm start
```
**Should show:** Browser opens to http://localhost:3000

**⚠️ Important:** Keep BOTH terminals open!

---

## 🎯 Quick Checklist

Before trying to add a donor, verify:

- [ ] Backend terminal shows: `Started BloodBridgeApplication`
- [ ] Backend terminal is still open (not closed)
- [ ] Browser test works: http://localhost:8080/api/donors/health shows message
- [ ] Frontend is running: http://localhost:3000 shows homepage
- [ ] No error messages in backend terminal
- [ ] MongoDB connection string is correct in `application.properties`

---

## 🆘 Still Not Working?

1. **Copy the exact error message** from backend terminal
2. **Check browser console** (Press F12 → Console tab)
3. **Check network tab** (Press F12 → Network tab) when trying to add donor
4. **Share:**
   - What you see in backend terminal
   - What you see in browser console
   - What happens when you visit http://localhost:8080/api/donors/health

---

## 💡 Common Solutions Summary

| Problem | Solution |
|---------|----------|
| Backend not started | Run `mvn spring-boot:run` in backend folder |
| Backend stopped | Restart it with `mvn spring-boot:run` |
| Port 8080 in use | Kill process or change port |
| MongoDB error | Fix connection string in `application.properties` |
| CORS error | Backend should allow `http://localhost:3000` |
| Frontend can't connect | Make sure backend is running first |

---

**Good luck! 🚀**



