module com.example.vetclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vetclinic to javafx.fxml;
    exports com.example.vetclinic;
    exports com.example.vetclinic.module;
    opens com.example.vetclinic.module to javafx.fxml;
    exports com.example.vetclinic.controller;
    opens com.example.vetclinic.controller to javafx.fxml;
    exports com.example.vetclinic.view;
    opens com.example.vetclinic.view to javafx.fxml;


}