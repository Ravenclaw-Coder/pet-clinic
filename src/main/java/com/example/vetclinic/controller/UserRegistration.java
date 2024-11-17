package com.example.vetclinic.controller;

import com.example.vetclinic.SceneManager;
import com.example.vetclinic.module.UserSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRegistration {

    private static final Logger LOGGER = Logger.getLogger(UserRegistration.class.getName());

    @FXML
    private TextField address;
    @FXML
    private Label errorText;

    @FXML
    private Button back;

    @FXML
    private TextField name;

    @FXML
    private TextField number;

    @FXML
    private PasswordField password;

    @FXML
    private Button registration;

    private UserSQL userSQL;

    public UserRegistration() {
        userSQL = UserSQL.getInstance();
    }

    @FXML
    void registration(ActionEvent event) {
        try {
            if (name.getText().isEmpty() || password.getText().isEmpty() || address.getText().isEmpty() || number.getText().isEmpty()) {
                errorText.setText("Заполните все поля");
                LOGGER.warning("All fields must be filled.");
            } else {
                boolean flag = userSQL.addUser(name.getText(), address.getText(), number.getText(), password.getText());
                if (flag) {
                    SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/userSignIn.fxml");
                    LOGGER.info("User registered successfully.");
                } else {
                    errorText.setText("Ошибка при регистрации");
                    LOGGER.warning("Error during registration.");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration failed", e);
        }
    }

    @FXML
    void toBack() {
        SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/userLogin.fxml");
        LOGGER.info("Navigating back to login screen.");
    }
}
