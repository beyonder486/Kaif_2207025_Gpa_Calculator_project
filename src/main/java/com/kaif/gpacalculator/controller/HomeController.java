package com.kaif.gpacalculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Home Screen
 */
public class HomeController {
    @FXML
    private void handleStartButton(ActionEvent event) {
        try {
            // Load the Course Entry screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/course-entry.fxml"));
            Parent root = loader.load();
            
            // Get the current stage and preserve dimensions
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            
            // Create new scene with preserved dimensions
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource("/com/kaif/gpacalculator/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Course Entry");
            
        } catch (IOException e) {
            System.err.println("Error loading course entry screen: " + e.getMessage());
        }
    }
}

