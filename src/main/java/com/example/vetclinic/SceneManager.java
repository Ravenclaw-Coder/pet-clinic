package com.example.vetclinic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneManager {

    private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());
    private static SceneManager instance;
    private Stage primaryStage;

    // Приватный конструктор для обеспечения Singleton
    private SceneManager() {}

    // Получение единственного экземпляра
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    // Установка главного окна
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Метод для переключения сцен
    public void switchScene(String fxmlPath) {
        if (primaryStage == null) {
            LOGGER.severe("Primary stage is not set. Call setPrimaryStage().");
            throw new IllegalStateException("Primary stage is not set.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            LOGGER.info("Успешный переход к : " + fxmlPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML: " + fxmlPath, e);
        }
    }
}
