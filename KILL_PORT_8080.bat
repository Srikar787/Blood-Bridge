@echo off
echo Finding process using port 8080...
netstat -ano | findstr :8080
echo.
echo Killing process...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo Killing PID: %%a
    taskkill /PID %%a /F
)
echo.
echo Done! You can now start your backend.
pause



