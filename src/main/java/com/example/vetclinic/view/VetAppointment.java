package com.example.vetclinic.view;

import com.example.vetclinic.controller.VetSignInController;
import com.example.vetclinic.module.Appointment;
import com.example.vetclinic.module.VeterinarianSQL;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VetAppointment {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back;

    @FXML
    private Label tricks;

    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> petNameColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTimeColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;




    @FXML
    void toBack(MouseEvent event) {
        try {
            Parent accountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/vetAccount.fxml"));
            Scene accountScene = new Scene(accountRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(accountScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentsTable != null : "fx:id=\"appointmentsTable\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentIdColumn != null : "fx:id=\"appointmentIdColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentDateColumn != null : "fx:id=\"appointmentDateColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert petNameColumn != null : "fx:id=\"petNameColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentTimeColumn != null : "fx:id=\"appointmentTimeColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";

        // Настройка остальных колонок
        appointmentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAppointmentId()).asObject());
        appointmentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentDate()));
        petNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPetName()));
        appointmentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTime()));

        // Настройка новой колонки для статуса
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        VeterinarianSQL doctorSQL = VeterinarianSQL.getInstance();
        LocalDate date_current = LocalDate.now();

        // Получение данных и заполнение таблицы
        ObservableList<Appointment> appointments = doctorSQL.listAppointments(VetSignInController.getLogin(), date_current);

        if (appointments.isEmpty()) {
            appointmentsTable.setPlaceholder(new Label("У Вас нет записей."));
        } else {
            appointmentsTable.setItems(appointments);
        }
    }
    @FXML
    void filterCompletedAppointments(MouseEvent event) {
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : appointmentsTable.getItems()) {
            if (appointment.isCompleted()) {
                filteredAppointments.add(appointment);
            }
        }

        appointmentsTable.setItems(filteredAppointments);
    }

}
