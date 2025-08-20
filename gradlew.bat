@echo off
REM Proxy wrapper to the actual Gradle wrapper located in android_frontend\
setlocal
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%android_frontend"
call .\gradlew.bat %*
endlocal
