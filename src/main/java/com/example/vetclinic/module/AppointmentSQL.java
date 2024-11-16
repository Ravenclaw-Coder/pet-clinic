package com.example.vetclinic.module;

import java.sql.*;
import java.time.LocalDate;

public class AppointmentSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static AppointmentSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private AppointmentSQL() {
    }

    // Синглтон для получения экземпляра класса
    public static synchronized AppointmentSQL getInstance() {
        if (instance == null) {
            instance = new AppointmentSQL();
        }
        return instance;
    }

    // Метод для добавления записи о приеме
    public boolean addAppointment(String name, LocalDate date, String time, String phone) {
        Connection connection = null;
        boolean success = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",  // подключение к базе данных petclinic
                    "root", "");

            // Запрос на вставку записи в таблицу "appointments"
            String query = "INSERT INTO appointments (appointment_date, appointment_time, pet_id, veterinarian_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, time);

            // Получаем id животного по телефону владельца и имени питомца
            preparedStatement.setInt(3, PetSQL.getId(phone, name));

            // Проверяем доступность врача на указанное время
            if (VeterinarianSQL.getId(date, time) != -1) {
                preparedStatement.setInt(4, VeterinarianSQL.getId(date, time));

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Прием с владельцем успешно добавлен!");
                    success = true;
                }
            } else {
                System.out.println("Врач на это время занят.");
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

    // Метод для удаления записи о приеме
    public boolean deleteAppointment(int id_app) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",  // подключение к базе данных petclinic
                    "root", "");

            // Запрос на удаление записи из таблицы "appointments"
            String deleteQuery = "DELETE FROM appointments WHERE id = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id_app);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Прием успешно удален!");
                return true;
            } else {
                System.out.println("Запись не найдена.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
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
        return false;
    }
}
