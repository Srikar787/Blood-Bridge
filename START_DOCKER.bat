@echo off
echo ========================================
echo BloodBridge Docker Startup Script
echo ========================================
echo.

REM Check if Docker is running
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running!
    echo Please start Docker Desktop and wait for it to fully load.
    echo Then run this script again.
    pause
    exit /b 1
)

echo Docker is running. Good!
echo.

REM Navigate to project directory
cd /d "%~dp0"
echo Current directory: %CD%
echo.

REM Check if docker-compose.yml exists
if not exist "docker-compose.yml" (
    echo ERROR: docker-compose.yml not found!
    echo Make sure you're in the project root directory.
    echo Expected location: C:\Users\srika\Downloads\z2
    pause
    exit /b 1
)

echo Found docker-compose.yml. Good!
echo.

REM Check which docker-compose command works
docker-compose --version >nul 2>&1
if %errorlevel% equ 0 (
    set DOCKER_COMPOSE_CMD=docker-compose
    echo Using: docker-compose
) else (
    docker compose version >nul 2>&1
    if %errorlevel% equ 0 (
        set DOCKER_COMPOSE_CMD=docker compose
        echo Using: docker compose
    ) else (
        echo ERROR: docker-compose command not found!
        echo Please check your Docker installation.
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo Starting BloodBridge Application...
echo ========================================
echo.
echo This may take 5-10 minutes the first time.
echo Please wait...
echo.

REM Start docker-compose
%DOCKER_COMPOSE_CMD% up -d

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo SUCCESS! Application is starting...
    echo ========================================
    echo.
    echo Frontend: http://localhost:3000
    echo Backend:  http://localhost:8081
    echo.
    echo To view logs: %DOCKER_COMPOSE_CMD% logs -f
    echo To stop:      %DOCKER_COMPOSE_CMD% down
    echo.
) else (
    echo.
    echo ========================================
    echo ERROR: Failed to start containers
    echo ========================================
    echo.
    echo Check the error messages above.
    echo.
)

pause

