package com.example.vetclinic.controller;

import com.example.vetclinic.module.UserSQL;
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
import java.net.URL;
import java.util.ResourceBundle;

public class UserSignIn {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;




    @FXML
    private Button back;

    @FXML
    private Label errorText;

    @FXML
    private Button login;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNumberField;
    private static String log;
    private static String password;
    private UserSQL userSQL;
    public UserSignIn(){
        userSQL = UserSQL.getInstance();
    }

    @FXML
    void login(MouseEvent event) {
        // Проверяем введенные данные
        if (phoneNumberField.getText().isEmpty() & passwordField.getText().isEmpty()){

        }
        else {
            log= phoneNumberField.getText();
            password = passwordField.getText();


            if (isValidLogin(log, password)) {
                // Успешный вход
                login.setStyle("-fx-background-color: green;");
                openUserAccountWindow();
            } else {
                // Неуспешный вход
                login.setStyle("-fx-background-color: red;");
            }
        }
    }

    public static String getLogin() {
        return log;
    }

    public static String getPassword() {
        return password;
    }

    private boolean isValidLogin(String phoneNumber, String password) {
        // Здесь должна быть проверка введенных данных, например, сравнение с хранящимися данными в системе
        // В данном примере просто возвращаем true для демонстрации успешного входа
        return userSQL.isUsers(phoneNumberField.getText(), passwordField.getText());

    }

    @FXML
    void toBack() {
        try {
            Parent userLoginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/userLogin.fxml"));
            Scene userLoginScene = new Scene(userLoginRoot);
            Stage window = (Stage) back.getScene().getWindow();
            window.setScene(userLoginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
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
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'toComeUser.fxml'.";
        assert errorText != null : "fx:id=\"errorText\" was not injected: check your FXML file 'toComeUser.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'toComeUser.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'toComeUser.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'toComeUser.fxml'.";

    }

}
