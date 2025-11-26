package com.kaif.gpacalculator;

import database.db;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GpaCalculatorApp extends Application {

    private Scene scene;
    private String cssPath = "/com/kaif/gpacalculator/css/styles.css";
    private db d=new db();

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the Home screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kaif/gpacalculator/view/home.fxml"));
            Parent root = loader.load();
            
            // Create scene
            scene = new Scene(root, 800, 600);
            
            // Add CSS stylesheet if available
            loadCSS();
            
            // Add key listener for CSS hot reload (Press F5 to reload CSS)
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.F5) {
                    reloadCSS();
                    System.out.println("CSS reloaded! (F5 pressed)");
                }
            });
            
            // Set up stage
            primaryStage.setTitle("GPA Calculator - Home (Press F5 to reload CSS)");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMaximized(false);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
        }
        d.getConnection();
    }

    private void loadCSS() {
        try {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } catch (NullPointerException e) {
            System.out.println("Warning: CSS file not found. Running without custom styles.");
        }
    }

    private void reloadCSS() {
        // Clear all stylesheets
        scene.getStylesheets().clear();
        // Reload CSS
        loadCSS();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
