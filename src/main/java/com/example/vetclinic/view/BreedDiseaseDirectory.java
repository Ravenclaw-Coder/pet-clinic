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
    private Label list;


    @FXML
    private TextField name;

    @FXML
    private Label error;



    @FXML
    void toFind(ActionEvent event) {
        DirectorySQL dir = DirectorySQL.getInstance();
        if (name.getText().isEmpty()) {
            error.setText("Заполните все поля");
            return;
        }

        ArrayList<String> list = dir.listDiseasesByBreed(name.getText());
        if (list.isEmpty()) {
            error.setText("Для указанной породы данные не найдены");
            this.list.setText("");
        } else {
            error.setText("");
            StringBuilder formattedOutput = new StringBuilder();
            formattedOutput.append(String.format("%-25s | %-25s%n", "Common Name", "Scientific Name"));
            formattedOutput.append("------------------------------------------------------------\n");

            for (String disease : list) {
                String[] parts = disease.split(" ", 2); // Разделение на common_name и scientific_name
                formattedOutput.append(String.format("%-25s | %-25s%n", parts[0], parts.length > 1 ? parts[1] : ""));
            }

            this.list.setText(formattedOutput.toString());
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
        assert list != null : "fx:id=\"listt\" was not injected: check your FXML file 'directory.fxml'.";
    }

}
