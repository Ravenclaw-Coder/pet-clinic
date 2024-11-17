package com.example.vetclinic.module;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VeterinarianSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static VeterinarianSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private VeterinarianSQL() {
    }

    public static synchronized VeterinarianSQL getInstance() {
        if (instance == null) {
            instance = new VeterinarianSQL();
        }
        return instance; //синг
    }

    public static int getId(LocalDate date, String time) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "SELECT v.id AS id_ " +
                    "FROM veterinarians v " +
                    "LEFT JOIN appointments a ON v.id = a.veterinarian_id " +
                    "AND a.date = ? AND a.time = ? " +
                    "WHERE a.id IS NULL " +
                    "LIMIT 1";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, time);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id_");
            } else {
                System.out.println("Свободный ветеринар не найден на указанную дату и время.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return id;
    }


    public boolean isUsers(String phone, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            String query = "SELECT u.username, u.password " +
                    "FROM users u " +
                    "JOIN roles r ON u.role_id = r.id " +
                    "JOIN veterinarians v ON v.phone = u.username " +
                    "WHERE u.password = ? AND r.role_name = 'Veterinarian' AND v.phone = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            } else {
                System.out.println("Пользователь с такими данными не найден или не является ветеринаром.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return false;
    }


    public boolean addDoctor(String name, String phone, String address, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            connection.setAutoCommit(false); // Отключаем автокоммит для транзакции

            // Добавление в таблицу users
            String queryUser = "INSERT INTO users (username, password, role_id) VALUES (?, ?, " +
                    "(SELECT id FROM roles WHERE role_name = 'Veterinarian'))";

            PreparedStatement preparedStatementUser = connection.prepareStatement(queryUser);
            preparedStatementUser.setString(1, phone); // Username — номер телефона
            preparedStatementUser.setString(2, password); // Пароль
            preparedStatementUser.executeUpdate();

            // Добавление в таблицу veterinarians
            String queryVet = "INSERT INTO veterinarians (name, phone, address) VALUES (?, ?, ?)";
            PreparedStatement preparedStatementVet = connection.prepareStatement(queryVet);
            preparedStatementVet.setString(1, name);
            preparedStatementVet.setString(2, phone);
            preparedStatementVet.setString(3, address);
            preparedStatementVet.executeUpdate();

            connection.commit(); // Фиксируем транзакцию
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Откат в случае ошибки
                } catch (SQLException rollbackEx) {
                    System.err.println("Ошибка отката транзакции: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Ошибка при добавлении ветеринара: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return false;
    }


    //getIdForAdd
    public static int getVeterinarianId(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // Запрос для получения ID ветеринара по номеру телефона
            String query = "SELECT id FROM veterinarians WHERE phone = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            } else {
                System.out.println("Ветеринар с таким номером телефона не найден.");
            }

            resultSet.close();
            preparedStatement.close();
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


    public boolean addPassword(String phone, String newPassword) {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Обновляем пароль для пользователя с данным телефоном (username)
            String updatePasswordQuery = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updatePasswordQuery);
            preparedStatement.setString(1, newPassword); // Новый пароль
            preparedStatement.setString(2, phone);        // Телефон как имя пользователя

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
                System.out.println("Пароль успешно обновлен для пользователя: " + phone);
            } else {
                System.out.println("Не удалось обновить пароль.");
            }

            preparedStatement.close();
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



    public String[] getVet(String phone) {
        Connection connection = null;
        String[] doctor = new String[4];

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "SELECT id, name, phone, address FROM veterinarians WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                doctor[0] = resultSet.getString("id");
                doctor[1] = resultSet.getString("name");
                doctor[2] = resultSet.getString("phone");
                doctor[3] = resultSet.getString("address");


            } else {
                System.out.println("Ветеринар с таким номером телефона не найден.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return doctor;
    }


    // Методы для обновления информации о враче
    public boolean updateName(int doctorId, String newName) {
        return updateDoctorField(doctorId, "name", newName);
    }



    public boolean updateAddress(int doctorId, String newAddress) {
        return updateDoctorField(doctorId, "address", newAddress);
    }

    private boolean updateDoctorField(int doctorId, String fieldName, String newValue) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "UPDATE veterinarians SET " + fieldName + " = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, doctorId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
        return success;
    }

    public ObservableList<Appointment> listAppointments(String phone, LocalDate date) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            String query = "SELECT appointments.id AS appointment_id, appointments.appointment_date AS appointment_date, " +
                    "pets.name AS pet_name, appointments.appointment_time AS appointment_time, " +
                    "appointments.is_completed AS is_completed " +
                    "FROM appointments " +
                    "JOIN pets ON appointments.pet_id = pets.id " +
                    "JOIN veterinarians ON appointments.veterinarian_id = veterinarians.id " +
                    "WHERE veterinarians.phone = ? AND appointments.appointment_date >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("appointment_id");
                String appointmentDate = resultSet.getString("appointment_date");
                String petName = resultSet.getString("pet_name");
                String appointmentTime = resultSet.getString("appointment_time");
                boolean isCompleted = resultSet.getBoolean("is_completed");

                // Создаем объект Appointment и добавляем в список
                appointments.add(new Appointment(appointmentId, appointmentDate, petName, appointmentTime, isCompleted));
            }

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

        return appointments;
    }

}
