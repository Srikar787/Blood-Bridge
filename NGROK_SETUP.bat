@echo off
echo ========================================
echo ngrok Setup for BloodBridge
echo ========================================
echo.

REM Check if Docker is running
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not running!
    echo Please start Docker Desktop first.
    echo Then run: docker-compose up -d
    pause
    exit /b 1
)

echo Docker is running. Good!
echo.

REM Check if containers are running
docker ps | findstr bloodbridge >nul 2>&1
if %errorlevel% neq 0 (
    echo WARNING: BloodBridge containers are not running!
    echo Starting containers...
    docker-compose up -d
    timeout /t 5 /nobreak >nul
)

echo.
echo ========================================
echo ngrok Configuration
echo ========================================
echo.

REM Check if ngrok is installed
ngrok version >nul 2>&1
if %errorlevel% neq 0 (
    echo ngrok is not installed!
    echo.
    echo Please:
    echo 1. Download from: https://ngrok.com/download
    echo 2. Extract ngrok.exe
    echo 3. Add to PATH or run from ngrok folder
    echo.
    pause
    exit /b 1
)

echo ngrok is installed. Good!
echo.

REM Check if authtoken is configured
ngrok config check >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo ngrok Auth Token Setup
    echo ========================================
    echo.
    echo You need to configure ngrok authtoken.
    echo.
    echo 1. Go to: https://dashboard.ngrok.com/get-started/your-authtoken
    echo 2. Copy your authtoken
    echo.
    set /p authtoken="Enter your ngrok authtoken: "
    if not "!authtoken!"=="" (
        ngrok config add-authtoken !authtoken!
        echo.
        echo Authtoken configured!
    ) else (
        echo No authtoken provided. Exiting.
        pause
        exit /b 1
    )
)

echo.
echo ========================================
echo Starting ngrok Tunnel
echo ========================================
echo.
echo Your frontend will be available at the ngrok URL shown below.
echo.
echo Press Ctrl+C to stop ngrok.
echo.
echo ========================================
echo.

REM Start ngrok
ngrok http 3000

pause

