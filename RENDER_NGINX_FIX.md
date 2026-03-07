# 🔧 Fix: Nginx "host not found in upstream 'backend'" Error

## Problem
The frontend service is failing with:
```
host not found in upstream "backend" in /etc/nginx/conf.d/default.conf:1
```

## Root Cause
The custom entrypoint script was conflicting with nginx's built-in template processing system.

## Solution Applied
✅ Removed custom entrypoint script  
✅ Let nginx's built-in entrypoint handle template substitution automatically  
✅ Template uses `${BACKEND_URL}` syntax which nginx processes automatically

## What Changed

### Before (Broken):
- Custom `docker-entrypoint.sh` script
- Overrode nginx's default entrypoint
- Caused conflicts with nginx's template system

### After (Fixed):
- No custom entrypoint
- Nginx's built-in entrypoint processes templates automatically
- Templates in `/etc/nginx/templates/` are processed and output to `/etc/nginx/conf.d/`

## Required Environment Variable

Make sure your frontend service in Render has:

```
BACKEND_URL=https://YOUR-BACKEND-URL.onrender.com
```

**⚠️ IMPORTANT:** 
- Must include `https://`
- Must be the full backend URL (e.g., `https://bloodbridge-backend-xxxx.onrender.com`)
- No trailing slash

## Next Steps

1. **Commit and push the updated Dockerfile:**
   ```bash
   git add frontend/Dockerfile frontend/nginx.conf.template
   git commit -m "Fix nginx template processing for Render"
   git push
   ```

2. **Redeploy frontend service in Render:**
   - Render will automatically detect the new commit
   - Or manually trigger a deploy

3. **Verify BACKEND_URL is set:**
   - Go to frontend service → Environment tab
   - Make sure `BACKEND_URL` is set to your backend's Render URL

## How It Works Now

1. Nginx's entrypoint script (`/docker-entrypoint.d/20-envsubst-on-templates.sh`) runs automatically
2. It finds templates in `/etc/nginx/templates/`
3. It substitutes `${BACKEND_URL}` with the environment variable value
4. Outputs the final config to `/etc/nginx/conf.d/default.conf`
5. Nginx starts with the correct backend URL

## Testing

After redeploy, check the logs:
- Should NOT see "host not found in upstream 'backend'"
- Should see nginx starting successfully
- Frontend should be able to proxy API calls to backend

---

**The fix is in the code - just commit, push, and redeploy!**



