package com.example.vetclinic.module;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static UserSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private UserSQL() {
    }

    // Публичный статический метод для получения единственного экземпляра класса
    public static synchronized UserSQL getInstance() {
        if (instance == null) {
            instance = new UserSQL();
        }
        return instance;
    }

    public boolean isUsers(String phone, String password) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // Исправленный запрос
            String query = "SELECT u.username, u.password " +
                    "FROM users u " +
                    "JOIN roles r ON u.role_id = r.id " +
                    "JOIN owners o ON o.phone = u.username " +
                    "WHERE u.password = ? AND r.role_name = 'Owner' AND o.phone = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Owner found with phone: " + phone);
                return true;
            } else {
                System.out.println("Owner not found.");
            }

            resultSet.close();
            preparedStatement.close();
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
        return false;
    }


    public boolean addUser(String name, String phone, String address, String password) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            connection.setAutoCommit(false); // Начало транзакции

            // Сначала добавляем запись в таблицу users
            String queryUser = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatementUser = connection.prepareStatement(queryUser);
            preparedStatementUser.setString(1, phone); // username совпадает с phone
            preparedStatementUser.setString(2, password);
            preparedStatementUser.setInt(3, getRoleId("Owner")); // Получение ID роли "Owner"
            int rowsInsertedUser = preparedStatementUser.executeUpdate();

            if (rowsInsertedUser > 0) {
                // Затем добавляем запись в таблицу owners
                String queryOwner = "INSERT INTO owners (name, phone, address) VALUES (?, ?, ?)";
                PreparedStatement preparedStatementOwner = connection.prepareStatement(queryOwner);
                preparedStatementOwner.setString(1, name);
                preparedStatementOwner.setString(2, phone); // phone должен совпадать с username
                preparedStatementOwner.setString(3, address);
                int rowsInsertedOwner = preparedStatementOwner.executeUpdate();

                if (rowsInsertedOwner > 0) {
                    connection.commit(); // Фиксация транзакции
                    System.out.println("Пользователь успешно добавлен.");
                    return true;
                }
            }

            connection.rollback(); // Откат, если добавление не удалось
            System.out.println("Не удалось добавить пользователя или владельца.");
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Откат изменений при ошибке
                } catch (SQLException rollbackEx) {
                    System.err.println("Ошибка отката транзакции: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return false;
    }



    // Метод для получения ID роли
    private int getRoleId(String roleName) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/petclinic", "root", "");
        String query = "SELECT id FROM roles WHERE role_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, roleName);

        ResultSet resultSet = preparedStatement.executeQuery();
        int roleId = -1;
        if (resultSet.next()) {
            roleId = resultSet.getInt("id");
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return roleId;
    }


    public static int getId(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "SELECT id FROM owners WHERE phone = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
            } else {
                System.out.println("Владелец с таким номером телефона не найден.");
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

    public boolean addPassword(String phone, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            // Устанавливаем соединение с базой данных
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // SQL-запрос для обновления пароля в таблице users
            String query = "UPDATE users " +
                    "SET password = ? " +
                    "WHERE id = (SELECT id FROM owners WHERE phone = ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password); // Устанавливаем новый пароль
            preparedStatement.setString(2, phone);   // Используем телефон для поиска пользователя

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
                System.out.println("Пароль успешно добавлен для пользователя с телефоном " + phone);
            } else {
                System.out.println("Не удалось обновить пароль. Пользователь с таким телефоном не найден.");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
        return success;
    }


    public String[] getUser(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String[] user = new String[4];

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "SELECT id, name, address, phone FROM owners WHERE phone = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user[0] = resultSet.getString("id");
                user[1] = resultSet.getString("name");
                user[2] = resultSet.getString("address");
                user[3] = resultSet.getString("phone");
            } else {
                System.out.println("Владелец с таким номером телефона не найден.");
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
        return user;
    }

    public boolean updateName(int idClient, String newName) {
        return updateClientField(idClient, "name", newName);
    }



    public boolean updateAddress(int idClient, String newAddress) {
        return updateClientField(idClient, "address", newAddress);
    }

    private boolean updateClientField(int idClient, String fieldName, String newValue) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "UPDATE owners SET " + fieldName + " = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, idClient);
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

    public ArrayList<Appointment> listAppointments(String phone, LocalDate date) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Appointment> appointments = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            String query =  "SELECT appointments.id AS appointment_id, appointments.appointment_date AS appointment_date, " +
                    "veterinarians.name AS veterinarian_name, appointments.appointment_time AS appointment_time " +
                    "FROM appointments " +
                    "JOIN pets ON appointments.pet_id = pets.id " +
                    "JOIN veterinarians ON appointments.veterinarian_id = veterinarians.id " +
                    "WHERE pets.owner_id = ? AND appointments.appointment_date >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getId(phone));
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();

            // Логирование результата
            if (!resultSet.next()) {
                System.out.println("Нет данных по запросу");
            } else {
                do {
                    Appointment appointment = new Appointment(
                            resultSet.getInt("appointment_id"),
                            resultSet.getString("appointment_date"),
                            resultSet.getString("veterinarian_name"),
                            resultSet.getString("appointment_time")
                    );
                    appointments.add(appointment);
                } while (resultSet.next());
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
        return appointments;
    }
}
