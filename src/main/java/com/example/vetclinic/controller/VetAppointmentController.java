package com.example.vetclinic.controller;

import com.example.vetclinic.module.VetAppointmentSQL;
import javafx.event.ActionEvent;
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

public class VetAppointmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back;

    @FXML
    private Button back1;

    @FXML
    private TextField disease;

    @FXML
    private Label error;

    @FXML
    private TextField number;
    private VetAppointmentSQL recepSQL ;

    public VetAppointmentController(){
        recepSQL = VetAppointmentSQL.getInstance();
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
        if (number.getText().isEmpty() & disease.getText().isEmpty()){
            error.setText("заполните все поля");
        }
        else {
            boolean flag = recepSQL.addAppointment(Integer.parseInt(number.getText()), Integer.parseInt(disease.getText()));
            if (flag){
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
            else {
                error.setText("приема не существует");
            }
        }

    }

    @FXML
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert back1 != null : "fx:id=\"back1\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert disease != null : "fx:id=\"disease\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'vetAppointController.fxml'.";
        assert number != null : "fx:id=\"number\" was not injected: check your FXML file 'vetAppointController.fxml'.";

    }

}
