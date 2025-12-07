@echo off
echo ========================================
echo Fix Port 8081 Conflict
echo ========================================
echo.

echo Checking what's using port 8081...
echo.

REM Find process using port 8081
netstat -ano | findstr :8081

echo.
echo ========================================
echo Solutions:
echo ========================================
echo.
echo Option 1: Stop the process using port 8081
echo   - Look at the PID (last column) above
echo   - Run: taskkill /PID <PID> /F
echo.
echo Option 2: Stop Docker containers
echo   - Run: docker-compose down
echo.
echo Option 3: Change port in docker-compose.yml
echo   - Edit docker-compose.yml
echo   - Change "8081:8081" to "8082:8081"
echo.
echo ========================================
echo.

REM Try to find and kill processes on port 8081
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do (
    echo Found process with PID: %%a
    echo.
    set /p killit="Kill this process? (Y/N): "
    if /i "!killit!"=="Y" (
        taskkill /PID %%a /F
        echo Process killed.
    )
)

pause

