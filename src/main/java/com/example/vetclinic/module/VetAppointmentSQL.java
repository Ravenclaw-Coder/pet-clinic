package com.example.vetclinic.module;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VetAppointmentSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static VetAppointmentSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private VetAppointmentSQL() {
    }

    public static synchronized VetAppointmentSQL getInstance() {
        if (instance == null) {
            instance = new VetAppointmentSQL();
        }
        return instance;
    }
    public boolean addRecep(int id, int id_d) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "INSERT INTO приемы (id_записи, id_болезни) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2,id_d );

            // Выполнение запроса
            preparedStatement.executeUpdate();

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            preparedStatement.close();
            connection.close();


        }catch (ClassNotFoundException e) {
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


}
