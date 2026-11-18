package com.imagemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/imagemanager/fxml/MainView.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(
                getClass().getResource("/com/imagemanager/fxml/style.css").toExternalForm()
        );
        primaryStage.setTitle("Gestionnaire d'Images - POO 2025-2026");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}