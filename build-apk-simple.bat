@echo off
echo =================================
echo   Building Android APK
echo =================================
echo.

echo Method 1: Trying to fix gradlew wrapper...
copy "C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\1w5dpkrfk8irigvoxmyhowfim\gradle-8.4\lib\plugins\gradle-wrapper-8.4.jar" "gradle\wrapper\gradle-wrapper.jar" >nul 2>&1

echo Testing gradlew...
call gradlew.bat clean >nul 2>&1
if %ERRORLEVEL% == 0 (
    echo ✅ Gradlew working! Building APK...
    call gradlew.bat assembleDebug
    goto :check_apk
)

echo.
echo Method 2: Trying direct Java execution...
if exist "C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\1w5dpkrfk8irigvoxmyhowfim\gradle-8.4" (
    echo Using Gradle 8.4 directly...
    set GRADLE_HOME=C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\1w5dpkrfk8irigvoxmyhowfim\gradle-8.4
    set JAVA_HOME=C:\Program Files\Java\jdk-17.0.12
    "%GRADLE_HOME%\bin\gradle" clean assembleDebug
    goto :check_apk
)

echo.
echo Method 3: Manual instructions...
echo.
echo ❌ Automated build failed. Please:
echo.
echo 1. Open Android Studio
echo 2. Open this project folder
echo 3. Let it sync and download dependencies
echo 4. Build → Build APKs
echo 5. The APK will be in: app\build\outputs\apk\debug\
echo.
echo OR
echo.
echo Push to GitHub and let Actions build it automatically.
echo.
pause
exit /b 1

:check_apk
echo.
echo Checking for built APK...
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo.
    echo =================================
    echo   ✅ APK BUILD SUCCESSFUL!
    echo =================================
    echo.
    echo APK Location: app\build\outputs\apk\debug\app-debug.apk
    echo APK Size: 
    dir "app\build\outputs\apk\debug\app-debug.apk" | findstr app-debug.apk
    echo.
    echo Ready for USB debugging!
    echo Run: test-complete.bat
    echo.
) else (
    echo ❌ APK not found. Build may have failed.
    echo Check the error messages above.
)

pause 