@echo off 
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "gpa-calculator.jar" com.kaif.gpacalculator.GpaCalculatorApp 
if %ERRORLEVEL% NEQ 0 pause 
