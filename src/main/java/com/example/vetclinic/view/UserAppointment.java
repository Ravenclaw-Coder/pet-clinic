package com.example.vetclinic.view;

import com.example.vetclinic.SceneManager;
import com.example.vetclinic.controller.UserSignIn;
import com.example.vetclinic.module.UserSQL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

public class UserAppointment {

    private static final Logger LOGGER = Logger.getLogger(UserAppointment.class.getName());

    @FXML
    private Button back;

    @FXML
    private Label tricks;

    @FXML
    void toBack(MouseEvent event) {
        LOGGER.info("Navigating back to user account screen.");
        SceneManager.getInstance().switchScene("/com/example/vetclinic/view/userAccount.fxml");
    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'userAppointment.fxml'.";
        assert tricks != null : "fx:id=\"tricks\" was not injected: check your FXML file 'userAppointment.fxml'.";

        UserSQL userSQL = UserSQL.getInstance();
        ArrayList<String> list = userSQL.listAppointments(UserSignIn.getLogin(), LocalDate.now());
        String str = "";

        if (list.isEmpty()) {
            tricks.setText("У вас нет записей.");
            LOGGER.info("No appointments found for user: " + UserSignIn.getLogin());
        } else {
            for (String appointment : list) {
                str += appointment + "\n";
            }
            tricks.setText(str);
            LOGGER.info("Appointments listed for user: " + UserSignIn.getLogin());
        }
    }
}
