# GPA Calculator Application

A modern JavaFX desktop application for calculating student GPA with an intuitive and visually appealing interface.

## 📋 Features

- ✅ Modern gradient-based UI design
- ✅ Course entry with name, code, credit hours, teachers, and grades
- ✅ Real-time GPA calculation with color-coded results
- ✅ Target credit hours tracking
- ✅ Smooth animations and transitions
- ✅ Responsive layout (resizable, maximizable window)
- ✅ Grade scale: A+ (4.0) to F (0.0)

## 🚀 Quick Start

### Running the Application

**Easiest way:**
```batch
run.bat
```

**With Maven:**
```batch
mvn javafx:run
```

## 📦 Distribution Options

### For End Users (No Java Required!)
Create a self-contained installer:
```batch
build-installer.bat
```
Output: `release/GPA Calculator-1.0.exe`

### For Portable Distribution (Requires Java 21)
Create a portable package:
```batch
create-distribution.bat
```
Output: `dist/` folder (zip and share)

📖 **See [QUICK-START.md](QUICK-START.md) for detailed instructions**  
📖 **See [DISTRIBUTION.md](DISTRIBUTION.md) for full distribution guide**

## 🛠️ Technology Stack

- **Language:** Java 21
- **UI Framework:** JavaFX 21.0.5
- **Build Tool:** Maven 3.x
- **Architecture:** MVC Pattern

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/kaif/gpacalculator/
│   │   ├── GpaCalculatorApp.java       # Main entry point
│   │   ├── controller/                  # Controllers
│   │   │   ├── HomeController.java
│   │   │   ├── CourseEntryController.java
│   │   │   └── GpaResultController.java
│   │   ├── model/                       # Data models
│   │   │   └── Course.java
│   │   └── util/                        # Utilities
│   │       └── GpaCalculator.java
│   └── resources/com/kaif/gpacalculator/
│       ├── view/                        # FXML layouts
│       │   ├── home.fxml
│       │   ├── course-entry.fxml
│       │   └── gpa-result.fxml
│       └── css/
│           └── styles.css               # Modern styling
```

## 💻 System Requirements

### For Running
- **Java:** JRE/JDK 21 or higher
- **OS:** Windows 10/11, macOS, or Linux
- **Memory:** 512 MB RAM minimum

### For Building
- **JDK:** Java Development Kit 21
- **Maven:** 3.6 or higher
- **IDE:** IntelliJ IDEA (recommended)

## 🎓 Grade Scale

| Grade | Points |
|-------|--------|
| A+    | 4.0    |
| A     | 3.75   |
| A-    | 3.7    |
| B+    | 3.3    |
| B     | 3.0    |
| B-    | 2.7    |
| C+    | 2.3    |
| C     | 2.0    |
| C-    | 1.7    |
| D+    | 1.3    |
| D     | 1.0    |
| F     | 0.0    |

## 🔧 Building from Source

```batch
# Clone the repository
git clone https://github.com/beyonder486/Kaif_2207025_Gpa_Calculator_project.git

# Navigate to project
cd Kaif_2207025_Gpa_Calculator_project

# Build
mvn clean package

# Run
mvn javafx:run
```

## 📝 License

Educational project by Kaif (Student ID: 2207025)

## 🤝 Contributing

This is an educational project. For questions or suggestions, please contact the developer.

---

**Developer:** Kaif  
**Student ID:** 2207025  
**Year:** 2025
