package com.example.vetclinic.view;

import com.example.vetclinic.module.Appointment;
import com.example.vetclinic.SceneManager;
import com.example.vetclinic.controller.UserSignInController;
import com.example.vetclinic.module.UserSQL;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UserAppointment {

    private static final Logger LOGGER = Logger.getLogger(UserAppointment.class.getName());

    @FXML
    private Button back;

    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> vetNameColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTimeColumn;

    @FXML
    void toBack(MouseEvent event) {
        LOGGER.info("Navigating back to user account screen.");
        SceneManager.getInstance().switchScene("/com/example/vetclinic/view/userAccount.fxml");
    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'userAppointment.fxml'.";
        assert appointmentsTable != null : "fx:id=\"appointmentsTable\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentIdColumn != null : "fx:id=\"appointmentIdColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentDateColumn != null : "fx:id=\"appointmentDateColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert vetNameColumn != null : "fx:id=\"vetNameColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        assert appointmentTimeColumn != null : "fx:id=\"appointmentTimeColumn\" was not injected: check your FXML file 'vetAppointment.fxml'.";

        UserSQL userSQL = UserSQL.getInstance();
        LocalDate date_current = LocalDate.now();

        ArrayList<Appointment> list = userSQL.listAppointments(UserSignInController.getLogin(), date_current);

        if (list.isEmpty()) {
            // Если записей нет, показать сообщение
            appointmentsTable.setPlaceholder(new Label("У Вас нет записей."));
        } else {
            // Заполняем таблицу данными
            ObservableList<Appointment> appointments = FXCollections.observableArrayList(list);
            appointmentsTable.setItems(appointments);
        }

        // Настройка колонок
        appointmentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAppointmentId()).asObject());
        appointmentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentDate()));
        vetNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVetName()));
        appointmentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTime()));
    }
}
