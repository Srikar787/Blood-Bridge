# ⚡ Quick Start Guide

**Follow this if you want the fastest path to running the project!**

## 📋 Installation Order (Do These in Order!)

1. ✅ **Java 17** → Install → Restart computer
2. ✅ **Node.js** → Install → Restart computer  
3. ✅ **Maven** → Install → Restart computer
4. ✅ **MongoDB Atlas** → Create account → Get connection string
5. ✅ **Configure project** → Update `application.properties`
6. ✅ **Install dependencies** → Run `npm install` in frontend folder
7. ✅ **Run backend** → `mvn spring-boot:run` in backend folder
8. ✅ **Run frontend** → `npm start` in frontend folder (new terminal)

---

## 🎯 The Absolute Minimum Steps

### 1. Install Java 17
- Download: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Install the `.exe` file
- **Restart your computer**

### 2. Install Node.js
- Download: https://nodejs.org/ (get LTS version)
- Install the `.msi` file
- **Restart your computer**

### 3. Install Maven
- Download: https://maven.apache.org/download.cgi (get the `.zip` file)
- Extract to `C:\Program Files\Apache\maven\`
- Set environment variables (see SETUP_FROM_SCRATCH.md for details)
- **Restart your computer**

### 4. Set Up MongoDB Atlas
- Sign up: https://www.mongodb.com/cloud/atlas/register
- Create free cluster
- Create database user (save password!)
- Whitelist IP (allow from anywhere for dev)
- Get connection string

### 5. Configure Project
- Open: `backend\src\main\resources\application.properties`
- Replace the MongoDB connection string with yours

### 6. Install Frontend Dependencies
```bash
cd C:\Users\srika\Downloads\z2\frontend
npm install
```

### 7. Run Backend (Terminal 1)
```bash
cd C:\Users\srika\Downloads\z2\backend
mvn spring-boot:run
```
**Wait for:** `Started BloodBridgeApplication`

### 8. Run Frontend (Terminal 2 - NEW WINDOW)
```bash
cd C:\Users\srika\Downloads\z2\frontend
npm start
```
**Browser opens automatically to:** http://localhost:3000

---

## ✅ Verify Everything Works

1. Backend test: http://localhost:8080/api/donors/health
   - Should show: `"BloodBridge API is running!"`

2. Frontend test: http://localhost:3000
   - Should show BloodBridge homepage

3. Add a test donor:
   - Click "Add Donor"
   - Fill form and submit
   - Check "Donors" page to see it

---

## 🆘 Need More Help?

See **[SETUP_FROM_SCRATCH.md](SETUP_FROM_SCRATCH.md)** for detailed step-by-step instructions with screenshots and troubleshooting!

---

## 📝 Important Notes

- **Always restart computer** after installing Java, Node.js, or Maven
- **Keep both terminals open** while running the app
- **MongoDB connection string** must be correct in `application.properties`
- **Two terminals needed** - one for backend, one for frontend



