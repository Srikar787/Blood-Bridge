@echo off
echo ========================================
echo Docker Setup Checker
echo ========================================
echo.

echo [1] Checking Docker installation...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo    [X] Docker is not installed or not in PATH
    echo    Please install Docker Desktop
) else (
    docker --version
    echo    [OK] Docker is installed
)
echo.

echo [2] Checking if Docker is running...
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo    [X] Docker is not running
    echo    Please start Docker Desktop
) else (
    echo    [OK] Docker is running
)
echo.

echo [3] Checking current directory...
echo    Current: %CD%
echo.

echo [4] Checking for docker-compose.yml...
cd /d "%~dp0"
if exist "docker-compose.yml" (
    echo    [OK] Found docker-compose.yml
) else (
    echo    [X] docker-compose.yml NOT FOUND
    echo    Expected location: %CD%
    echo    Please make sure you're in the project root
)
echo.

echo [5] Checking for backend folder...
if exist "backend" (
    echo    [OK] Found backend folder
) else (
    echo    [X] backend folder NOT FOUND
)
echo.

echo [6] Checking for frontend folder...
if exist "frontend" (
    echo    [OK] Found frontend folder
) else (
    echo    [X] frontend folder NOT FOUND
)
echo.

echo [7] Checking docker-compose command...
docker-compose --version >nul 2>&1
if %errorlevel% equ 0 (
    docker-compose --version
    echo    [OK] docker-compose command works
    set DOCKER_COMPOSE_CMD=docker-compose
) else (
    docker compose version >nul 2>&1
    if %errorlevel% equ 0 (
        docker compose version
        echo    [OK] docker compose command works
        set DOCKER_COMPOSE_CMD=docker compose
    ) else (
        echo    [X] docker-compose command not found
    )
)
echo.

echo ========================================
echo Summary
echo ========================================
echo.
echo If all checks passed, you can run:
echo   START_DOCKER.bat
echo.
echo Or manually:
echo   docker-compose up -d
echo.

pause

