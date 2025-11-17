@echo off
echo ========================================
echo Creating Portable GPA Calculator Package
echo ========================================

REM Create dist directory
if not exist "dist" mkdir dist
if exist "dist\GPA-Calculator" rmdir /s /q "dist\GPA-Calculator"
mkdir "dist\GPA-Calculator"

echo.
echo [1/5] Copying application files...
copy "target\gpa-calculator-1.0-SNAPSHOT-shaded.jar" "dist\GPA-Calculator\gpa-calculator.jar"
copy "run.bat" "dist\GPA-Calculator\run.bat"

echo.
echo [2/5] Creating JRE (this may take a few minutes)...
jlink --add-modules java.base,java.desktop,java.logging,java.xml,jdk.unsupported,jdk.crypto.ec ^
      --output "dist\GPA-Calculator\jre" ^
      --strip-debug ^
      --no-man-pages ^
      --no-header-files ^
      --compress=2

echo.
echo [3/5] Creating launcher script...
(
echo @echo off
echo cd /d "%%~dp0"
echo start "" "jre\bin\javaw.exe" -jar "gpa-calculator.jar"
) > "dist\GPA-Calculator\GPA-Calculator.bat"

echo.
echo [4/5] Creating README...
(
echo GPA Calculator - Portable Edition
echo ==================================
echo.
echo To run the application:
echo 1. Double-click GPA-Calculator.bat
echo 2. Or run: run.bat
echo.
echo This package includes:
echo - GPA Calculator Application
echo - Bundled Java Runtime ^(JRE 21^)
echo - No installation required
echo.
echo System Requirements:
echo - Windows 10 or later
echo - 200 MB disk space
echo.
) > "dist\GPA-Calculator\README.txt"

echo.
echo [5/5] Creating ZIP package...
cd dist
powershell -command "Compress-Archive -Path 'GPA-Calculator' -DestinationPath 'GPA-Calculator-Portable.zip' -Force"
cd ..

echo.
echo ========================================
echo ✓ Portable package created successfully!
echo Location: dist\GPA-Calculator-Portable.zip
echo ========================================
echo.
echo To test on another device:
echo 1. Copy GPA-Calculator-Portable.zip to the device
echo 2. Extract the ZIP file
echo 3. Run GPA-Calculator.bat
echo.
pause
