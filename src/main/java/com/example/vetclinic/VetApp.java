package com.example.vetclinic;

import javafx.application.Application;
import javafx.stage.Stage;

public class VetApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Veterinary Clinic");

        // Инициализация SceneManager
        SceneManager.getInstance().setPrimaryStage(primaryStage);

        // Первая сцена
        SceneManager.getInstance().switchScene("/com/example/vetclinic/start.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
