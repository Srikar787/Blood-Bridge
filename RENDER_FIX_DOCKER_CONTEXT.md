# 🔧 Fix Render Docker Build Error

## Error You're Seeing
```
ERROR: failed to calculate checksum of ref: "/src": not found
ERROR: failed to calculate checksum of ref: "/pom.xml": not found
```

## Problem
Render is building from the **root directory** instead of the `backend/` directory, so it can't find `pom.xml` and `src/` folder.

## Solution: Set Docker Context in Render

### Step 1: Go to Your Backend Service in Render
1. Log into Render dashboard
2. Click on your **backend service** (`bloodbridge-backend`)

### Step 2: Go to Settings
1. Click **"Settings"** tab
2. Scroll down to **"Build & Deploy"** section

### Step 3: Set Docker Context
Look for these fields:

**Dockerfile Path:**
```
backend/Dockerfile
```

**Docker Context:**
```
backend
```

**OR if Render uses "Root Directory":**
```
backend
```

### Step 4: Save and Redeploy
1. Click **"Save Changes"**
2. Render will automatically trigger a new deployment
3. Wait for build to complete

## Alternative: Use render.yaml (Recommended)

If Render supports `render.yaml` configuration file:

1. Make sure `render.yaml` is in your **root directory** of the repository
2. Render will automatically use it
3. The `render.yaml` I created has the correct context already set

**Check if render.yaml is in your GitHub repo root:**
- It should be at: `https://github.com/Srikar787/Blood-Bridge/render.yaml`

## Manual Fix in Render Dashboard

### For Backend Service:

1. **Settings** → **Build & Deploy**
2. Find **"Dockerfile Path"** → Set to: `backend/Dockerfile`
3. Find **"Docker Context"** or **"Root Directory"** → Set to: `backend`
4. **Port:** `8081`
5. Click **"Save Changes"**

### For Frontend Service:

1. **Settings** → **Build & Deploy**
2. **Dockerfile Path:** `frontend/Dockerfile`
3. **Docker Context:** `frontend`
4. **Port:** `80`
5. Click **"Save Changes"**

## Verify Your Repository Structure

Your GitHub repo should have this structure:
```
Blood-Bridge/
├── backend/
│   ├── Dockerfile          ← Backend Dockerfile
│   ├── pom.xml
│   └── src/
├── frontend/
│   ├── Dockerfile          ← Frontend Dockerfile
│   ├── package.json
│   └── src/
├── docker-compose.yml
└── render.yaml            ← Should be in root
```

## Quick Checklist

- [ ] Backend service has **Docker Context** = `backend`
- [ ] Backend service has **Dockerfile Path** = `backend/Dockerfile`
- [ ] Frontend service has **Docker Context** = `frontend`
- [ ] Frontend service has **Dockerfile Path** = `frontend/Dockerfile`
- [ ] `render.yaml` is in repository root (optional but helpful)

## After Fixing

1. Save settings in Render
2. Render will auto-redeploy
3. Check build logs - should see:
   ```
   COPY pom.xml .          ← Should work now
   COPY src ./src          ← Should work now
   ```

## Still Having Issues?

### Option 1: Check Repository Structure
Make sure your GitHub repo has the correct structure. Render clones from GitHub, so if files are missing there, the build will fail.

### Option 2: Use render.yaml
1. Make sure `render.yaml` is committed to your repo
2. Render should auto-detect it
3. Or manually import it in Render dashboard

### Option 3: Contact Render Support
If the context setting doesn't appear in Render dashboard, contact Render support - they can help configure it.

---

**The key fix: Set Docker Context to `backend` (not root directory)!**



