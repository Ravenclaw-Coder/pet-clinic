package com.example.vetclinic.controller;

import com.example.vetclinic.SceneManager;
import com.example.vetclinic.module.VeterinarianSQL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class VeterinarianRegistration {

    @FXML
    private TextField address;

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

    private VeterinarianSQL vetSQL;

    public VeterinarianRegistration() {
        vetSQL = VeterinarianSQL.getInstance();
    }

    @FXML
    void registration(MouseEvent event) {
        if (name.getText().isEmpty() || password.getText().isEmpty() || number.getText().isEmpty() || address.getText().isEmpty()) {
            // errorText.setText("Заполните все поля");
        } else {
            boolean flag = vetSQL.addDoctor(name.getText(), number.getText(), address.getText(), password.getText());
            if (flag) {
                SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/vetSignIn.fxml");
            } else {
                // System.out.println("Ошибка");
            }
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/vetLogin.fxml");
    }
}
