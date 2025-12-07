# 🐳 Docker Containerization Guide

## Overview

This guide will help you containerize the entire BloodBridge application using Docker (completely FREE!). Once containerized, the application will work on any system with Docker installed.

## Prerequisites

### Install Docker (FREE)

**Windows:**
1. Download Docker Desktop: https://www.docker.com/products/docker-desktop/
2. Install and restart your computer
3. Start Docker Desktop

**Mac:**
1. Download Docker Desktop: https://www.docker.com/products/docker-desktop/
2. Install and start Docker Desktop

**Linux:**
```bash
# Ubuntu/Debian
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Restart your terminal or log out and back in
```

**Verify Installation:**
```bash
docker --version
docker-compose --version
```

## Quick Start

### Step 1: Configure Environment Variables (Optional)

Create a `.env` file in the root directory (copy from `.env.example`):

```bash
cp .env.example .env
```

Edit `.env` with your actual values:
- MongoDB URI (if different)
- Google OAuth credentials
- Email/SMTP settings
- JWT secret

**Note:** If you don't create `.env`, the default values from `docker-compose.yml` will be used.

### Step 2: Build and Start Containers

From the root directory of your project:

```bash
# Build and start all services
docker-compose up -d

# Or build and start with logs visible
docker-compose up
```

This will:
- Build the backend Docker image
- Build the frontend Docker image
- Start both containers
- Set up networking between them

### Step 3: Access the Application

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8081
- **Health Check:** http://localhost:8081/api/donors/health

### Step 4: View Logs

```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Step 5: Stop Containers

```bash
# Stop containers
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

## Project Structure

```
bloodbridge/
├── backend/
│   ├── Dockerfile          # Backend container definition
│   ├── .dockerignore       # Files to exclude from build
│   └── src/                # Spring Boot source code
├── frontend/
│   ├── Dockerfile          # Frontend container definition
│   ├── .dockerignore       # Files to exclude from build
│   └── src/                # React source code
├── docker-compose.yml      # Orchestration file
├── .env.example            # Environment variables template
└── .dockerignore           # Root level ignore file
```

## How It Works

### Backend Container
- **Base Image:** `eclipse-temurin:21-jre-alpine` (Java 21)
- **Build:** Multi-stage build with Maven
- **Port:** 8081
- **Health Check:** `/api/donors/health`

### Frontend Container
- **Base Image:** `nginx:alpine` (lightweight web server)
- **Build:** Multi-stage build with Node.js
- **Port:** 80 (mapped to 3000 on host)
- **Proxy:** Nginx proxies `/api` requests to backend

### Networking
- Both containers are on the same Docker network
- Frontend can reach backend at `http://backend:8081`
- No need to expose backend port if only using through frontend

## Configuration

### Environment Variables

You can configure the application using environment variables in `docker-compose.yml` or `.env` file:

**MongoDB:**
```yaml
SPRING_DATA_MONGODB_URI=mongodb+srv://...
```

**Email:**
```yaml
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
```

**OAuth:**
```yaml
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_SECRET=your-client-secret
```

### Updating Configuration

1. Edit `docker-compose.yml` or `.env` file
2. Restart containers:
   ```bash
   docker-compose down
   docker-compose up -d
   ```

## Common Commands

### Build Only
```bash
# Build images without starting
docker-compose build

# Rebuild specific service
docker-compose build backend
docker-compose build frontend
```

### Start/Stop
```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Restart services
docker-compose restart
```

### View Status
```bash
# List running containers
docker-compose ps

# View logs
docker-compose logs -f

# View resource usage
docker stats
```

### Clean Up
```bash
# Remove containers and networks
docker-compose down

# Remove containers, networks, and volumes
docker-compose down -v

# Remove images
docker-compose down --rmi all
```

## Troubleshooting

### Issue 1: Port Already in Use
**Error:** `Bind for 0.0.0.0:8081 failed: port is already allocated`

**Solution:**
```bash
# Find what's using the port
# Windows:
netstat -ano | findstr :8081

# Mac/Linux:
lsof -i :8081

# Kill the process or change port in docker-compose.yml
```

### Issue 2: Container Won't Start
**Error:** Container exits immediately

**Solution:**
```bash
# Check logs
docker-compose logs backend
docker-compose logs frontend

# Check if health check is passing
docker-compose ps
```

### Issue 3: Frontend Can't Reach Backend
**Error:** Network errors in browser console

**Solution:**
1. Make sure both containers are running: `docker-compose ps`
2. Check network: `docker network ls`
3. Verify backend is accessible: `curl http://localhost:8081/api/donors/health`
4. Check nginx proxy config in frontend Dockerfile

### Issue 4: Build Fails
**Error:** Build errors during `docker-compose build`

**Solution:**
```bash
# Clean build
docker-compose build --no-cache

# Check Dockerfile syntax
docker build -t test-backend ./backend
```

### Issue 5: MongoDB Connection Issues
**Error:** Cannot connect to MongoDB

**Solution:**
1. Verify MongoDB Atlas allows connections from anywhere (0.0.0.0/0)
2. Check connection string in environment variables
3. Test connection: `docker-compose exec backend wget -O- http://localhost:8081/api/donors/health`

## Production Deployment

### For Production:

1. **Use Environment Variables:**
   - Never hardcode secrets in `docker-compose.yml`
   - Use `.env` file (add to `.gitignore`)
   - Or use Docker secrets

2. **Update CORS:**
   ```yaml
   SPRING_WEB_CORS_ALLOWED_ORIGINS=https://yourdomain.com
   ```

3. **Update OAuth Redirect URI:**
   ```yaml
   SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI=https://yourdomain.com/login/oauth2/code/google
   ```

4. **Use HTTPS:**
   - Add reverse proxy (nginx/traefik) for SSL
   - Or use Docker with SSL certificates

5. **Resource Limits:**
   ```yaml
   services:
     backend:
       deploy:
         resources:
           limits:
             cpus: '1'
             memory: 512M
   ```

## Deploy to Cloud (FREE Options)

### Option 1: Railway (Free Tier)
1. Sign up: https://railway.app/
2. Connect GitHub repo
3. Railway auto-detects Docker
4. Add environment variables
5. Deploy!

### Option 2: Render (Free Tier)
1. Sign up: https://render.com/
2. Create new Web Service
3. Connect repo
4. Use `docker-compose.yml`
5. Deploy!

### Option 3: Fly.io (Free Tier)
1. Sign up: https://fly.io/
2. Install flyctl
3. `fly launch`
4. Deploy!

## Benefits of Containerization

✅ **Works Anywhere** - Runs on Windows, Mac, Linux  
✅ **Consistent Environment** - Same setup everywhere  
✅ **Easy Deployment** - One command to deploy  
✅ **Isolated** - No conflicts with system packages  
✅ **Scalable** - Easy to scale up/down  
✅ **Free** - Docker is completely free  

## Next Steps

1. ✅ Install Docker
2. ✅ Run `docker-compose up -d`
3. ✅ Access http://localhost:3000
4. ✅ Enjoy containerized app! 🎉

## Need Help?

- Check logs: `docker-compose logs -f`
- Check status: `docker-compose ps`
- Restart: `docker-compose restart`
- Clean start: `docker-compose down && docker-compose up -d`

---

**Your application is now containerized and ready to run anywhere!** 🐳

