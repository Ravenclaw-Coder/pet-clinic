package com.example.vetclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UserSignInController {

    @FXML
    private Button back;
    @FXML
    private Button login;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Label errorText;

    private static String log;
    private static String password;

    @FXML
    void login(MouseEvent event) {
        log = SignInHandler.getLogin(phoneNumberField.getText());
        password = SignInHandler.getPassword(passwordField.getText());

        // Проверка для владельца
        boolean isValid = SignInHandler.isValidLogin(log, password, "owner");
        if (isValid) {
            login.setStyle("-fx-background-color: green;");
            openUserAccountWindow();
        } else {
            login.setStyle("-fx-background-color: red;");
            errorText.setText("Неправильный логин или пароль");
        }
    }

    private void openUserAccountWindow() {
        try {
            Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
            Scene userAccountScene = new Scene(userAccountRoot);
            Stage window = (Stage) login.getScene().getWindow();
            window.setScene(userAccountScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            Parent userLoginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/userLogin.fxml"));
            Scene userLoginScene = new Scene(userLoginRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(userLoginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'userSignIn.fxml'.";
        assert errorText != null : "fx:id=\"errorText\" was not injected: check your FXML file 'userSignIn.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'userSignIn.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'userSignIn.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'userSignIn.fxml'.";
    }

    // Новый метод getLogin для получения логина
    public static String getLogin() {
        return log;
    }
}
