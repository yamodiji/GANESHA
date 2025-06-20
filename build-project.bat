@echo off
echo Building Android App Drawer Project...
echo.

REM Try to use gradle from your PATH first
where gradle >nul 2>&1
if %ERRORLEVEL% == 0 (
    echo Using system Gradle...
    gradle clean build
    goto :end
)

REM If no system gradle, try to use your existing Gradle installation directly
set GRADLE_HOME=C:\Users\Ganesha\.gradle\wrapper\dists\gradle-8.4-bin\1w5dpkrfk8irigvoxmyhowfim\gradle-8.4
if exist "%GRADLE_HOME%\bin\gradle.bat" (
    echo Using Gradle from cache...
    "%GRADLE_HOME%\bin\gradle.bat" clean build
    goto :end
)

echo Error: Could not find Gradle installation
echo Please ensure Gradle is installed or available in your PATH

:end
pause 