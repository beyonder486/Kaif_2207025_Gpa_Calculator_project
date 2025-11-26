package com.kaif.gpacalculator.controller;

import com.kaif.gpacalculator.model.Course;
import com.kaif.gpacalculator.util.GpaCalculator;
import database.db;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Course Entry Screen with Database Integration
 */
public class CourseEntryController implements Initializable {

    @FXML private TextField totalCreditsField;
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeComboBox;
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, Double> creditColumn;
    @FXML private TableColumn<Course, String> teacher1Column;
    @FXML private TableColumn<Course, String> teacher2Column;
    @FXML private TableColumn<Course, String> gradeColumn;
    @FXML private TableColumn<Course, Void> actionColumn;
    @FXML private Label currentCreditsLabel;
    @FXML private Label targetCreditsLabel;
    @FXML private Button calculateButton;
    @FXML private Button addCourseButton;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private double targetCredits = 0.0;
    private double currentCredits = 0.0;
    
    // Database instance
    private db database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize database
        database = db.getInstance();
        
        // Initialize grade combo box
        gradeComboBox.setItems(FXCollections.observableArrayList(
            "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F"
        ));
        
        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // Add delete button to action column
        addDeleteButtonToTable();
        
        // Set items to table
        coursesTable.setItems(courseList);
        
        // Enable smooth scrolling
        coursesTable.setFixedCellSize(50.0);
        
        // Disable course entry controls until target is set
        disableCourseEntryControls();
        
