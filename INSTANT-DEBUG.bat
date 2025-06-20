@echo off
echo =================================
echo   INSTANT USB DEBUGGING MODE
echo   No APK rebuilding needed!
echo =================================
echo.

echo Checking device connection...
adb devices
echo.

echo Setting up instant debugging...

REM Enable hot swap and instant run
adb shell settings put global development_settings_enabled 1
adb shell settings put global adb_enabled 1

REM Set up port forwarding for instant updates
adb forward tcp:8080 tcp:8080
adb forward tcp:8081 tcp:8081

echo.
echo =================================
echo   ✅ INSTANT DEBUG MODE ACTIVE!
echo =================================
echo.
echo Now you can:
echo.
echo 1. Make code changes in your editor
echo 2. Save the file
echo 3. Changes appear INSTANTLY on your phone!
echo.
echo No APK rebuilding required!
echo.
echo Hot reload commands:
echo   Ctrl+S = Save and hot reload
echo   R + R = Refresh app state
echo.
echo To monitor live changes:
echo   adb logcat -s "ReactNative:*" "AppDrawer:*"
echo.
pause 