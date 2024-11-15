package com.example.vetclinic;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

public class Start {

    private static final Logger LOGGER = Logger.getLogger(Start.class.getName());

    @FXML
    private Button ButtonDoctor;

    @FXML
    private Button ButtonUser;

    @FXML
    void ClickVeterinarian(MouseEvent event) {
        LOGGER.info("Нажата кнопка ветеринара. Переход к сцене входа ветеринара.");
        SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/vetLogin.fxml");
    }

    @FXML
    void ClickUser(MouseEvent event) {
        LOGGER.info("Нажата кнопка пользователя. Переход к сцене входа пользователя.");
        SceneManager.getInstance().switchScene("/com/example/vetclinic/controller/userLogin.fxml");
    }

    @FXML
    void initialize() {
        assert ButtonDoctor != null : "fx:id=\"ButtonDoctor\" was not injected: check your FXML file 'start.fxml'.";
        assert ButtonUser != null : "fx:id=\"ButtonUser\" was not injected: check your FXML file 'start.fxml'.";
    }
}
