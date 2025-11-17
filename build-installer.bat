@echo off
REM ============================================
REM GPA Calculator - Self-Contained Package Builder
REM Creates a Windows installer with bundled Java runtime
REM ============================================

echo Building self-contained GPA Calculator package...
echo.

REM Check if running from project root
if not exist "pom.xml" (
    echo ERROR: This script must be run from the project root directory.
    pause
    exit /b 1
)

REM Step 1: Build the project
echo [1/4] Building project with Maven...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven build failed.
    pause
    exit /b 1
)

REM Step 2: Check for jpackage (available in JDK 14+)
where jpackage >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: jpackage not found. Please ensure you're using JDK 14 or higher.
    pause
    exit /b 1
)

REM Step 3: Create runtime image with jlink
echo.
echo [2/4] Creating custom Java runtime with jlink...
if exist "target\runtime" rmdir /s /q "target\runtime"

jlink --add-modules java.base,java.desktop,java.logging,java.xml,jdk.unsupported,javafx.controls,javafx.fxml,javafx.graphics,javafx.base --output target\runtime --strip-debug --no-header-files --no-man-pages --compress=2

if %ERRORLEVEL% NEQ 0 (
    echo WARNING: jlink failed. Will use full runtime instead.
)

REM Step 4: Create installer package
echo.
echo [3/4] Creating Windows installer with jpackage...
if exist "release" rmdir /s /q "release"

jpackage --name "GPA Calculator" --app-version 1.0 --vendor "Kaif" --description "Student GPA Calculator Application" --input target --main-jar gpa-calculator-1.0-SNAPSHOT-fat.jar --main-class com.kaif.gpacalculator.GpaCalculatorApp --type exe --dest release --win-dir-chooser --win-menu --win-shortcut

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: jpackage failed. See error messages above.
    pause
    exit /b 1
)

echo.
echo [4/4] Package created successfully!
echo.
echo Installer location: release\GPA Calculator-1.0.exe
echo.
echo This installer includes:
echo   - GPA Calculator application
echo   - Bundled Java runtime (no Java installation required)
echo   - Desktop shortcut
echo   - Start menu entry
echo.
echo Users can run the installer on any Windows device without installing Java.
echo.
pause
