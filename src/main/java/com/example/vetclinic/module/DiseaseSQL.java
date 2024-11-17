package com.example.vetclinic.module;

import java.sql.*;
import java.util.ArrayList;

public class DiseaseSQL {

    private static DiseaseSQL instance;

    private DiseaseSQL() {
    }

    public static synchronized DiseaseSQL getInstance() {
        if (instance == null) {
            instance = new DiseaseSQL();
        }
        return instance;
    }

    public ArrayList<Disease> getAllDiseases() {
        ArrayList<Disease> diseases = new ArrayList<>();
        String query = "SELECT id, common_name, scientific_name FROM diseases";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                diseases.add(new Disease(
                        resultSet.getInt("id"),
                        resultSet.getString("common_name"),
                        resultSet.getString("scientific_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return diseases;
    }
}
