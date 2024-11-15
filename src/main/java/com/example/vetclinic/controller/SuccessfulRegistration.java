package com.example.vetclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SuccessfulRegistration {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button inUserAccount;

    @FXML
    void inUserAccount(MouseEvent event) {
        try {
            Parent userAccountRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAccount.fxml"));
            Scene userAccountScene = new Scene(userAccountRoot);
            Stage window = (Stage) inUserAccount.getScene().getWindow();
            window.setScene(userAccountScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert inUserAccount != null : "fx:id=\"inUserAccount\" was not injected: check your FXML file 'successfulRegistration.fxml'.";

    }

}
