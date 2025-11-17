# GPA Calculator - Distribution Guide

## Overview
This guide explains how to run and distribute the GPA Calculator application on different devices.

---

## Option 1: Quick Run (Requires Java 21)

### For Development/Testing
Simply run the included batch file:
```batch
run.bat
```

This script will:
- Detect if Maven is available and run directly with `mvn javafx:run` (best option)
- Otherwise, run using Java with the module path and JavaFX libraries
- Display helpful error messages if requirements are missing

### Manual Build & Run
```batch
# Build the project
mvn clean package

# Run with Maven (recommended)
mvn javafx:run

# OR run with Java directly
java --module-path target\lib --add-modules javafx.controls,javafx.fxml -cp target\gpa-calculator-1.0-SNAPSHOT.jar com.kaif.gpacalculator.GpaCalculatorApp
```

---

## Option 2: Self-Contained Installer (NO Java Required!)

### Building the Installer

Run the included build script:
```batch
build-installer.bat
```

This will:
1. Build the project with Maven
2. Create a custom Java runtime using `jlink`
3. Package everything into a Windows `.exe` installer using `jpackage`
4. Output to `release/GPA Calculator-1.0.exe`

**Requirements for building:**
- JDK 21 (must include `jpackage` and `jlink` tools)
- Maven

### What the Installer Includes
- ✅ Complete GPA Calculator application
- ✅ Bundled Java 21 runtime (users don't need Java installed)
- ✅ Desktop shortcut
- ✅ Start menu entry
- ✅ Windows uninstaller

### Distributing to Other Devices
1. Run `build-installer.bat` on your development machine
2. Share the generated `GPA Calculator-1.0.exe` file (located in `release/` folder)
3. Users simply run the installer - **no Java installation needed!**

---

## Option 3: Portable Package Distribution

### Create a Portable Package (Requires Java 21 on target device)

Run the distribution builder:
```batch
create-distribution.bat
```

This creates a `dist` folder containing:
- **GPA-Calculator.bat** - Double-click launcher
- **gpa-calculator.jar** - Application
- **lib/** - JavaFX dependencies
- **README.txt** - User instructions

### To Distribute:
1. Zip the entire `dist` folder
2. Share the zip file with users
3. Users extract and double-click `GPA-Calculator.bat`

**Note:** Users must have Java 21 installed. This is the easiest method for sharing with other developers or tech-savvy users.

---

## System Requirements

### For Self-Contained Installer (Option 2)
- **Operating System:** Windows 10/11
- **Disk Space:** ~200 MB
- **No Java installation required!**

### For JAR Distribution (Options 1 & 3)
- **Operating System:** Windows, macOS, or Linux
- **Java Version:** Java 21 or higher
- **Disk Space:** ~50 MB

### For Building/Development
- **JDK:** Java Development Kit 21
- **Build Tool:** Maven 3.6+
- **IDE:** IntelliJ IDEA recommended (optional)

---

## Platform-Specific Notes

### Windows
- The installer creates desktop and start menu shortcuts
- Uninstall via Windows "Add or Remove Programs"

### macOS
To create a Mac installer, use `jpackage` on macOS:
```bash
jpackage --name "GPA Calculator" \
  --app-version 1.0 \
  --input target \
  --main-jar gpa-calculator-1.0-SNAPSHOT-fat.jar \
  --type dmg \
  --dest release
```

### Linux
To create a Linux package, use `jpackage` on Linux:
```bash
jpackage --name "gpa-calculator" \
  --app-version 1.0 \
  --input target \
  --main-jar gpa-calculator-1.0-SNAPSHOT-fat.jar \
  --type deb \
  --dest release
```

---

## Quick Reference

| Distribution Method | User Requirements | File Size | Cross-Platform |
|---------------------|-------------------|-----------|----------------|
| Self-Contained Installer | None! | ~200 MB | No (build per OS) |
| Fat JAR | Java 21 | ~50 MB | Yes |
| Maven Run | Java 21 + Maven | N/A | Yes |

---

## Troubleshooting

### "Java is not installed or not in PATH"
- Download and install Java 21 from: https://adoptium.net/

### "jpackage not found"
- Ensure you're using JDK 21 (not JRE)
- Verify `jpackage` is in your PATH: `jpackage --version`

### Application won't start
- Verify Java version: `java -version` (should show 21 or higher)
- Try running from command line to see error messages
- Check that all FXML and resource files are included in the JAR

### Building fails
- Run `mvn clean` before building
- Check internet connection (Maven needs to download dependencies)
- Verify `JAVA_HOME` environment variable points to JDK 21

---

## Recommended Distribution Method

**For end users (students, teachers):**
Use the **self-contained installer** (Option 2). Users simply download and run the `.exe` file - no technical knowledge required!

**For developers:**
Use **Maven** (Option 1) for quick testing and development.

**For cross-platform sharing:**
Use the **fat JAR** (Option 3) if recipients have Java 21 installed.

---

## Developer Info
- **Developer:** Kaif (Student ID: 2207025)
- **Java Version:** 21
- **JavaFX Version:** 21.0.5
- **Build Tool:** Maven 3.x
