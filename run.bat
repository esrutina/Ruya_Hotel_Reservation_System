@echo off
title Ruya Hotel System Launcher
echo ===================================================
echo             Ruya Hotel Management System
echo ===================================================
echo.
echo Starting the application...
echo.

:: Run the application using the compiled classes and libraries
java -cp "out;lib/*" com.ruyahotel.Main

if %ERRORLEVEL% neq 0 (
    echo.
    echo Warning: Application exited with error code %ERRORLEVEL%.
    echo If it failed to start, make sure:
    echo 1. Java is installed (run 'java -version' in command prompt)
    echo 2. MySQL is running (via XAMPP) and the 'ruya_hotel' database is created
    echo.
)

pause
