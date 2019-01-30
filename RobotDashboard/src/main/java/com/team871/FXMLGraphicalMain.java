package com.team871;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @project Robotics Dashboard
 * @author Team871-TPfaffe
 *
 */

public class FXMLGraphicalMain extends Application {


    @Override
    public void start(Stage primaryStage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/display.fxml"));
        } catch (IOException e) {
            System.out.println("Failed to load the FXML file!");
        }

        primaryStage.setTitle("Test 1");
        primaryStage.setScene(new Scene(root, 720, 480));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void stop() {
        System.out.println("GraphicalMain shutdown initiated");
        Platform.exit();
    }
}
