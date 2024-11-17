package com.example.vetclinic.view;

import com.example.vetclinic.module.Disease;
import com.example.vetclinic.module.DiseaseSQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DiseaseViewController {

    @FXML
    private TableView<Disease> diseaseTable;

    @FXML
    private TableColumn<Disease, Integer> idColumn;

    @FXML
    private TableColumn<Disease, String> commonNameColumn;

    @FXML
    private TableColumn<Disease, String> scientificNameColumn;

    @FXML
    private javafx.scene.control.Button closeButton;

    private final DiseaseSQL diseaseSQL = DiseaseSQL.getInstance();

    @FXML
    void initialize() {
        // Настройка колонок таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        commonNameColumn.setCellValueFactory(new PropertyValueFactory<>("commonName"));
        scientificNameColumn.setCellValueFactory(new PropertyValueFactory<>("scientificName"));

        // Заполнение данных из базы
        ObservableList<Disease> diseases = FXCollections.observableArrayList(diseaseSQL.getAllDiseases());
        diseaseTable.setItems(diseases);
    }

    @FXML
    void onClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
