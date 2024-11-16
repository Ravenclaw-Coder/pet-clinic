package com.example.vetclinic.controller;

import com.example.vetclinic.module.VeterinarianSQL;
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

public class ChangeVeterinarian {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField adres;

    @FXML
    private Button back;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private Label phone;

    @FXML
    private Button save;
    private String[] user;
    private VeterinarianSQL doctorSQL;
    public ChangeVeterinarian(){
        doctorSQL = VeterinarianSQL.getInstance();
        user = doctorSQL.getDoctor(VetSignInController.getLogin()); //подаем номер телефона, для получнения статик массив
    }

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
    void toSave(MouseEvent event) {
        if (doctorSQL.updateName(Integer.parseInt(user[0]), name.getText()) &
                doctorSQL.updateAddress(Integer.parseInt(user[0]), adres.getText())) {
            try {
                Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/vetAccount.fxml"));
                Scene userAccountScene = new Scene(userAccountRoot);
                Stage window = (Stage) back.getScene().getWindow();
                window.setScene(userAccountScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        assert adres != null : "fx:id=\"adres\" was not injected: check your FXML file 'changeVet.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'changeVet.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'changeVet.fxml'.";
        assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'changeVet.fxml'.";
        assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'changeVet.fxml'.";
        assert save != null : "fx:id=\"save\" was not injected: check your FXML file 'changeVet.fxml'.";
        phone.setText(VetSignInController.getLogin());
        name.setText(user[1]);
        adres.setText(user[2]);
    }

}
