# 🔐 Google OAuth Setup Guide

## Error
```
Error 401: invalid_client
The OAuth client was not found.
```

This means Google OAuth credentials are not configured or are incorrect.

## Step-by-Step Setup

### 1. Go to Google Cloud Console
Visit: https://console.cloud.google.com/

### 2. Create or Select a Project
- Click on the project dropdown at the top
- Click "New Project" or select an existing one
- Give it a name (e.g., "BloodBridge")
- Click "Create"

### 3. Enable Google+ API
- Go to **APIs & Services** → **Library**
- Search for "Google+ API" or "People API"
- Click on it and click **Enable**

### 4. Create OAuth 2.0 Credentials
- Go to **APIs & Services** → **Credentials**
- Click **+ CREATE CREDENTIALS** → **OAuth client ID**
- If prompted, configure the OAuth consent screen first:
  - Choose **External** (unless you have Google Workspace)
  - Fill in required fields:
    - App name: `BloodBridge`
    - User support email: Your email
    - Developer contact: Your email
  - Click **Save and Continue**
  - Add scopes: `email`, `profile`
  - Click **Save and Continue**
  - Add test users (your email) if needed
  - Click **Save and Continue**

### 5. Create OAuth Client ID
- Application type: **Web application**
- Name: `BloodBridge Web Client`
- Authorized JavaScript origins:
  - `http://localhost:3000`
  - `http://localhost:8081`
- Authorized redirect URIs:
  - `http://localhost:8081/login/oauth2/code/google`
- Click **Create**

### 6. Copy Credentials
- You'll see a popup with:
  - **Client ID** (looks like: `123456789-abcdefghijklmnop.apps.googleusercontent.com`)
  - **Client Secret** (looks like: `GOCSPX-abcdefghijklmnopqrstuvwxyz`)
- **Copy both values**

### 7. Update application.properties
Open `backend/src/main/resources/application.properties` and replace:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID_HERE
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET_HERE
```

**Example:**
```properties
spring.security.oauth2.client.registration.google.client-id=123456789-abcdefghijklmnop.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=GOCSPX-abcdefghijklmnopqrstuvwxyz
```

### 8. Restart Backend
After updating the credentials:
```bash
cd backend
mvnd spring-boot:run
```

## Quick Checklist

- [ ] Google Cloud project created
- [ ] Google+ API enabled
- [ ] OAuth consent screen configured
- [ ] OAuth client ID created (Web application)
- [ ] Authorized redirect URI added: `http://localhost:8081/login/oauth2/code/google`
- [ ] Client ID and Secret copied
- [ ] Credentials added to `application.properties`
- [ ] Backend restarted

## Common Issues

### Issue 1: "Redirect URI mismatch"
**Solution**: Make sure the redirect URI in Google Console exactly matches:
```
http://localhost:8081/login/oauth2/code/google
```

### Issue 2: "OAuth consent screen not configured"
**Solution**: Complete the OAuth consent screen setup before creating credentials

### Issue 3: "Invalid client"
**Solution**: 
- Double-check Client ID and Secret are correct
- Make sure there are no extra spaces
- Restart backend after updating

### Issue 4: "Access blocked"
**Solution**: 
- Add your email as a test user in OAuth consent screen
- Or publish the app (requires verification for production)

## Testing

1. Go to `http://localhost:3000/login`
2. Click "Continue with Google"
3. Should redirect to Google login
4. After login, should redirect back to your app

## Security Note

⚠️ **Never commit credentials to Git!**
- Keep `application.properties` out of version control
- Use environment variables for production
- Consider using `.env` file (not committed)

## Alternative: Disable Google OAuth (For Testing)

If you don't want to set up Google OAuth right now, you can:
1. Use regular email/password signup/login
2. Comment out OAuth2 configuration in `SecurityConfig.java`
3. Remove Google OAuth button from frontend

But the setup above is recommended for full functionality!

