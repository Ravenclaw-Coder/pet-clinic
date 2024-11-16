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
            String query = "SELECT u.id " +
                    "FROM users u " +
                    "JOIN owners o ON u.id = o.id " +
                    "JOIN roles r ON u.role_id = r.id " +
                    "WHERE o.phone = ? AND u.password = ? AND r.role_name = 'Owner'";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);

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

            // Вставка данных в таблицу owners
            String query = "INSERT INTO owners (name, phone, address) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, address);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                if (keys.next()) {
                    int ownerId = keys.getInt(1);

                    // Добавляем пользователя в таблицу users
                    String userQuery = "INSERT INTO users (id, username, password, role_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement userStmt = connection.prepareStatement(userQuery);
                    userStmt.setInt(1, ownerId);
                    userStmt.setString(2, name); // username совпадает с именем
                    userStmt.setString(3, password);
                    userStmt.setInt(4, getRoleId("Owner")); // Получаем роль "owner"
                    userStmt.executeUpdate();
                    userStmt.close();

                    System.out.println("Новый владелец и пользователь успешно добавлены!");
                    return true;
                }
            } else {
                System.out.println("Не удалось добавить владельца.");
            }

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

    public boolean updatePhone(int idClient, String newPhone) {
        return updateClientField(idClient, "phone", newPhone);
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

    public ArrayList<String> listAppointments(String phone, LocalDate date) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> list = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            String query = "SELECT appointments.id AS id_, appointments.date AS date, " +
                    "doctors.name AS doctor_name, appointments.time AS appointment_time " +
                    "FROM appointments " +
                    "JOIN pets ON appointments.pet_id = pets.id " +
                    "JOIN doctors ON appointments.doctor_id = doctors.id " +
                    "WHERE pets.owner_id = ? AND appointments.date >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getId(phone));
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String str = resultSet.getInt("id_") + " " +
                        resultSet.getString("date") + " " +
                        resultSet.getString("doctor_name") + " " +
                        resultSet.getString("appointment_time");
                list.add(str);
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
        return list;
    }
}
