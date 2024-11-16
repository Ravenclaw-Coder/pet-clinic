package com.example.vetclinic.view;

import com.example.vetclinic.controller.UserSignInController;
import com.example.vetclinic.module.UserSQL;
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
import java.net.URL;
import java.util.ResourceBundle;

public class UserAccount {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label adres;

    @FXML
    private Button back;

    @FXML
    private Button makeAnAppointment;

    @FXML
    private Button myTricks;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Button cancel;

    @FXML
    private Button toChangeData;

    private UserSQL userSQL;

    public UserAccount(){
        userSQL = UserSQL.getInstance();
    }

    @FXML
    void makeAnAppointment(MouseEvent event) {
        try {
            Parent makeAppointmentRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/makeAnAppointment.fxml"));
            Scene makeAppointmentScene = new Scene(makeAppointmentRoot);
            Stage window = (Stage) makeAnAppointment.getScene().getWindow();
            window.setScene(makeAppointmentScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toCancel(ActionEvent event) {
        try {
            Parent myTricksRoot = FXMLLoader.load(getClass().getResource("cancelUser.fxml"));
            Scene myTricksScene = new Scene(myTricksRoot);
            Stage window = (Stage) myTricks.getScene().getWindow();
            window.setScene(myTricksScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void myTricks(MouseEvent event) {
        try {
            Parent myTricksRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/view/userAppointment.fxml"));
            Scene myTricksScene = new Scene(myTricksRoot);
            Stage window = (Stage) myTricks.getScene().getWindow();
            window.setScene(myTricksScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toBack(MouseEvent event) {
        try {
            Parent userLoginRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/userLogin.fxml"));
            Scene userLoginScene = new Scene(userLoginRoot);
            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(userLoginScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toChangeData(MouseEvent event) {
        try {
            Parent changeUserRoot = FXMLLoader.load(getClass().getResource("/com/example/vetclinic/controller/changeUser.fxml"));
            Scene changeUserScene = new Scene(changeUserRoot);
            Stage window = (Stage) toChangeData.getScene().getWindow();
            window.setScene(changeUserScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert adres != null : "fx:id=\"adres\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert makeAnAppointment != null : "fx:id=\"makeAnAppointment\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert myTricks != null : "fx:id=\"myTricks\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert phone != null : "fx:id=\"phone\" was not injected: check your FXML file 'userAccount.fxml'.";
        assert toChangeData != null : "fx:id=\"toChangeData\" was not injected: check your FXML file 'userAccount.fxml'.";

        phone.setText(UserSignInController.getLogin());  // Используем getLogin() из UserSignInController
        String[] users = userSQL.getUser(UserSignInController.getLogin());
        name.setText(users[1]);
        adres.setText(users[2]);
    }
}