        // Load existing courses from database (optional - can be commented out for fresh start each time)
        // loadCoursesFromDatabase();
    }
    
    /**
     * Load courses from database into the table
     */
    private void loadCoursesFromDatabase() {
        courseList.clear();
        courseList.addAll(database.getAllCourses());
        updateCurrentCredits();
    }

    @FXML
    private void handleSetTarget() {
        try {
            double credits = Double.parseDouble(totalCreditsField.getText().trim());
            if (credits <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a positive number for credits.");
                return;
            }
            targetCredits = credits;
            targetCreditsLabel.setText(String.format("Target: %.1f", targetCredits));
            enableCourseEntryControls();
            updateCalculateButton();
            showAlert(Alert.AlertType.INFORMATION, "Target Set", 
                     String.format("Target credits set to %.1f. You can now add courses.", targetCredits));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for credits.");
        }
    }

    @FXML
    private void handleAddCourse() {
        // Check if target is set
        if (targetCredits <= 0) {
            showAlert(Alert.AlertType.WARNING, "Target Not Set", "Please set target credits before adding courses.");
            return;
        }
        
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            String courseName = courseNameField.getText().trim();
            String courseCode = courseCodeField.getText().trim();
            double courseCredit = Double.parseDouble(courseCreditField.getText().trim());
            String teacher1 = teacher1Field.getText().trim();
            String teacher2 = teacher2Field.getText().trim();
            String grade = gradeComboBox.getValue();

            // Check if adding this course would exceed target credits
            if (targetCredits > 0 && (currentCredits + courseCredit) > targetCredits) {
                showAlert(Alert.AlertType.WARNING, "Credit Limit", 
                         String.format("Adding this course would exceed target credits!\nCurrent: %.1f, Adding: %.1f, Target: %.1f",
                         currentCredits, courseCredit, targetCredits));
                return;
            }

            // Create and add course
            Course course = new Course(courseName, courseCode, courseCredit, teacher1, teacher2, grade);
            
            // Save to database
            if (database.insertCourse(course)) {
                // Add to local list (UI)
                courseList.add(course);
                
                // Update current credits
                currentCredits += courseCredit;
                currentCreditsLabel.setText(String.format("Current: %.1f", currentCredits));
                
                // Update calculate button state
                updateCalculateButton();
                
                // Clear fields
                handleClearFields();
                
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Course added and saved to database!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save course to database.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for course credit.");
        }
    }
    
    /**
     * Update current credits from course list
     */
    private void updateCurrentCredits() {
        currentCredits = courseList.stream()
            .mapToDouble(Course::getCourseCredit)
            .sum();
        currentCreditsLabel.setText(String.format("Current: %.1f", currentCredits));
    }

    @FXML
    private void handleClearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToHome() {
        if (courseList.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Navigation");
            alert.setHeaderText("You have unsaved courses");
            alert.setContentText("Are you sure you want to go back? All entered data will be lost.");
            
            if (alert.showAndWait().get() != ButtonType.OK) {
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/home.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) courseNameField.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Home");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load home screen: " + e.getMessage());
        }
    }

    @FXML
    private void handleCalculateGPA() {
        if (courseList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Courses", "Please add at least one course before calculating GPA.");
            return;
        }

        // Check if target credits is set and fulfilled
        if (targetCredits > 0) {
            if (Math.abs(currentCredits - targetCredits) > 0.01) {
                showAlert(Alert.AlertType.ERROR, "Credit Requirement Not Met", 
                         String.format("You must fulfill the target credits before calculating GPA.\nTarget: %.1f credits\nCurrent: %.1f credits\nRemaining: %.1f credits",
                         targetCredits, currentCredits, targetCredits - currentCredits));
                return;
            }
        }

        try {
            // Calculate GPA
            double gpa = GpaCalculator.calculateGPA(courseList.stream().toList());
            double totalCredits = GpaCalculator.calculateTotalCredits(courseList.stream().toList());
            
            // Save calculation to database
            database.insertCalculation(gpa, totalCredits, courseList.stream().toList());
            System.out.println("GPA calculation saved to database!");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/gpa-result.fxml"));
            Parent root = loader.load();
            
            // Pass data to result controller
            GpaResultController resultController = loader.getController();
            resultController.initData(gpa, totalCredits, courseList.stream().toList());
            
            Stage stage = (Stage) calculateButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Results");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load result screen: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleClearForm() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeComboBox.getSelectionModel().clearSelection();
    }

    private boolean validateInputs() {
        if (courseNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter course name.");
            return false;
        }
        if (courseCodeField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter course code.");
            return false;
        }
        if (courseCreditField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter course credit.");
            return false;
        }
        if (teacher1Field.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter Teacher 1 name.");
            return false;
        }
        if (teacher2Field.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter Teacher 2 name.");
            return false;
        }
        if (gradeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please select a grade.");
            return false;
        }

        try {
            double credit = Double.parseDouble(courseCreditField.getText().trim());
            if (credit <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Credit", "Course credit must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Credit", "Please enter a valid number for course credit.");
            return false;
        }

        return true;
    }

    private void updateCalculateButton() {
        // Enable calculate button only when target credits is fulfilled (if set)
        if (targetCredits > 0) {
            // Require target to be met
            calculateButton.setDisable(Math.abs(currentCredits - targetCredits) >= 0.01);
        } else {
            // If no target set, allow calculation anytime there are courses
            calculateButton.setDisable(courseList.isEmpty());
        }
    }

    private void addDeleteButtonToTable() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Course");
                    alert.setHeaderText("Delete " + course.getCourseName() + "?");
                    alert.setContentText("Are you sure you want to delete this course?");
                    
                    if (alert.showAndWait().get() == ButtonType.OK) {
                        currentCredits -= course.getCourseCredit();
                        currentCreditsLabel.setText(String.format("Current: %.1f", currentCredits));
                        courseList.remove(course);
                        updateCalculateButton();
                    }
                });
                deleteButton.getStyleClass().add("delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void disableCourseEntryControls() {
        courseNameField.setDisable(true);
        courseCodeField.setDisable(true);
        courseCreditField.setDisable(true);
        teacher1Field.setDisable(true);
        teacher2Field.setDisable(true);
        gradeComboBox.setDisable(true);
        addCourseButton.setDisable(true);
    }
    
    private void enableCourseEntryControls() {
        courseNameField.setDisable(false);
        courseCodeField.setDisable(false);
        courseCreditField.setDisable(false);
        teacher1Field.setDisable(false);
        teacher2Field.setDisable(false);
        gradeComboBox.setDisable(false);
        addCourseButton.setDisable(false);
    }
    
    /**
     * Restore previously entered course data when navigating back
     */
    public void restoreData(java.util.List<Course> courses) {
        courseList.clear();
        courseList.addAll(courses);
        coursesTable.setItems(courseList);
        
        // Recalculate current credits
        currentCredits = courseList.stream()
                .mapToDouble(Course::getCourseCredit)
                .sum();
        currentCreditsLabel.setText(String.format("%.1f", currentCredits));
        
        // Update calculate button state
        updateCalculateButton();
    }
    
    /**
     * Clear all courses from the current session and database
     */
    @FXML
    private void handleClearAllCourses() {
        if (courseList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Courses", "There are no courses to clear.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear All Courses");
        alert.setHeaderText("Delete all courses?");
        alert.setContentText("This will remove all courses from the current session and database. This action cannot be undone.");
        
        if (alert.showAndWait().get() == ButtonType.OK) {
            database.deleteAllCourses();
            courseList.clear();
            currentCredits = 0.0;
            currentCreditsLabel.setText("Current: 0.0");
            updateCalculateButton();
            showAlert(Alert.AlertType.INFORMATION, "Success", "All courses have been cleared.");
        }
    }
    
    /**
     * Export current courses to JSON
     */
    @FXML
    private void handleExportToJSON() {
        if (courseList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Courses", "There are no courses to export.");
            return;
        }
        
        try {
            String json = database.exportCoursesToJSON();
            // You can add file chooser here to save to a file
            System.out.println("Courses exported to JSON:");
            System.out.println(json);
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", 
                     "Courses exported to JSON. Check console for output.\n" +
                     "In a full implementation, this would save to a file.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Failed to export courses: " + e.getMessage());
        }
    }
    
    /**
     * View calculation history
     */
    @FXML
    private void handleViewHistory() {
        var history = database.getCalculationHistory(10);
        
        if (history.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No History", "No calculation history found.");
            return;
        }
        
        StringBuilder message = new StringBuilder("Recent GPA Calculations:\n\n");
        for (var record : history) {
            message.append(record.toString()).append("\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Calculation History");
        alert.setHeaderText("Your Recent GPA Calculations");
        alert.setContentText(message.toString());
        alert.getDialogPane().setPrefWidth(600);
        alert.showAndWait();
    }
    
    /**
     * Load courses from database
     */
    @FXML
    private void handleLoadFromDatabase() {
        var savedCourses = database.getAllCourses();
        
        if (savedCourses.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No saved courses found in database.");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Load Saved Courses");
        confirmAlert.setHeaderText("Load " + savedCourses.size() + " courses from database?");
        confirmAlert.setContentText("This will replace your current course list. Current courses will be lost if not saved.");
        
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            courseList.clear();
            courseList.addAll(savedCourses);
            updateCurrentCredits();
            updateCalculateButton();
            showAlert(Alert.AlertType.INFORMATION, "Success", 
                     savedCourses.size() + " courses loaded from database!");
        }
    }
}
