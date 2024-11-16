package com.example.vetclinic.module;

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

            // Добавление в таблицу veterinarians
            String queryVet = "INSERT INTO veterinarians (name, phone, address) VALUES (?, ?, ?)";
            PreparedStatement preparedStatementVet = connection.prepareStatement(queryVet, Statement.RETURN_GENERATED_KEYS);
            preparedStatementVet.setString(1, name);
            preparedStatementVet.setString(2, phone);
            preparedStatementVet.setString(3, address);
            preparedStatementVet.executeUpdate();

            // Получаем ID вставленного ветеринара
            ResultSet generatedKeys = preparedStatementVet.getGeneratedKeys();
            if (!generatedKeys.next()) throw new SQLException("Не удалось получить ID ветеринара.");
            int vetId = generatedKeys.getInt(1);

            // Добавление в таблицу users
            String queryUser = "INSERT INTO users (username, password, role_id) VALUES (?, ?, " +
                    "(SELECT id FROM roles WHERE role_name = 'Veterinarian'))";
            PreparedStatement preparedStatementUser = connection.prepareStatement(queryUser);
            preparedStatementUser.setString(1, phone); // Username — номер телефона
            preparedStatementUser.setString(2, password); // Пароль
            preparedStatementUser.executeUpdate();

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

    //вместо addpassword
    public boolean addUser(String phone, String password) {
        Connection connection = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");

            // Получаем ID роли "veterinarian" из таблицы roles
            String getRoleIdQuery = "SELECT id FROM roles WHERE role_name = ?";
            PreparedStatement roleStmt = connection.prepareStatement(getRoleIdQuery);
            roleStmt.setString(1, "veterinarian");
            ResultSet roleResult = roleStmt.executeQuery();

            int roleId = -1;
            if (roleResult.next()) {
                roleId = roleResult.getInt("id");
            } else {
                System.out.println("Роль 'veterinarian' не найдена.");
                return false;
            }

            // Вставляем пользователя в таблицу users
            String query = "INSERT INTO users (username, password, role_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone); // Используем телефон как имя пользователя
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, roleId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
                System.out.println("Пользователь успешно добавлен: " + phone);
            } else {
                System.out.println("Не удалось добавить пользователя.");
            }

            roleStmt.close();
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


    public String[] getDoctor(String phone) {
        Connection connection = null;
        String[] doctor = new String[4];

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic",
                    "root", "");
            String query = "SELECT v.id, v.name, v.address, v.phone " +
                    "FROM veterinarians v WHERE v.phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                doctor[0] = resultSet.getString("id");
                doctor[1] = resultSet.getString("name");
                doctor[2] = resultSet.getString("address");
                doctor[3] = resultSet.getString("phone");
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

    public boolean updatePhone(int doctorId, String newPhone) {
        return updateDoctorField(doctorId, "phone", newPhone);
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
            String query = "UPDATE doctors SET " + fieldName + " = ? WHERE id = ?";
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

            String query = "SELECT appointments.id AS appointment_id, appointments.date AS appointment_date, " +
                    "pets.name AS pet_name, appointments.time AS appointment_time " +
                    "FROM appointments " +
                    "JOIN pets ON appointments.pet_id = pets.id " +
                    "JOIN doctors ON appointments.doctor_id = doctors.id " +
                    "WHERE doctors.phone = ? AND appointments.date >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setDate(2, Date.valueOf(date));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String appointment = resultSet.getInt("appointment_id") + " " +
                        resultSet.getString("appointment_date") + " " +
                        resultSet.getString("pet_name") + " " +
                        resultSet.getString("appointment_time");
                list.add(appointment);
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
