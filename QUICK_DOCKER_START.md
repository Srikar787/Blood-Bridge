# 🚀 Quick Docker Start Guide

## One-Command Setup

```bash
docker-compose up -d
```

That's it! Your application is now running.

## Access Your App

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8081
- **Health Check:** http://localhost:8081/api/donors/health

## Common Commands

```bash
# Start
docker-compose up -d

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Restart
docker-compose restart

# Rebuild after code changes
docker-compose up -d --build
```

## Troubleshooting

**Port already in use?**
```bash
# Change ports in docker-compose.yml or stop conflicting services
```

**Container won't start?**
```bash
# Check logs
docker-compose logs backend
docker-compose logs frontend
```

**Need to update code?**
```bash
# Rebuild and restart
docker-compose up -d --build
```

## Full Guide

See `DOCKER_SETUP.md` for complete documentation.

---

**That's it! Your app is containerized! 🐳**

