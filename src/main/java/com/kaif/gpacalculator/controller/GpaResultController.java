package com.kaif.gpacalculator.controller;

import com.kaif.gpacalculator.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the GPA Result Screen
 */
public class GpaResultController {

    @FXML
    private Label gpaValueLabel;
    
    @FXML
    private Label gpaGradeLabel;
    
    @FXML
    private Label totalCreditsLabel;
    
    @FXML
    private Label totalCoursesLabel;
    
    @FXML
    private Button newCalculationButton;
    
    @FXML
    private Button backToHomeButton;
    
    private double gpa;
    private double totalCredits;
    private int totalCourses;
    
    /**
     * Initialize the result screen with GPA data
     */
    public void initData(double gpa, double totalCredits, List<Course> courses) {
        this.gpa = gpa;
        this.totalCredits = totalCredits;
        this.totalCourses = courses != null ? courses.size() : 0;
        
        displayResults();
    }
    
    /**
     * Display the GPA calculation results
     */
    private void displayResults() {
        gpaValueLabel.setText(String.format("%.2f", gpa));
        totalCreditsLabel.setText(String.format("%.1f", totalCredits));
        totalCoursesLabel.setText(String.format("%d", totalCourses));
        
        // Set color and grade based on GPA value
        String grade;
        String color;
        if (gpa >= 3.5) {
            grade = "Excellent (A)";
            color = "-fx-text-fill: #27ae60;"; // Green
        } else if (gpa >= 3.0) {
            grade = "Good (B)";
            color = "-fx-text-fill: #2980b9;"; // Blue
        } else if (gpa >= 2.0) {
            grade = "Average (C)";
            color = "-fx-text-fill: #f39c12;"; // Orange
        } else {
            grade = "Below Average (D/F)";
            color = "-fx-text-fill: #e74c3c;"; // Red
        }
        
        gpaValueLabel.setStyle(color);
        gpaGradeLabel.setText("Grade: " + grade);
    }
    
    /**
     * Handle new calculation button - return to course entry
     */
    @FXML
    private void handleNewCalculation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/course-entry.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) newCalculationButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            } catch (NullPointerException e) {
                System.out.println("Warning: CSS file not found.");
            }
            
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Course Entry");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading course entry screen: " + e.getMessage());
        }
    }
    
    /**
     * Handle back to entry button - return to course entry screen
     */
    @FXML
    private void handleBackToEntry() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/course-entry.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) gpaValueLabel.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            } catch (NullPointerException e) {
                System.out.println("Warning: CSS file not found.");
            }
            
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Course Entry");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading course entry screen: " + e.getMessage());
        }
    }
    
    /**
     * Handle print button - placeholder for print functionality
     */
    @FXML
    private void handlePrint() {
        System.out.println("Print/Save functionality - To be implemented");
        // Future enhancement: Add print or save to PDF functionality
    }
    
    /**
     * Handle back to home button
     */
    @FXML
    private void handleBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/home.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) backToHomeButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);
            
            try {
                scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            } catch (NullPointerException e) {
                System.out.println("Warning: CSS file not found.");
            }
            
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Home");
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading home screen: " + e.getMessage());
        }
    }
}
