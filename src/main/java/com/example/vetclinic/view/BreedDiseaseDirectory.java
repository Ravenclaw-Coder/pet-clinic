package com.example.vetclinic.view;

import com.example.vetclinic.module.DirectorySQL;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
public class BreedDiseaseDirectory {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back;

    @FXML
    private Button btnFind;
    @FXML
    private Label listt;


    @FXML
    private TextField name;

    @FXML
    private Label error;



    @FXML
    void toFind(ActionEvent event) {
        DirectorySQL dir = DirectorySQL.getInstance();
        if (name.getText().isEmpty()){
            error.setText("Заполните все поля");
        }
        else {
            ArrayList<String> list = dir.listAppointments(name.getText());
            String str = new String();
            for (int i = 0; i<list.size();i++){
                str+=list.get(i)+"\n";
            }
            listt.setText(str);

        }

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
    void initialize() {
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'directory.fxml'.";
        assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'directory.fxml'.";
        assert btnFind != null : "fx:id=\"btnFind\" was not injected: check your FXML file 'directory.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'directory.fxml'.";
        assert error != null : "fx:id=\"error\" was not injected: check your FXML file 'directory.fxml'.";
        assert listt != null : "fx:id=\"listt\" was not injected: check your FXML file 'directory.fxml'.";
    }

}
