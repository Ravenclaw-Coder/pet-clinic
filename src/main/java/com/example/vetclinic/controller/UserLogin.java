package com.example.vetclinic.controller;

import com.example.vetclinic.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

public class UserLogin {

    private static final Logger LOGGER = Logger.getLogger(UserLogin.class.getName());

    @FXML
    void loginToAccountUser(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/userSignIn.fxml");
            LOGGER.info("Переход к окну входа.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate to User Sign In: " + e.getMessage());
        }
    }

    @FXML
    void toRegistrationUser(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/userRegistration.fxml");
            LOGGER.info("Переход к окну регистрации.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate to User Registration: " + e.getMessage());
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            SceneManager.getInstance().switchScene("/com/example/vetclinic/start.fxml");
            LOGGER.info("Возврат к стартовому окну.");
        } catch (Exception e) {
            LOGGER.severe("Failed to navigate back to Start: " + e.getMessage());
        }
    }
}
