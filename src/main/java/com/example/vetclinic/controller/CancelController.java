package com.example.vetclinic.controller;

import com.example.vetclinic.module.Cancel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CancelController {

    @FXML
    private TextField appointmentIdField;

    @FXML
    private Label statusLabel;

    private Cancel cancel;

    public CancelController() {
        cancel = new Cancel();
    }

    @FXML
    void cancelAppointment(ActionEvent event) {
        String appointmentIdText = appointmentIdField.getText();
        if (appointmentIdText.isEmpty()) {
            statusLabel.setText("Введите ID записи");
            return;
        }

        try {
            int appointmentId = Integer.parseInt(appointmentIdText);
            boolean isCancelled = cancel.cancelAppointment(appointmentId);

            if (isCancelled) {
                statusLabel.setText("Запись успешно отменена.");
            } else {
                statusLabel.setText("Не удалось отменить запись.");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Неверный формат ID.");
        }
    }

    @FXML
    void goBackToAccount(ActionEvent event) {
        try {
            Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
            Scene userAccountScene = new Scene(userAccountRoot);
            Stage window = (Stage) statusLabel.getScene().getWindow(); // Получаем текущее окно
            window.setScene(userAccountScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
