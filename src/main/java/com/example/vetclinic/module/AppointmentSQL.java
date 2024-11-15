package com.example.vetclinic.module;

import java.sql.*;
import java.time.LocalDate;

public class AppointmentSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static AppointmentSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private AppointmentSQL() {
    }
    public static synchronized AppointmentSQL getInstance() {
        if (instance == null) {
            instance = new AppointmentSQL();
        }
        return instance;
    }

    public boolean addAppointment(String name, LocalDate date, String time, String phone) {
        Connection connection = null;
        // UserSQL userSQL = UserSQL.getInstance();
        boolean success = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "INSERT INTO записи (дата, время, животное_id, врач_id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, time);
            preparedStatement.setInt(3, PetSQL.getId(phone,name));
            //preparedStatement.setInt(4, UserSQL.getId(phone) );
            if (VeterinarianSQL.getId(date,time)!= -1) {
                preparedStatement.setInt(4, VeterinarianSQL.getId(date, time));

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Приём с владельцем успешно добавлен!");
                }
                return true;
            }
            else {
                System.out.println("занят");
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

    /*public boolean isOcupiedForUser(String phone,LocalDate date, String time){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Integer> bookings = new ArrayList<Integer>();


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "Kvashnina", "-bL*)jxbvjMg.NVG");

            Statement statement = connection.createStatement();

            String query = "SELECT id" +
                    "FROM записи" +
                    "WHERE владелец_id = ? " +
                    "AND дата = ? " +
                    "AND время = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, UserSQL.getId(phone));
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setString(3, time);

            resultSet = preparedStatement.executeQuery();

            // Проверка результатов запроса
            if (resultSet.next()) {
                return true;
            }


            // Закрываем ресурсы
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                // Закрываем соединение в блоке finally для обеспечения его закрытия в любом случае
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return false;
    }
    /*public int isOcupiedForUser(int id_n){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "Kvashnina", "-bL*)jxbvjMg.NVG");

            Statement statement = connection.createStatement();

            String query = "SELECT id" +
                    "FROM записи" +
                    "WHERE id = ? ";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_n);


            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
                //System.out.println("ID владельца: " + id);
            } else {
                System.out.println("не найден.");
            }


            // Закрываем ресурсы
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        } finally {
            try {
                // Закрываем соединение в блоке finally для обеспечения его закрытия в любом случае
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return id;
    }*/

    public boolean deleteAppointment(int id_app) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");

            String deleteQuery = "DELETE FROM записи WHERE id = ?";

            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id_app);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("ошиюбка");
            }

        }
        catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер JDBC: " + e.getMessage());
        }
        catch (SQLException e) {
            System.err.println("Ошибка при выполнении SQL-запроса: " + e.getMessage());
        }
        finally {
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

