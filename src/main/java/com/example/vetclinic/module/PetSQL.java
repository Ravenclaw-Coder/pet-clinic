package com.example.vetclinic.module;

import java.sql.*;

public class PetSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static PetSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private PetSQL() {
    }

    public static synchronized PetSQL getInstance() {
        if (instance == null) {
            instance = new PetSQL();
        }
        return instance;
    }

    // Метод для добавления информации о животном
    public boolean addPet(String name, String breed, String status, String phone) {
        Connection connection = null;
        boolean success = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // Изменено на таблицы и поля базы данных petclinic
            String query = "INSERT INTO pets (name, breed, status, owner_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, breed);
            preparedStatement.setString(3, status);
            preparedStatement.setInt(4, UserSQL.getId(phone));  // Получаем ID владельца по номеру телефона

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Запись о животном с владельцем успешно добавлена!");
                success = true;
            } else {
                System.out.println("Не удалось добавить животное.");
            }

            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }

        return success;
    }

    // Метод для получения ID животного по имени и номеру телефона владельца
    public static int getId(String phone, String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // Изменено на таблицы и поля базы данных petclinic
            String query = "SELECT id FROM pets WHERE name = ? AND owner_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, UserSQL.getId(phone));  // Получаем ID владельца по номеру телефона

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }

        return id;
    }
}
