# 🔧 MongoDB Atlas Connection Fix

## Error
```
SSLException: (internal_error) Received fatal alert: internal_error
Timed out after 30000 ms while waiting for a server
```

## Common Causes & Solutions

### 1. **IP Address Not Whitelisted** ⚠️ MOST COMMON

**Problem**: MongoDB Atlas blocks connections from IPs not in the whitelist.

**Solution**:
1. Go to [MongoDB Atlas Console](https://cloud.mongodb.com/)
2. Navigate to **Network Access** (left sidebar)
3. Click **Add IP Address**
4. Choose one of:
   - **Add Current IP Address** (recommended for development)
   - **Allow Access from Anywhere** (0.0.0.0/0) - ⚠️ Less secure, use only for testing
5. Click **Confirm**
6. Wait 1-2 minutes for changes to propagate

### 2. **Connection String Issues**

**Check**:
- Username and password are correct
- Database name matches (`bloodbridge`)
- Cluster name is correct (`cluster0.9ynn7vx`)

**Solution**: Get fresh connection string from Atlas:
1. Go to **Database** → **Connect**
2. Choose **Connect your application**
3. Copy the connection string
4. Replace `<password>` with your actual password
5. Update `application.properties`

### 3. **SSL/TLS Configuration**

**Updated**: Added SSL parameters to connection string:
```
&ssl=true&tlsAllowInvalidHostnames=false
```

### 4. **Network/Firewall Issues**

**Check**:
- Firewall isn't blocking port 27017
- Corporate network/VPN isn't blocking MongoDB
- Antivirus isn't interfering

**Solution**: 
- Try from different network (mobile hotspot)
- Temporarily disable firewall/antivirus to test
- Check if port 27017 is accessible

### 5. **MongoDB Atlas Cluster Status**

**Check**:
- Cluster is running (not paused)
- Cluster hasn't been deleted
- Account hasn't expired

**Solution**: 
- Go to Atlas dashboard
- Check cluster status
- Ensure cluster is active

## Updated Configuration

The `application.properties` has been updated with:
```properties
spring.data.mongodb.uri=mongodb+srv://...?retryWrites=true&w=majority&ssl=true&tlsAllowInvalidHostnames=false
```

## Testing Connection

### Option 1: Test from MongoDB Compass
1. Download [MongoDB Compass](https://www.mongodb.com/products/compass)
2. Use the same connection string
3. Test connection

### Option 2: Test from Command Line
```bash
# Install MongoDB Shell (mongosh)
# Then test connection:
mongosh "mongodb+srv://srikarreddy3666_db_user:SQJpRS725mUirp4C@cluster0.9ynn7vx.mongodb.net/bloodbridge"
```

### Option 3: Check Backend Logs
After restarting backend, check for:
- ✅ `Successfully connected to server`
- ❌ `SSLException` or `Connection refused`

## Quick Fix Steps

1. **Whitelist Your IP** (Most Important!)
   - Go to MongoDB Atlas → Network Access
   - Add your current IP address
   - Wait 2 minutes

2. **Restart Backend**
   ```bash
   cd backend
   mvnd spring-boot:run
   ```

3. **Verify Connection**
   - Check backend logs for successful connection
   - Try signing up again

## Alternative: Use Local MongoDB (For Development)

If Atlas continues to have issues, you can use local MongoDB:

```properties
# Local MongoDB (requires MongoDB installed locally)
spring.data.mongodb.uri=mongodb://localhost:27017/bloodbridge
spring.data.mongodb.database=bloodbridge
```

## Still Having Issues?

1. **Check MongoDB Atlas Status**: https://status.mongodb.com/
2. **Verify Credentials**: Make sure username/password are correct
3. **Check Account Limits**: Free tier has connection limits
4. **Contact Support**: MongoDB Atlas support if cluster issues persist

## Most Likely Solution

**90% of the time, it's the IP whitelist issue.** Make sure your IP is added to MongoDB Atlas Network Access!

