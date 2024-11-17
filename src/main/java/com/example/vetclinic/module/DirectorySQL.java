package com.example.vetclinic.module;

import java.sql.*;
import java.util.ArrayList;

public class DirectorySQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static DirectorySQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private DirectorySQL() {
    }

    // Синглтон для получения экземпляра класса
    public static synchronized DirectorySQL getInstance() {
        if (instance == null) {
            instance = new DirectorySQL();
        }
        return instance;
    }

    // Метод для получения списка заболеваний по породе
    public ArrayList<String> listDiseasesByBreed(String breedName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> diseasesList = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", // подключение к базе данных petclinic
                    "root", "");

            String query = """
                SELECT d.common_name, d.scientific_name
                FROM diseases d
                JOIN breed_disease bd ON d.id = bd.disease_id
                JOIN breeds b ON b.id = bd.breed_id
                WHERE b.breed_name = ?
                """;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, breedName);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String disease = resultSet.getString("common_name") + " (" +
                        resultSet.getString("scientific_name") + ")";
                diseasesList.add(disease);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
        return diseasesList;
    }
}
