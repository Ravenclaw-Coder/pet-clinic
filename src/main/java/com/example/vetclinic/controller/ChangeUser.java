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
        // Получаем новый пароль из поля ввода
        String newPassword = password.getText();

        // Обновление имени и адреса пользователя в базе данных
        boolean isUpdated = userSQL.updateName(Integer.parseInt(user[0]), name.getText()) &&
                userSQL.updateAddress(Integer.parseInt(user[0]), address.getText());

        // Если данные были обновлены и пароль не пустой, пытаемся обновить пароль
        if (isUpdated) {
            boolean passwordUpdated = false;

            // Если новый пароль не пустой, обновляем пароль
            if (!newPassword.isEmpty()) {
                passwordUpdated = userSQL.addPassword(user[3], newPassword); // user[3] - это телефон пользователя
            }

            // Переход в аккаунт после сохранения данных (имя, адрес или пароль)
            Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
            Scene userAccountScene = new Scene(userAccountRoot);
            Stage window = (Stage) back.getScene().getWindow();
            window.setScene(userAccountScene);
            window.show();

            if (passwordUpdated) {
                System.out.println("Пароль успешно обновлен.");
            } else if (!newPassword.isEmpty()) {
                // Если пароль не обновился, выводим ошибку
                System.out.println("Ошибка при обновлении пароля.");
            }
        } else {
            // Если не удалось обновить имя или адрес
            System.out.println("Ошибка при обновлении данных.");
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
