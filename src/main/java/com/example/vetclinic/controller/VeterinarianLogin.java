package com.example.vetclinic.controller;

import com.example.vetclinic.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

public class VeterinarianLogin {

    private static final Logger LOGGER = Logger.getLogger(VeterinarianLogin.class.getName());

    @FXML
    void loginToAccountVet(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/vetSignIn.fxml");
            LOGGER.info("Переход к окну входа.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate: " + e.getMessage());
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/start.fxml");
            LOGGER.info("Возврат к стартовому окну.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate: " + e.getMessage());
        }
    }

    @FXML
    void toRegistrationVet(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/vetRegistration.fxml");
            LOGGER.info("Переход к окну регистрации.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate: " + e.getMessage());
        }
    }

}
