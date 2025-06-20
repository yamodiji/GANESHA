@echo off
echo =================================
echo   Android App Drawer Builder
echo      USB Debugging Ready
echo =================================
echo.

REM Check if ADB is available
echo Checking ADB connection...
adb version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: ADB not found! Please install Android SDK Platform Tools
    echo Download from: https://developer.android.com/studio/releases/platform-tools
    pause
    exit /b 1
)

REM Check device connection
echo Checking connected devices...
adb devices
echo.

REM Attempt multiple build methods
echo Attempting to build project...

REM Method 1: Try system Gradle if available
where gradle >nul 2>&1
if %ERRORLEVEL% == 0 (
    echo Using system Gradle...
    gradle clean assembleDebug
    goto :install
)

REM Method 2: Try Gradle from cache
set GRADLE_BIN=C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\gradle-8.4\bin\gradle.bat
if exist "%GRADLE_BIN%" (
    echo Using cached Gradle installation...
    "%GRADLE_BIN%" clean assembleDebug
    goto :install
)

REM Method 3: Try the other cache location
set GRADLE_BIN2=C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\1w5dpkrfk8irigvoxmyhowfim\gradle-8.4\bin\gradle.bat
if exist "%GRADLE_BIN2%" (
    echo Using cached Gradle installation (method 2)...
    "%GRADLE_BIN2%" clean assembleDebug
    goto :install
)

REM Method 4: Manual Java compilation (last resort)
echo Attempting direct Java compilation...
if exist "app\src\main\java" (
    echo This would require setting up classpath manually...
    echo Please install Gradle or use Android Studio for building.
    pause
    exit /b 1
)

:install
echo.
echo Checking if APK was built...
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ERROR: APK not found! Build may have failed.
    pause
    exit /b 1
)

echo ✅ APK built successfully!
echo.

echo Installing on device...
adb install -r "app\build\outputs\apk\debug\app-debug.apk"
if %ERRORLEVEL% neq 0 (
    echo Installation failed! Check device connection and USB debugging.
    pause
    exit /b 1
)

echo.
echo Granting required permissions...
adb shell pm grant com.appdrawer.app android.permission.QUERY_ALL_PACKAGES 2>nul

echo.
echo Launching App Drawer...
adb shell am start -n com.appdrawer.app/.MainActivity

echo.
echo =================================
echo  ✅ SUCCESS! App is now running!
echo =================================
echo.
echo Your App Drawer should now be visible on your device!
echo.
echo Useful commands:
echo   adb logcat -s "AppDrawer"     - View app logs
echo   adb shell am force-stop com.appdrawer.app  - Stop the app
echo.
pause 