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

    // Метод для добавления записи о приеме
    // Метод для добавления консультации
    public boolean addConsultation(int appointmentId, int diseaseId, boolean isOwner) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            String query = "INSERT INTO consultations (appointment_id, disease_id, is_owner) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId); // ID приема
            preparedStatement.setInt(2, diseaseId);    // ID болезни
            preparedStatement.setBoolean(3, isOwner);  // Флаг владельца

            int rowsInserted = preparedStatement.executeUpdate();
            success = rowsInserted > 0;

            if (!success) {
                System.err.println("Не удалось добавить консультацию: " +
                        "appointmentId=" + appointmentId + ", diseaseId=" + diseaseId + ", isOwner=" + isOwner);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }

        return success;
    }


}
