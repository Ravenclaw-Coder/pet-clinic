package com.example.vetclinic.module;

import java.sql.*;
import java.time.LocalDate;

public class AppointmentSQL {
    private static AppointmentSQL instance;

    private AppointmentSQL() {
    }

    public static synchronized AppointmentSQL getInstance() {
        if (instance == null) {
            instance = new AppointmentSQL();
        }
        return instance;
    }

    // Метод для получения pet_id по имени питомца и номеру телефона владельца
    public int getPetId(String petName, String ownerPhone) {
        Connection connection = null;
        int petId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            String query = "SELECT p.id FROM pets p JOIN owners o ON p.owner_id = o.id WHERE p.name = ? AND o.phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, petName);
            preparedStatement.setString(2, ownerPhone);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                petId = resultSet.getInt("id");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return petId;
    }

    // Метод для добавления записи о приеме
    public boolean addAppointment(String petName, String breed, LocalDate date, String time, String ownerPhone, boolean isOwner) {
        Connection connection = null;
        boolean success = false;

        int petId = getPetId(petName, ownerPhone); // Получаем ID питомца по имени и телефону владельца
        int veterinarianId = getVeterinarianId(); // Получаем ID ветеринара

        // Если питомец не найден, добавляем его в таблицу pets
        if (petId == -1) {
            petId = addPet(petName, breed, ownerPhone);
            if (petId == -1) {
                // Если питомец не добавлен, выводим сообщение, но продолжаем выполнение
                System.err.println("Не удалось добавить питомца или питомец уже есть в базе.");
            }
        }


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Запрос на добавление записи о приеме
            String query = "INSERT INTO appointments (appointment_date, appointment_time, pet_id, veterinarian_id, is_owner) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));  // Устанавливаем дату приема
            preparedStatement.setString(2, time);  // Устанавливаем время приема
            preparedStatement.setInt(3, petId);  // Устанавливаем ID питомца
            preparedStatement.setInt(4, veterinarianId);  // Устанавливаем ID ветеринара
            preparedStatement.setBoolean(5, isOwner); // Устанавливаем значение is_owner

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
                System.out.println("Запись о приеме успешно добавлена!");
            }
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return success;
    }

    // Метод для получения ID ветеринара (пример, зависит от вашей логики)
    private int getVeterinarianId() {
        return 1; // Можно получить ID ветеринара по логике приложения
    }

    // Метод для получения owner_id по номеру телефона владельца
    private int getOwnerId(String ownerPhone) {
        Connection connection = null;
        int ownerId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            String query = "SELECT id FROM owners WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ownerPhone);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ownerId = resultSet.getInt("id");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return ownerId;
    }

    // Метод для добавления питомца в таблицу pets
    private int addPet(String petName, String breed, String ownerPhone) {
        Connection connection = null;
        int petId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Получаем owner_id по номеру телефона владельца
            int ownerId = getOwnerId(ownerPhone);
            if (ownerId == -1) {
                System.err.println("Владелец с таким номером телефона не найден.");
                return -1;  // Если владелец не найден, не добавляем питомца
            }

            // Запрос на добавление питомца в таблицу pets
            String query = "INSERT INTO pets (name, breed, owner_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, petName);
            preparedStatement.setString(2, breed);
            preparedStatement.setInt(3, ownerId);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    petId = generatedKeys.getInt(1);  // Получаем ID добавленного питомца
                }
            }

            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return petId;
    }

}
