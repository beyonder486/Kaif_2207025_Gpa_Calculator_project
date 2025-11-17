package com.kaif.gpacalculator.controller;

import com.kaif.gpacalculator.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the GPA Result Screen
 */
public class GpaResultController {

    @FXML private Label gpaValueLabel;
    @FXML private Label gpaGradeLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private Label totalCreditsLabel;
    @FXML private Label qualityPointsLabel;
    @FXML private Label performanceMessageLabel;
    
    @FXML private TableView<Course> resultTable;
    @FXML private TableColumn<Course, String> resultNameColumn;
    @FXML private TableColumn<Course, String> resultCodeColumn;
    @FXML private TableColumn<Course, Double> resultCreditColumn;
    @FXML private TableColumn<Course, String> resultTeacher1Column;
    @FXML private TableColumn<Course, String> resultTeacher2Column;
    @FXML private TableColumn<Course, String> resultGradeColumn;
    @FXML private TableColumn<Course, Double> resultPointsColumn;

    private double gpa;
    private List<Course> courses;

    @FXML
    private void initialize() {
        // Set up table columns
        resultNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        resultCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        resultCreditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        resultTeacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        resultTeacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        resultGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        resultPointsColumn.setCellValueFactory(new PropertyValueFactory<>("gradePoints"));
    }

    /**
     * Initialize data for the result screen
     */
    public void initData(double gpa, double totalCredits, List<Course> courses) {
        this.gpa = gpa;
        this.courses = courses;
        
        // Display GPA
        gpaValueLabel.setText(String.format("%.2f", gpa));
        
        // Determine grade and color
        String grade;
        String colorClass;
        String message;
        
        if (gpa >= 3.5) {
            grade = "Excellent (A)";
            colorClass = "gpa-excellent";
            message = "Outstanding performance! Keep up the excellent work!";
        } else if (gpa >= 3.0) {
            grade = "Good (B)";
            colorClass = "gpa-good";
            message = "Great job! You're doing well in your studies.";
        } else if (gpa >= 2.0) {
            grade = "Average (C)";
            colorClass = "gpa-average";
            message = "You're passing. Consider improving your study habits.";
        } else {
            grade = "Below Average";
            colorClass = "gpa-below-average";
            message = "You need to work harder. Seek help if needed.";
        }
        
        gpaGradeLabel.setText("Grade: " + grade);
        performanceMessageLabel.setText(message);
        
        // Apply color styling
        gpaValueLabel.getStyleClass().add(colorClass);
        
        // Calculate statistics
        int totalCourses = courses.size();
        double qualityPoints = courses.stream().mapToDouble(Course::getWeightedGradePoints).sum();
        
        totalCoursesLabel.setText(String.valueOf(totalCourses));
        totalCreditsLabel.setText(String.format("%.1f", totalCredits));
        qualityPointsLabel.setText(String.format("%.2f", qualityPoints));
        
        // Populate table with courses
        ObservableList<Course> courseData = FXCollections.observableArrayList(courses);
        resultTable.setItems(courseData);
    }

    @FXML
    private void handleBackToEntry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/course-entry.fxml"));
            Parent root = loader.load();
            
            // Get the controller and restore data if needed
            CourseEntryController controller = loader.getController();
            if (courses != null && !courses.isEmpty()) {
                controller.restoreData(courses);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewCalculation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/home.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        // TODO: Implement print/save functionality
        System.out.println("Print/Save functionality - To be implemented");
    }
}
