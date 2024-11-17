package com.example.vetclinic.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cancel {

    private static final String URL = "jdbc:mysql://localhost:3306/petclinic";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public boolean cancelAppointment(int appointmentId) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "DELETE FROM appointments WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0; // Возвращаем true, если запись была удалена
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // Возвращаем false, если запись не была удалена
    }
}
