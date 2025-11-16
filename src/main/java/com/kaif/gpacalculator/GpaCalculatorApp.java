package com.kaif.gpacalculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application class for GPA Calculator
 * Entry point for the JavaFX application
 */
public class GpaCalculatorApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Home screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/home.fxml"));
            Parent root = loader.load();
            
            // Create scene
            Scene scene = new Scene(root, 800, 600);
            
            // Add CSS stylesheet if available
            try {
                scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            } catch (NullPointerException e) {
                // CSS file not found, continue without styling
                System.out.println("Warning: CSS file not found. Running without custom styles.");
            }
            
            // Set up stage
            primaryStage.setTitle("GPA Calculator - Home");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMaximized(false); // Start normal size, but allow maximizing
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
