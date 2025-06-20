@echo off
echo =================================
echo App Drawer - Complete Testing
echo =================================
echo.

echo Step 1: Checking device connection...
adb devices
echo.

echo Step 2: Building debug APK...
call gradlew.bat clean assembleDebug
if %ERRORLEVEL% neq 0 (
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)
echo.

echo Step 3: Installing APK...
adb install -r "app\build\outputs\apk\debug\app-debug.apk"
if %ERRORLEVEL% neq 0 (
    echo Installation failed! Please check device connection.
    pause
    exit /b 1
)
echo.

echo Step 4: Granting permissions...
adb shell pm grant com.appdrawer.app.debug android.permission.QUERY_ALL_PACKAGES
echo Permissions granted successfully.
echo.

echo Step 5: Launching App Drawer...
adb shell am start -n com.appdrawer.app.debug/.MainActivity
echo.

echo =================================
echo ✅ App Drawer Testing Complete!
echo =================================
echo.
echo The app should now be running on your device.
echo.
echo To monitor logs, run:
echo adb logcat -s "AppDrawer:*"
echo.
echo To stop the app, run:
echo adb shell am force-stop com.appdrawer.app.debug
echo.
pause 