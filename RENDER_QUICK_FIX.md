# 🚀 Render Quick Fix - Your Specific URLs

## ✅ Your Current Setup

- **Frontend URL**: `https://blood-bridge-2-omij.onrender.com`
- **Backend URL**: `https://blood-bridge-1-5g02.onrender.com`

## ✅ Frontend Environment Variables (Already Set!)

Your frontend service already has:
- ✅ `BACKEND_URL` = `https://blood-bridge-1-5g02.onrender.com`
- ✅ `REACT_APP_API_URL` = `https://blood-bridge-1-5g02.onrender.com`

**These are correct!** ✅

## ⚠️ Backend Environment Variables (Need to Set!)

Go to your **Backend Service** (`blood-bridge-1-5g02`) in Render:

1. Click on your backend service
2. Go to **Environment** tab
3. Add/Update these environment variables:

### Required Variables:

```bash
APP_FRONTEND_URL=https://blood-bridge-2-omij.onrender.com

SPRING_WEB_CORS_ALLOWED_ORIGINS=https://blood-bridge-2-omij.onrender.com

SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google
```

## 🔐 Update Google OAuth Console

1. Go to: https://console.cloud.google.com/
2. Navigate to: **APIs & Services** → **Credentials**
3. Click on your OAuth 2.0 Client ID
4. Update **Authorized JavaScript origins**:
   ```
   https://blood-bridge-2-omij.onrender.com
   https://blood-bridge-1-5g02.onrender.com
   http://localhost:3000
   http://localhost:8081
   ```
5. Update **Authorized redirect URIs**:
   ```
   https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google
   http://localhost:8081/login/oauth2/code/google
   ```
6. Click **Save**

## 🔄 After Setting Environment Variables

1. **Backend will auto-redeploy** when you save environment variables
2. **Wait for deployment to complete** (check the "Events" tab)
3. **Test your application**:
   - Go to: `https://blood-bridge-2-omij.onrender.com/login`
   - Try regular login
   - Try Google OAuth login

## ✅ Verification Checklist

- [ ] Backend `APP_FRONTEND_URL` = `https://blood-bridge-2-omij.onrender.com`
- [ ] Backend `SPRING_WEB_CORS_ALLOWED_ORIGINS` = `https://blood-bridge-2-omij.onrender.com`
- [ ] Backend `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI` = `https://blood-bridge-1-5g02.onrender.com/login/oauth2/code/google`
- [ ] Frontend `BACKEND_URL` = `https://blood-bridge-1-5g02.onrender.com` ✅ (already set)
- [ ] Frontend `REACT_APP_API_URL` = `https://blood-bridge-1-5g02.onrender.com` ✅ (already set)
- [ ] Google OAuth Console updated with both Render URLs
- [ ] Both services redeployed
- [ ] Tested login
- [ ] Tested Google OAuth

## 🐛 If Login Still Fails

1. **Check backend logs** in Render dashboard
2. **Verify backend is running**: Visit `https://blood-bridge-1-5g02.onrender.com/api/donors/health`
3. **Check browser console** for CORS errors
4. **Verify environment variables** are saved (no typos, no extra spaces)

## 📝 Summary

Your frontend is correctly configured! You just need to:
1. Set 3 environment variables in your **backend service**
2. Update Google OAuth Console with your Render URLs
3. Wait for redeployment
4. Test!
