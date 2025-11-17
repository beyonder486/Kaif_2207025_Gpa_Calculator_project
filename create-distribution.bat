@echo off
REM ============================================
REM GPA Calculator - Create Distribution Package
REM Creates a portable package with Java libs
REM ============================================

echo Creating distribution package...
echo.

REM Check if project is built
if not exist "target\gpa-calculator-1.0-SNAPSHOT.jar" (
    echo Building project...
    call mvn clean package
    if %ERRORLEVEL% NEQ 0 (
        echo ERROR: Build failed.
        pause
        exit /b 1
    )
)

REM Create distribution directory
if exist "dist" rmdir /s /q "dist"
mkdir "dist"
mkdir "dist\lib"

REM Copy application JAR
echo Copying application files...
copy "target\gpa-calculator-1.0-SNAPSHOT.jar" "dist\gpa-calculator.jar"

REM Copy JavaFX dependencies
copy "target\lib\*.jar" "dist\lib\"

REM Create launcher script for distribution
echo @echo off > "dist\GPA-Calculator.bat"
echo java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "gpa-calculator.jar" com.kaif.gpacalculator.GpaCalculatorApp >> "dist\GPA-Calculator.bat"
echo if %%ERRORLEVEL%% NEQ 0 pause >> "dist\GPA-Calculator.bat"

REM Create README for users
echo GPA Calculator - Portable Distribution > "dist\README.txt"
echo. >> "dist\README.txt"
echo REQUIREMENTS: >> "dist\README.txt"
echo - Java 21 or higher must be installed >> "dist\README.txt"
echo - Download from: https://adoptium.net/ >> "dist\README.txt"
echo. >> "dist\README.txt"
echo TO RUN: >> "dist\README.txt"
echo Double-click GPA-Calculator.bat >> "dist\README.txt"
echo. >> "dist\README.txt"
echo DEVELOPER: Kaif (Student ID: 2207025) >> "dist\README.txt"

echo.
echo ========================================
echo Distribution package created in 'dist' folder
echo ========================================
echo.
echo Contents:
echo   - GPA-Calculator.bat  (launcher script)
echo   - gpa-calculator.jar  (application)
echo   - lib\                (JavaFX dependencies)
echo   - README.txt          (user instructions)
echo.
echo TO DISTRIBUTE:
echo 1. Zip the entire 'dist' folder
echo 2. Share the zip file
echo 3. Users extract and run GPA-Calculator.bat
echo.
echo NOTE: Users must have Java 21 installed!
echo For no-Java-required distribution, use build-installer.bat
echo.
pause
