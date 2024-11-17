package com.example.vetclinic.controller;

import com.example.vetclinic.module.AppointmentSQL;
import com.example.vetclinic.module.PetSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserAppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back;

    @FXML
    private RadioButton btnAft;

    @FXML
    private RadioButton btnAft_Morn;

    @FXML
    private RadioButton btnEven;

    @FXML
    private RadioButton btnMorn;

    @FXML
    private CheckBox clicker;

    @FXML
    private DatePicker date;

    @FXML
    private TextField name;

    @FXML
    private TextField namePoroda;

    @FXML
    private Label error;

    @FXML
    private ToggleGroup groupTime;

    @FXML
    private Button recording;

    private PetSQL petSQL;
    private AppointmentSQL appointmentSQL;

    public UserAppointmentController(){
        petSQL = PetSQL.getInstance();
        appointmentSQL = AppointmentSQL.getInstance();
    }

    @FXML
    private void togglePoroda(ActionEvent event) {
        // Включаем/выключаем поле ввода породы в зависимости от состояния флажка
        namePoroda.setDisable(!clicker.isSelected());
    }

    @FXML
    private CheckBox isOwnerCheckBox; // Галочка для указания владельца питомца

    @FXML
    void recording(MouseEvent event) {
        String petName = name.getText();
        String breed = clicker.isSelected() ? namePoroda.getText() : null;
        LocalDate appointmentDate = date.getValue();
        String time = null;

        if (btnMorn.isSelected()) time = "10:00";
        else if (btnAft_Morn.isSelected()) time = "12:00";
        else if (btnAft.isSelected()) time = "14:00";
        else if (btnEven.isSelected()) time = "18:00";

        if (petName.isEmpty() || appointmentDate == null || time == null) {
            error.setText("Заполните все поля");
            return;
        }

        String ownerPhone = UserSignInController.getLogin(); // Получаем телефон владельца

        // Проверяем, есть ли питомец у данного владельца
        int petId = petSQL.getPetId(petName, ownerPhone);
        boolean petExists = petId != -1; // Если petId != -1, значит питомец уже существует

        boolean isOwner = isOwnerCheckBox.isSelected(); // Проверка, является ли питомец с хозяином

        // Если питомца нет, добавляем его
        if (!petExists) {
            boolean petAdded = petSQL.addPet(petName, breed != null ? "1" : "0", breed, ownerPhone, isOwner);
            if (!petAdded) {
                error.setText("Не удалось добавить питомца.");
                return;
            }
        }

        // Логика для добавления записи на прием
        boolean appointmentAdded = appointmentSQL.addAppointment(petName, breed, appointmentDate, time, ownerPhone, isOwner);
        if (appointmentAdded) {
            try {
                Parent successRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/successfulRegistration.fxml"));
                Stage window = (Stage) back.getScene().getWindow();
                window.setScene(new Scene(successRoot));
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            error.setText("Ошибка добавления записи. Попробуйте снова.");
        }
    }





    @FXML
    void toBack(MouseEvent event) {
        try {
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
    void initialize() {
        groupTime = new ToggleGroup();
        btnAft.setToggleGroup(groupTime);
        btnAft_Morn.setToggleGroup(groupTime);
        btnEven.setToggleGroup(groupTime);
        btnMorn.setToggleGroup(groupTime);
        namePoroda.setDisable(true);
    }
}
