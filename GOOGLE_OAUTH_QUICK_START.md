# 🔐 Google OAuth - Quick Start Guide

## ✅ Your Current Status

I can see you already have Google OAuth credentials configured! However, let me help you verify they're set up correctly.

## 📋 Quick Setup Steps

### Step 1: Get Google OAuth Credentials

1. **Go to Google Cloud Console**
   - Visit: https://console.cloud.google.com/
   - Sign in with your Google account

2. **Create or Select Project**
   - Click project dropdown (top bar)
   - Click "New Project" or select existing
   - Name: `BloodBridge`
   - Click "Create"

3. **Configure OAuth Consent Screen** (First Time Only)
   - Go to **APIs & Services** → **OAuth consent screen**
   - Choose **External** (unless you have Google Workspace)
   - Fill in:
     - App name: `BloodBridge`
     - User support email: Your email
     - Developer contact: Your email
   - Click **Save and Continue**
   - Add scopes: `email`, `profile`
   - Click **Save and Continue**
   - Add test users: Add your email address
   - Click **Save and Continue**

4. **Create OAuth 2.0 Client ID**
   - Go to **APIs & Services** → **Credentials**
   - Click **+ CREATE CREDENTIALS** → **OAuth client ID**
   - Application type: **Web application**
   - Name: `BloodBridge Web Client`
   - **Authorized JavaScript origins:**
     ```
     http://localhost:3000
     http://localhost:8081
     ```
   - **Authorized redirect URIs:**
     ```
     http://localhost:8081/login/oauth2/code/google
     ```
   - Click **Create**

5. **Copy Your Credentials**
   - You'll see a popup with:
     - **Client ID**: `212722254826-xxxxx.apps.googleusercontent.com`
     - **Client Secret**: `GOCSPX-xxxxx`
   - **Copy both!**

### Step 2: Update application.properties

Open `backend/src/main/resources/application.properties` and update:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID_HERE
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET_HERE
```

**Important:**
- Remove `http://` prefix if present (Client ID should NOT have http://)
- No spaces before or after the values
- Make sure there are no quotes

**Example (Correct):**
```properties
spring.security.oauth2.client.registration.google.client-id=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
```

**Example (Wrong):**
```properties
# ❌ DON'T DO THIS:
spring.security.oauth2.client.registration.google.client-id=http://212722254826-...
# ❌ DON'T DO THIS:
spring.security.oauth2.client.registration.google.client-id="212722254826-..."
```

### Step 3: Restart Backend

After updating credentials:
```bash
cd backend
mvn spring-boot:run
# or
./mvnw spring-boot:run
```

### Step 4: Test

1. Start your frontend:
   ```bash
   cd frontend
   npm start
   ```

2. Go to: `http://localhost:3000/login`

3. Click **"Continue with Google"** button

4. You should be redirected to Google login page

5. After logging in, you'll be redirected back to your app

## ✅ Verification Checklist

- [ ] Google Cloud project created
- [ ] OAuth consent screen configured
- [ ] OAuth client ID created (Web application type)
- [ ] Authorized redirect URI added: `http://localhost:8081/login/oauth2/code/google`
- [ ] Client ID copied (without http:// prefix)
- [ ] Client Secret copied
- [ ] Credentials added to `application.properties`
- [ ] No duplicate entries in `application.properties`
- [ ] Backend restarted

## 🐛 Common Issues & Fixes

### Issue 1: "Redirect URI mismatch"
**Error:** `redirect_uri_mismatch`

**Fix:**
- Go to Google Cloud Console → Credentials
- Edit your OAuth 2.0 Client ID
- Make sure redirect URI is exactly: `http://localhost:8081/login/oauth2/code/google`
- No trailing slash, no https

### Issue 2: "Invalid client"
**Error:** `invalid_client` or `401 unauthorized`

**Fix:**
- Double-check Client ID and Secret in `application.properties`
- Make sure there are no extra spaces
- Remove `http://` prefix if present
- Restart backend after changes

### Issue 3: "Access blocked"
**Error:** `access_denied` or "This app isn't verified"

**Fix:**
- Add your email as a test user in OAuth consent screen
- Go to: APIs & Services → OAuth consent screen → Test users
- Add your email address
- Save and try again

### Issue 4: "OAuth consent screen not configured"
**Error:** When trying to create OAuth client ID

**Fix:**
- Complete OAuth consent screen setup first (Step 3 above)
- Then create OAuth client ID

## 🔍 Debugging

### Check Backend Logs

Look for these messages:
```
✅ "OAuth2 authentication successful"
❌ "OAuth2 authentication failed"
```

### Test OAuth Endpoint

Try accessing directly:
```
http://localhost:8081/oauth2/authorization/google
```

Should redirect to Google login.

### Check Frontend

Make sure your frontend has a "Continue with Google" button that links to:
```
http://localhost:8081/oauth2/authorization/google
```

## 📝 Your Current Configuration

I've already fixed your `application.properties` file. Your credentials are now properly configured:

```properties
spring.security.oauth2.client.registration.google.client-id=212722254826-ascbmvrdommhqt15m59icbr3uq4b5gop.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
```

**Just restart your backend and test!**

## 🚀 Next Steps

1. ✅ Verify credentials in Google Cloud Console
2. ✅ Make sure redirect URI matches exactly
3. ✅ Restart backend
4. ✅ Test login with Google button
5. ✅ Enjoy OAuth! 🎉

## 📚 More Help

- Full guide: See `GOOGLE_OAUTH_SETUP.md`
- Backend code: `backend/src/main/java/com/bloodbridge/config/SecurityConfig.java`
- Frontend: Check your Login component for Google button

## 🔒 Security Note

⚠️ **Never commit credentials to Git!**
- Your `application.properties` should be in `.gitignore`
- Use environment variables for production
- Keep secrets secure!

---

**Need help?** Check the backend logs for specific error messages!

