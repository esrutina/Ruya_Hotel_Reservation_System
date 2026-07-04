@echo off
title Ruya Hotel System Compiler and Launcher
echo ===================================================
echo        Ruya Hotel System: Compile and Run
echo ===================================================
echo.
echo Step 1: Cleaning and creating output directory...
if not exist "out" mkdir "out"

echo.
echo Step 2: Finding Java source files...
:: Find all Java files under src/ and write to a temporary file
dir /s /b src\*.java > sources.txt

echo.
echo Step 3: Compiling source files...
javac -cp "lib/*" -d out -sourcepath src @sources.txt

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Compilation failed!
    del sources.txt
    pause
    exit /b %ERRORLEVEL%
)

del sources.txt
echo Compilation successful!
echo.
echo Step 4: Starting the application...
echo.

java -cp "out;lib/*" com.ruyahotel.Main

if %ERRORLEVEL% neq 0 (
    echo.
    echo Warning: Application exited with error code %ERRORLEVEL%.
    echo Please make sure your database is running and credentials are correct.
)

pause
