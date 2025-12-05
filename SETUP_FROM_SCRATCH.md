# 🚀 Complete Setup Guide - From Scratch (Windows)

This guide will help you install everything needed and run the BloodBridge project on a fresh Windows machine.

**👋 Never done this before? No problem!** This guide includes every single step, even the basics.

---

## 🎯 What is This Project?

This is a **BloodBridge** application - a web app where you can:
- Add blood donors to a database
- View all registered donors
- Search for donors by blood type

It has two parts:
1. **Backend** (Java/Spring Boot) - The server that stores data
2. **Frontend** (React) - The website you see in your browser

---

## 📝 Basic Computer Skills You'll Need

- Opening a web browser
- Downloading files
- Opening Command Prompt (we'll show you how!)
- Copying and pasting text
- Opening and editing text files

That's it! We'll guide you through everything else.

## 📋 What You'll Need to Install

1. **Java 17** (for Spring Boot backend)
2. **Node.js & npm** (for React frontend)
3. **Maven** (for building Java project)
4. **MongoDB Atlas Account** (free cloud database)
5. **A code editor** (VS Code recommended)

---

## Step 1: Install Java 17

### Download Java
1. Go to: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Scroll down to "Windows" section
3. Download **"Windows x64 Installer"** (the `.exe` file)
4. Run the installer
5. **Important**: During installation, note the installation path (usually `C:\Program Files\Java\jdk-17`)

### Set Java Environment Variables
1. Press `Windows Key + R`, type `sysdm.cpl`, press Enter
2. Click **"Advanced"** tab → **"Environment Variables"**
3. Under **"System variables"**, click **"New"**
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-17` (or your installation path)
4. Find **"Path"** in System variables, click **"Edit"**
5. Click **"New"** and add: `%JAVA_HOME%\bin`
6. Click **OK** on all windows

### Verify Java Installation

**How to Open Command Prompt:**
1. Press the **Windows key** (the key with Windows logo, usually between Ctrl and Alt)
2. Type: `cmd`
3. Press **Enter**
4. A black window will open - this is Command Prompt!

**Now test Java:**
1. In the Command Prompt window, type: `java -version`
2. Press **Enter**
3. You should see something like: `java version "17.0.9"` or similar
4. Type: `javac -version`
5. Press **Enter**
6. You should see: `javac 17.0.9` or similar

✅ **If both commands show version numbers (not "command not found"), Java is installed correctly!**

**💡 Tip:** If you see "command not found" or an error, restart your computer and try again.

---

## Step 2: Install Node.js and npm

### Download Node.js
1. Go to: https://nodejs.org/
2. Download the **LTS version** (recommended, e.g., v20.x.x)
3. Run the installer (`.msi` file)
4. Click **"Next"** through the installation (keep default options)
5. Make sure **"Add to PATH"** is checked during installation

### Verify Node.js Installation

1. **Close the old Command Prompt** (click the X or type `exit` and press Enter)
2. **Open a NEW Command Prompt** (Windows key → type `cmd` → Enter)
   - ⚠️ **Important:** Always open a NEW Command Prompt after installing software!
3. Type: `node -v` and press **Enter**
4. You should see: `v20.11.0` or similar (any version starting with v is good)
5. Type: `npm -v` and press **Enter**
6. You should see: `10.2.4` or similar (any version starting with 10 or 9 is good)

✅ **If both commands show version numbers, Node.js is installed correctly!**

---

## Step 3: Install Maven

### Download Maven
1. Go to: https://maven.apache.org/download.cgi
2. Under **"Files"**, find **"apache-maven-3.9.x-bin.zip"** (the zip file, NOT the source)
3. Click on it to download (file will be around 10 MB)
4. **Extract the zip file:**
   - Find the downloaded file (usually in `Downloads` folder)
   - **Right-click** on the zip file
   - Select **"Extract All..."**
   - Choose location: `C:\Program Files\Apache\`
   - Click **"Extract"**
   - If the `Apache` folder doesn't exist, create it first:
     - Go to `C:\Program Files\`
     - Right-click → New → Folder → Name it `Apache`
     - Then extract the zip there
5. After extraction, you should have: `C:\Program Files\Apache\apache-maven-3.9.6\` (version number may vary)

### Set Maven Environment Variables
1. Press `Windows Key + R`, type `sysdm.cpl`, press Enter
2. Click **"Advanced"** tab → **"Environment Variables"**
3. Under **"System variables"**, click **"New"**
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven\apache-maven-3.9.x` (your maven folder)
4. Find **"Path"** in System variables, click **"Edit"**
5. Click **"New"** and add: `%MAVEN_HOME%\bin`
6. Click **OK** on all windows

### Verify Maven Installation

1. **Close and open a NEW Command Prompt** (important after setting environment variables!)
2. Type: `mvn -version` and press **Enter**
3. You should see something like:
   ```
   Apache Maven 3.9.6
   Maven home: C:\Program Files\Apache\maven\apache-maven-3.9.6
   Java version: 17.0.9
   ```
4. If you see version info, it's working!

✅ **If you see Maven version info, Maven is installed correctly!**

**💡 If it says "mvn is not recognized":**
- Restart your computer (environment variables need a restart)
- Or close ALL Command Prompt windows and open a new one

---

## Step 4: Set Up MongoDB Atlas (Free Cloud Database)

### Create MongoDB Atlas Account
1. Go to: https://www.mongodb.com/cloud/atlas/register
2. Sign up with your email (or use Google/GitHub)
3. Verify your email if prompted

### Create a Free Cluster
1. After login, click **"Build a Database"**
2. Choose **"M0 FREE"** (Free Shared tier)
3. Select a **Cloud Provider** (AWS recommended)
4. Select a **Region** closest to you
5. Click **"Create"** (takes 3-5 minutes)

### Create Database User
1. In the setup wizard, create a database user:
   - **Username**: `bloodbridge-user` (or your choice)
   - **Password**: Click "Autogenerate Secure Password" or create your own
   - **IMPORTANT**: **Copy and save the password!** You'll need it.
2. Click **"Create Database User"**

### Configure Network Access
1. In the setup wizard, under "Where would you like to connect from?"
2. Click **"Add My Current IP Address"**
3. **OR** for development, click **"Allow Access from Anywhere"** (adds `0.0.0.0/0`)
   - ⚠️ This is less secure but easier for development
4. Click **"Finish and Close"**

### Get Your Connection String
1. Wait for your cluster to finish creating (green checkmark)
2. Click **"Connect"** button on your cluster
3. Choose **"Connect your application"**
4. Select **"Java"** and version **"5.0 or later"**
5. Copy the connection string (looks like):
   ```
   mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
   ```
6. **Replace** `<username>` with your database username
7. **Replace** `<password>` with your database password
8. **Add database name** at the end: `...mongodb.net/bloodbridge?retryWrites=true&w=majority`

**Example final connection string:**
```
mongodb+srv://bloodbridge-user:MyPassword123@cluster0.abc123.mongodb.net/bloodbridge?retryWrites=true&w=majority
mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge?retryWrites=true&w=majority
```

✅ **Save this connection string - you'll need it next!**

---

## Step 5: Configure the Project

### Update MongoDB Connection String

**How to Open and Edit the File:**

**Option 1: Using Notepad (Easiest)**
1. Open **File Explorer** (Windows key + E)
2. Navigate to: `C:\Users\srika\Downloads\z2\backend\src\main\resources\`
3. Find the file named `application.properties`
4. **Right-click** on it → **Open with** → **Notepad**
5. Find the line that says: `spring.data.mongodb.uri=mongodb+srv://username:password@...`
6. **Select the entire line** (click and drag, or triple-click)
7. **Delete it** (press Delete key)
8. **Type or paste** your MongoDB connection string:
   ```properties
   spring.data.mongodb.uri=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@YOUR_CLUSTER.mongodb.net/bloodbridge?retryWrites=true&w=majority
   ```
   - Replace `YOUR_USERNAME` with your MongoDB username
   - Replace `YOUR_PASSWORD` with your MongoDB password
   - Replace `YOUR_CLUSTER` with your cluster name (usually `cluster0.xxxxx`)
9. Press **Ctrl + S** to save
10. Close Notepad

**Option 2: Using VS Code (If you install it)**
1. Install VS Code from: https://code.visualstudio.com/
2. Open VS Code
3. File → Open Folder → Select `C:\Users\srika\Downloads\z2`
4. Navigate to `backend\src\main\resources\application.properties`
5. Edit and save (Ctrl + S)

**Example of what the line should look like:**
```properties
spring.data.mongodb.uri=mongodb+srv://bloodbridge-user:MyPassword123@cluster0.abc123.mongodb.net/bloodbridge?retryWrites=true&w=majority
```

✅ **Make sure to save the file after editing!**

---

## Step 6: Install Frontend Dependencies

**What is "cd" command?**
- `cd` means "change directory" (go to a folder)
- You need to navigate to the frontend folder first

**Step-by-step:**
1. Open **Command Prompt** (Windows key → type `cmd` → Enter)
2. Type this command and press **Enter**:
   ```
   cd C:\Users\srika\Downloads\z2\frontend
   ```
   - **💡 Tip:** You can copy this line, then in Command Prompt, right-click to paste
3. Press **Enter**
4. You should see the prompt change to show the frontend folder path
5. Now type this command and press **Enter**:
   ```
   npm install
   ```
6. **Wait patiently!** This will download many files (may take 2-5 minutes)
   - You'll see lots of text scrolling
   - Don't close the window!
7. When it's done, you'll see something like:
   ```
   added 1234 packages, and audited 1235 packages in 2m
   ```

✅ **When you see "added X packages", installation is complete!**

**⚠️ If you see errors:**
- Make sure you're in the correct folder (check the path in Command Prompt)
- Make sure you have internet connection
- Try again - sometimes it needs a second attempt

---

## Step 7: Run the Application

**Important:** You need **TWO Command Prompt windows** running at the same time:
- **Window 1:** Backend (Java server)
- **Window 2:** Frontend (React website)

### Terminal 1: Start Backend (Spring Boot)

1. Open **Command Prompt** (Windows key → `cmd` → Enter)
2. Navigate to backend folder:
   ```
   cd C:\Users\srika\Downloads\z2\backend
   ```
   Press **Enter**
3. Run Spring Boot:
   ```
   mvn spring-boot:run
   ```
   Press **Enter**
4. **Wait patiently!** This will:
   - Download dependencies (first time only, may take 2-5 minutes)
   - Compile the code
   - Start the server
5. When it's ready, you'll see:
   ```
   Started BloodBridgeApplication in 5.234 seconds
   ```
6. **⚠️ IMPORTANT:** 
   - **Keep this window open!** Don't close it!
   - The backend is now running
   - You should see it's listening on port 8080

**✅ Backend is running when you see "Started BloodBridgeApplication"**

### Terminal 2: Start Frontend (React)

1. **Open a NEW Command Prompt** (don't close the first one!)
   - Press Windows key → type `cmd` → Enter
   - Or right-click Start menu → Command Prompt
2. Navigate to frontend folder:
   ```
   cd C:\Users\srika\Downloads\z2\frontend
   ```
   Press **Enter**
3. Start React:
   ```
   npm start
   ```
   Press **Enter**
4. **Wait a moment** - React will:
   - Start the development server
   - Open your browser automatically
5. Your browser should open to: `http://localhost:3000`
   - If it doesn't open automatically, manually go to: http://localhost:3000
6. You should see the **BloodBridge homepage** with a red navigation bar

**✅ Frontend is running when you see the website in your browser!**

**💡 Remember:**
- Keep BOTH Command Prompt windows open
- Don't close them while using the app
- To stop the app, press `Ctrl + C` in each window

---

## Step 8: Test the Application

### Test Backend
1. Open a browser
2. Go to: http://localhost:8080/api/donors/health
3. You should see: `"BloodBridge API is running!"`

### Test Frontend
1. You should already see the homepage at http://localhost:3000
   - If not, open your browser and go to: http://localhost:3000
2. Look at the top navigation bar (red bar)
3. Click on **"Add Donor"** (in the navigation menu)
4. Fill in the form:
   - **Name:** Type `John Doe`
   - **Email:** Type `john@example.com`
   - **Phone:** Type `1234567890`
   - **Blood Type:** Click the dropdown, select `O+`
   - **Address:** (optional - leave blank or type something)
   - **Last Donation Date:** (optional - leave blank)
   - **Active Donor:** (checkbox - leave it checked)
5. Click the **"Add Donor"** button (red button at bottom)
6. You should either:
   - See a success message, OR
   - Be automatically redirected to the Donors page
7. Click **"Donors"** in the navigation menu
8. You should see your added donor (John Doe) in a card

✅ **If you can add and view donors, everything is working perfectly!**

**🎉 Congratulations! Your application is running!**

---

## 🐛 Troubleshooting

### "java: command not found" or "javac: command not found"
- **Solution**: Restart your computer after installing Java, then try again
- Or manually add Java to PATH (see Step 1)

### "mvn: command not found"
- **Solution**: Restart your computer after installing Maven
- Or check that MAVEN_HOME is set correctly

### "npm: command not found"
- **Solution**: Restart your computer after installing Node.js
- Or reinstall Node.js and make sure "Add to PATH" is checked

### Backend won't start - MongoDB connection error
- **Check**: Your MongoDB connection string in `application.properties`
- **Check**: Your IP address is whitelisted in MongoDB Atlas
- **Check**: Your cluster is running (not paused) in MongoDB Atlas

### Frontend shows "Failed to fetch donors"
- **Check**: Backend is running (Terminal 1 should show it's started)
- **Check**: Backend is on port 8080 (check Terminal 1 output)
- **Check**: No firewall blocking localhost:8080

### Port 8080 or 3000 already in use
- **Solution**: Close other applications using these ports
- Or change ports in `application.properties` (backend) or `package.json` (frontend)

### "npm install" fails
- **Solution**: Make sure you're in the `frontend` folder
- Try: `npm cache clean --force` then `npm install` again
- Check your internet connection

---

## 📝 Quick Reference Commands

### Start Backend
```bash
cd C:\Users\srika\Downloads\z2\backend
mvn spring-boot:run
```

### Start Frontend
```bash
cd C:\Users\srika\Downloads\z2\frontend
npm start
```

### Stop Applications
- Press `Ctrl + C` in each terminal window

---

## ✅ Checklist

Before running, make sure:
- [ ] Java 17 installed (`java -version` works)
- [ ] Node.js installed (`node -v` works)
- [ ] Maven installed (`mvn -version` works)
- [ ] MongoDB Atlas account created
- [ ] MongoDB cluster created and running
- [ ] Database user created
- [ ] IP address whitelisted in MongoDB Atlas
- [ ] Connection string updated in `application.properties`
- [ ] Frontend dependencies installed (`npm install` completed)
- [ ] Backend running in Terminal 1
- [ ] Frontend running in Terminal 2

---

## 🎉 You're All Set!

Your BloodBridge application should now be running. You can:
- Add donors through the web interface
- View all donors
- Test the API endpoints

For more information, see the main `README.md` file.

---

## 💡 Next Steps (Optional)

- Install **VS Code** for better code editing: https://code.visualstudio.com/
- Install **Git** for version control: https://git-scm.com/download/win
- Set up **n8n** for automation (see `n8n/README.md`)

