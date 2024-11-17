package com.example.vetclinic.controller;

import com.example.vetclinic.module.VetAppointmentSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class VetAppointmentController {

    @FXML
    private Button back;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField disease_id;

    @FXML
    private TextField appoint_id;

    @FXML
    private CheckBox ownerCheckbox;

    @FXML
    private Label error;

    private final VetAppointmentSQL consultSQL;

    public VetAppointmentController() {
        consultSQL = VetAppointmentSQL.getInstance();
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/vetAccount.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toRecep(ActionEvent event) {
        // Получаем значения полей и состояния чекбокса
        String appointmentIdText = appoint_id.getText();
        String diseaseIdText = disease_id.getText();
        boolean isOwner = ownerCheckbox.isSelected();

        // Проверка заполненности полей
        if (appointmentIdText.isEmpty() || diseaseIdText.isEmpty()) {
            error.setText("Заполните все поля");
            return;
        }

        try {
            // Конвертация ID в числа
            int appointmentId = Integer.parseInt(appointmentIdText);
            int diseaseId = Integer.parseInt(diseaseIdText);

            // Вызов метода для добавления консультации
            boolean success = consultSQL.addConsultation(appointmentId, diseaseId, isOwner);

            if (success) {
                // Переход на экран аккаунта
                Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/vetAccount.fxml"));
                Scene loginScene = new Scene(loginRoot);
                Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
                window.setScene(loginScene);
                window.show();
            } else {
                error.setText("Ошибка: приема с таким ID не существует");
            }
        } catch (NumberFormatException e) {
            error.setText("Введите корректные числовые значения");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert confirmButton != null : "fx:id=\"confirmButton\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert disease_id != null : "fx:id=\"disease_id\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert appoint_id != null : "fx:id=\"appoint_id\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert ownerCheckbox != null : "fx:id=\"ownerCheckbox\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'vetAppointController.fxml'.";
    }
}
