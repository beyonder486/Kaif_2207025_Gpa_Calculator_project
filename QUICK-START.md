# Quick Start - Running GPA Calculator on Any Device

## For Users (Non-Technical)

### Option A: Self-Contained Installer (BEST - No Java Needed!)
1. Ask the developer to run `build-installer.bat`
2. Get the `.exe` file from the `release` folder
3. Double-click to install
4. Run from Start Menu or Desktop shortcut
5. **No Java installation required!**

## For Users with Java 21

### Option B: Portable Package (Simple)
1. Extract the provided zip file
2. Double-click `GPA-Calculator.bat`
3. Done!

## For Developers

### Option C: Run from Source
```batch
# Clone or open project
cd Kaif_2207025_Gpa_Calculator_project

# Quick run
run.bat

# Or with Maven directly
mvn javafx:run
```

## Which Method to Choose?

| Method | User Needs | File Size | Best For |
|--------|------------|-----------|----------|
| **Self-Contained Installer** | Nothing! | ~200 MB | End users, students |
| **Portable Package** | Java 21 | ~50 MB | Developers, tech users |
| **Run from Source** | Java 21 + Maven | N/A | Development, testing |

## Quick Commands

### Build Everything:
```batch
mvn clean package
```

### Create Portable Package:
```batch
create-distribution.bat
```

### Create Installer (No Java Required for Users):
```batch
build-installer.bat
```

### Run Directly:
```batch
run.bat
```

## Requirements Summary

### To BUILD the installer (one-time):
- Windows PC with JDK 21
- Maven installed
- Internet connection (first build)

### To RUN the installer on other devices:
- **Nothing!** Just run the .exe file

### To RUN the portable package:
- Java 21 installed
- Extract zip and run .bat file

---

**Developer:** Kaif (Student ID: 2207025)  
**Project:** GPA Calculator  
**Java Version:** 25  
**JavaFX Version:** 25.0.5
