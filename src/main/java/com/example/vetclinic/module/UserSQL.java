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
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT телефон, password FROM владельцы Join passwords USING(id) WHERE телефон = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String ph = resultSet.getString("телефон");
                String pas = resultSet.getString("password");
                System.out.println("good");
                return true;
                //System.out.println("Найден владелец: телефон = " + найденныйТелефон + ", пароль = " + найденныйПароль);
            }
            else
                System.out.println("not");

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
        return false;
    }

    public boolean addUser(String name, String phone, String adres, String password) {
        Connection connection = null;
        boolean success = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "INSERT INTO владельцы (имя, телефон, адрес) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, adres);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                addPassword(phone,password);
                System.out.println("Новый владелец успешно добавлен!");
                return true;
            }
            else
                System.out.println("нет");

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

    public static int getId(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT id FROM владельцы WHERE телефон = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("id");
                //System.out.println("ID владельца: " + id);
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

    public static boolean addPassword(String phone, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "INSERT INTO passwords (id, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getId(phone));
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
                System.out.println("Пароль успешно добавлен для телефона " + phone);
            } else {
                System.out.println("Не удалось добавить пароль для телефона " + phone +
                        ". Пользователь с таким телефоном не найден.");
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

    public String[] getUser(String phone) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String[] user = new String[4];

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT id, имя, адрес, телефон FROM владельцы WHERE телефон = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user[0] = resultSet.getString("id");
                user[1] = resultSet.getString("имя");
                user[2] = resultSet.getString("адрес");
                user[3] = resultSet.getString("телефон");

                //System.out.println("Имя владельца: " + имя);
                //System.out.println("Адрес владельца: " + адрес);
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
        return updateClientField(idClient, "имя", newName);
    }
    public boolean updatePhone(int idClient, String newPhone) {
        return updateClientField(idClient, "телефон", newPhone);
    }


    public boolean updateAdress(int idClient, String newAdres) {
        return updateClientField(idClient, "адрес", newAdres);
    }

    private boolean updateClientField(int idClient, String fieldName, String newValue) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "UPDATE владельцы SET " + fieldName + " = ? WHERE id = ?";
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
    public ArrayList<String> listAppointments(String phone, LocalDate date){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> list = new ArrayList<String>();


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");

            Statement statement = connection.createStatement();

            // Подготовка запроса
            String query = //"SELECT записи.дата AS дата, " +
                    "SELECT записи.id AS id_,записи.дата AS дата, "+
            "врачи.имя AS имя_врача, " +
                    "записи.время AS время_записи " +
                    "FROM записи " +
                    "JOIN животные ON записи.животное_id = животные.id " +
                    "JOIN врачи ON записи.врач_id = врачи.id " +
                    "WHERE животные.владелец_id = ? and " +
                    "записи.дата >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getId(phone));
            preparedStatement.setDate(2, Date.valueOf(date));


            resultSet = preparedStatement.executeQuery();

            // Обработка результатов запроса
            while (resultSet.next()) {

                String str = "";
                str+= resultSet.getInt("id_")+ " ";
                str+= resultSet.getString("дата")+ " ";
                str+= resultSet.getString("имя_врача")+" ";
                str+= resultSet.getString("время_записи");

                list.add(str);
                // Вывод результатов на консоль (можно заменить на вашу логику обработки)
            }

            // Закрытие ресурсов
            resultSet.close();
            preparedStatement.close();
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
        return list;
    }

}