@echo off
REM ============================================
REM GPA Calculator - Windows Launcher
REM ============================================

echo Starting GPA Calculator...
echo.

REM Check if Maven is available (best option for development)
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven detected. Running with Maven...
    call mvn javafx:run
    goto :end
)

REM Check if Java is available
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH.
    echo.
    echo Please install Java 21 or higher from:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Check if built artifacts exist
if not exist "target\lib" (
    echo ERROR: Project not built. Please run:
    echo mvn clean package
    echo.
    pause
    exit /b 1
)

REM Run using the module path with JavaFX libs
echo Running GPA Calculator with Java...
java --module-path "target\lib" --add-modules javafx.controls,javafx.fxml -cp "target\gpa-calculator-1.0-SNAPSHOT.jar" com.kaif.gpacalculator.GpaCalculatorApp

:end
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application exited with error code: %ERRORLEVEL%
    pause
)
