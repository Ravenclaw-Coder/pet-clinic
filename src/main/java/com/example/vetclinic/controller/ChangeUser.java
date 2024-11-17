package com.example.vetclinic.controller;

import com.example.vetclinic.module.UserSQL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangeUser {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField address;

    @FXML
    private Button back;

    @FXML
    private TextField name;

    @FXML
    private Label phone;

    @FXML
    private TextField password;

    @FXML
    private Button save;

    private UserSQL userSQL;
    private String[] user;

    public ChangeUser() {
        userSQL = UserSQL.getInstance();
        // Получение информации о пользователе по логину, который сохраняется в UserSignIn
        user = userSQL.getUser(UserSignInController.getLogin());
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            // Переход на экран пользовательского аккаунта
            Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
            Scene userAccountScene = new Scene(userAccountRoot);
            Stage window = (Stage) back.getScene().getWindow();
            window.setScene(userAccountScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toSave(MouseEvent event) throws IOException {
        // Обновление имени и адреса пользователя в базе данных
        if (userSQL.updateName(Integer.parseInt(user[0]), name.getText()) &&
                userSQL.updateAddress(Integer.parseInt(user[0]), address.getText())) {
            try {
                // Переход обратно в окно пользовательского аккаунта после успешного сохранения
                Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
                Scene userAccountScene = new Scene(userAccountRoot);
                Stage window = (Stage) back.getScene().getWindow();
                window.setScene(userAccountScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        // Проверка на наличие всех элементов в FXML
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'changeUser.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'changeUser.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'changeUser.fxml'.";
        assert phone != null : "fx:id=\"number\" was not injected: check your FXML file 'changeUser.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'changeUser.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'changeUser.fxml'.";

        // Инициализация полей с данными пользователя
        phone.setText(UserSignInController.getLogin());
        name.setText(user[1]);  // Имя пользователя
        address.setText(user[2]); // Адрес пользователя
    }
}
