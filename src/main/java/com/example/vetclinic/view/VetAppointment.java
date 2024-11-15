package com.example.vetclinic.view;

import com.example.vetclinic.controller.VetSignIn;
import com.example.vetclinic.module.VeterinarianSQL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        assert tricks != null : "fx:id=\"tricks\" was not injected: check your FXML file 'vetAppointment.fxml'.";
        VeterinarianSQL doctorSQL = VeterinarianSQL.getInstance();
        LocalDate date_current = LocalDate.now();
        System.out.println(date_current);
        ArrayList<String> list = doctorSQL.listAppointments(VetSignIn.getLog(),date_current);
        String str = "";
        for (int i = 0; i < list.size();i++){
            str+=list.get(i)+"\n";
        }
        tricks.setText(str);
    }

}
