
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
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        boolean success = false;

        try {
            // Создаем соединение с базой данных
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Отключаем авто-commit для управления транзакциями
            connection.setAutoCommit(false);

            // Шаг 1: Добавляем запись в consultations
            String query1 = "INSERT INTO consultations (appointment_id, disease_id, is_owner) VALUES (?, ?, ?)";
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setInt(1, appointmentId); // ID приема
            preparedStatement1.setInt(2, diseaseId);    // ID болезни
            preparedStatement1.setBoolean(3, isOwner);  // Флаг владельца

            int rowsInserted = preparedStatement1.executeUpdate();

            if (rowsInserted <= 0) {
                System.err.println("Ошибка при добавлении консультации: не удалось добавить запись в consultations.");
                connection.rollback(); // Откатываем транзакцию
                return false;
            }
            System.out.println("Консультация успешно добавлена для приема с ID " + appointmentId);

            // Шаг 2: Помечаем прием как завершенный
            String query2 = "UPDATE appointments SET is_completed = TRUE WHERE id = ?";
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, appointmentId); // Устанавливаем ID приема для обновления

            int rowsUpdated = preparedStatement2.executeUpdate();

            if (rowsUpdated <= 0) {
                System.err.println("Ошибка при завершении приема с ID: " + appointmentId);
                connection.rollback(); // Откатываем транзакцию
                return false;
            }
            System.out.println("Прием с ID " + appointmentId + " успешно помечен как завершенный.");

            // Если обе операции прошли успешно, подтверждаем транзакцию
            connection.commit();
            success = true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Откатываем транзакцию в случае ошибки
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Ошибка при откате транзакции: " + rollbackEx.getMessage());
            }
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement1 != null) preparedStatement1.close();
                if (preparedStatement2 != null) preparedStatement2.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }

        return success;
    }

    public boolean markAppointmentAsCompleted(int appointmentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            // Устанавливаем соединение с базой данных
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Обновляем запись: отмечаем прием как завершенный
            String query = "UPDATE appointments SET is_completed = TRUE WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                success = true;
                System.out.println("Прием с ID " + appointmentId + " успешно помечен как завершенный.");
            } else {
                System.err.println("Ошибка: прием с ID " + appointmentId + " не найден.");
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