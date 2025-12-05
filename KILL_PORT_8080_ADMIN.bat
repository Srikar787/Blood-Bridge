@echo off
echo ========================================
echo Kill Port 8080 Process (Admin Required)
echo ========================================
echo.
echo Finding process using port 8080...
netstat -ano | findstr :8080
echo.
echo Attempting to kill process...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo Trying to kill PID: %%a
    taskkill /PID %%a /F
    if errorlevel 1 (
        echo ERROR: Could not kill PID %%a - Access Denied
        echo.
        echo SOLUTION: Right-click this file and select "Run as administrator"
    ) else (
        echo SUCCESS: PID %%a terminated
    )
)
echo.
echo Done!
pause



