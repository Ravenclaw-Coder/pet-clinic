package com.example.vetclinic.view;

import com.example.vetclinic.controller.VetSignInController;
import com.example.vetclinic.module.VeterinarianSQL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class VeterinarianAccount {

    @FXML
    private Label address;

    @FXML
    private Button back;

    @FXML
    private Button directory;

    @FXML
    private Button myTricks;

    @FXML
    private Label name;

    @FXML
    private Label number;

    @FXML
    private Button reception;

    @FXML
    private Button cancel;

    @FXML
    private Button toChangeData;

    private VeterinarianSQL doctorSQL;

    public VeterinarianAccount(){
        doctorSQL = VeterinarianSQL.getInstance();
    }

    @FXML
    void directory(MouseEvent event) {
        try {
            Parent directoryRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/directory.fxml"));
            Scene directoryScene = new Scene(directoryRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(directoryScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void myTricks(MouseEvent event) {
        try {
            Parent tricksRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/vetAppointment.fxml"));
            Scene tricksScene = new Scene(tricksRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(tricksScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toCancel(ActionEvent event) {
        try {
            Parent tricksRoot = FXMLLoader.load(getClass().getResource("cancel.fxml"));
            Scene tricksScene = new Scene(tricksRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(tricksScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/vetSignIn.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toChangeData(MouseEvent event) {
        try {
            Parent changeDataRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/changeVet.fxml"));
            Scene changeDataScene = new Scene(changeDataRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(changeDataScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toRecep(ActionEvent event) {
        try {
            Parent recepRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/vetAppointController.fxml"));
            Scene recepScene = new Scene(recepRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(recepScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert address != null : "fx:id=\"address\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert directory != null : "fx:id=\"directory\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert myTricks != null : "fx:id=\"myTricks\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert number != null : "fx:id=\"number\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert reception != null : "fx:id=\"reception\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert toChangeData != null : "fx:id=\"toChangeData\" was not injected: check your FXML file 'vetAccount.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'vetAccount.fxml'.";

        number.setText(VetSignInController.getLogin());  // Используем getLogin() из VetSignInController
        String[] users = doctorSQL.getDoctor(VetSignInController.getLogin());
        name.setText(users[1]);
        address.setText(users[2]);
    }
}
