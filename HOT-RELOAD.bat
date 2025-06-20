@echo off
echo =================================
echo   ANDROID HOT RELOAD DEBUGGING
echo =================================
echo.

echo Setting up hot reload for Android development...
echo.

REM Enable instant run features
adb shell settings put global development_settings_enabled 1

REM Check if app is installed
adb shell pm list packages | findstr "com.appdrawer.app" >nul
if %ERRORLEVEL% neq 0 (
    echo App not installed yet. Installing from build...
    if exist "app\build\outputs\apk\debug\app-debug.apk" (
        adb install "app\build\outputs\apk\debug\app-debug.apk"
    ) else (
        echo Please build APK first or use GitHub Actions
        pause
        exit /b 1
    )
)

echo.
echo =================================
echo   DEVELOPMENT WORKFLOW
echo =================================
echo.
echo For INSTANT updates without rebuilding:
echo.
echo 1. COMPOSE HOT RELOAD:
echo    - Make UI changes in .kt files
echo    - Press Ctrl+Shift+F10 in Android Studio
echo    - OR use ./gradlew assembleDebug --continuous
echo.
echo 2. LIVE CODE CHANGES:
echo    - Modify ViewModel logic
echo    - Update SearchBar.kt, AppGrid.kt etc.
echo    - Deploy with: adb install -r [new-apk]
echo.
echo 3. INSTANT LOGS:
echo    adb logcat -s "AppDrawer:*" -v time
echo.
echo 4. APP RESTART (instant):
echo    adb shell am force-stop com.appdrawer.app
echo    adb shell am start -n com.appdrawer.app/.MainActivity
echo.
echo ✅ Ready for rapid development!
echo.
pause 