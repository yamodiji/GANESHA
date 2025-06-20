@echo off
echo =================================
echo   ADB Setup for USB Debugging
echo =================================
echo.

echo Downloading Android Platform Tools...
echo This includes ADB for USB debugging.
echo.

REM Create temp directory
if not exist "temp" mkdir temp

REM Download platform tools
echo Downloading from Google...
powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/platform-tools-latest-windows.zip' -OutFile 'temp\platform-tools.zip'"

if not exist "temp\platform-tools.zip" (
    echo Download failed! Please download manually from:
    echo https://dl.google.com/android/repository/platform-tools-latest-windows.zip
    pause
    exit /b 1
)

echo.
echo Extracting platform tools...
powershell -Command "Expand-Archive 'temp\platform-tools.zip' -DestinationPath 'temp' -Force"

echo.
echo Moving ADB to project directory...
copy "temp\platform-tools\adb.exe" "."
copy "temp\platform-tools\AdbWinApi.dll" "."
copy "temp\platform-tools\AdbWinUsbApi.dll" "."

echo.
echo Cleaning up...
rmdir /s /q "temp"

echo.
echo =================================
echo   ✅ ADB Setup Complete!
echo =================================
echo.
echo ADB is now available in your project directory.
echo.
echo Next steps:
echo 1. Enable USB Debugging on your phone
echo 2. Connect your phone via USB
echo 3. Run: build-and-run.bat
echo.
pause 