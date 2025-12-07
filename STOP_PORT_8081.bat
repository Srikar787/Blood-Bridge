@echo off
echo ========================================
echo Stopping Process on Port 8081
echo ========================================
echo.

echo Finding processes using port 8081...
echo.

REM Find and kill processes on port 8081
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081 ^| findstr LISTENING') do (
    echo Found process with PID: %%a
    echo Killing process...
    taskkill /PID %%a /F >nul 2>&1
    if %errorlevel% equ 0 (
        echo [OK] Process %%a killed successfully
    ) else (
        echo [ERROR] Could not kill process %%a (may need admin rights)
    )
    echo.
)

echo ========================================
echo Checking if port 8081 is now free...
echo ========================================
echo.

netstat -ano | findstr :8081
if %errorlevel% neq 0 (
    echo [OK] Port 8081 is now free!
    echo You can now run: docker-compose up -d
) else (
    echo [WARNING] Port 8081 is still in use
    echo You may need to run this script as Administrator
    echo Right-click and select "Run as administrator"
)

echo.
pause

