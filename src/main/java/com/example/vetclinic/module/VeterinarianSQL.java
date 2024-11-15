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
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");

            String query = "SELECT в.id AS номер " +
                    "FROM врачи в " +
                    "LEFT JOIN записи пв ON в.id = пв.врач_id " +
                    "AND пв.дата = ? AND пв.время = ? " +
                    "WHERE пв.id IS NULL " +
                    "LIMIT 1";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, time);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("номер");
            } else {
                System.out.println("Свободный врач не найден на указанную дату и время.");
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
    public boolean isUsers(String phone, String password) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT телефон, password FROM врачи Join passwords_doctors USING(id) WHERE телефон = ? AND password = ?";
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

    public boolean addDoctor(String name, String phone, String adres, String password) {
        Connection connection = null;
        boolean success = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "INSERT INTO врачи ( имя, адрес, телефон) VALUES ( ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, adres);
            preparedStatement.setString(3, phone);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) { //добавился ли врач?
                addPassword(phone,password);
                System.out.println("Врач успешно добавлен.");
                return true;
            } else
                System.out.println("нет");

            preparedStatement.close();
            connection.close();
        }catch (ClassNotFoundException e) {
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

    public static int getIdForAdd(String phone) { //просто по телефону
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT id FROM врачи WHERE телефон = ?";
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
            String query = "INSERT INTO passwords_doctors (id, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getIdForAdd(phone)); //метод выше
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
    public String[] getDoctor(String phone) { //
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String[] user = new String[4];

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vetclinic",
                    "root", "");
            String query = "SELECT id, имя, адрес, телефон FROM врачи WHERE телефон = ?";
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
            String query = "UPDATE врачи SET " + fieldName + " = ? WHERE id = ?";
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

            String query = "SELECT записи.id AS id_, записи.дата AS дата, " +
                    "животные.имя AS имя, " +
                    "записи.время AS время_записи " +
                    "FROM записи " +
                    "JOIN животные ON записи.животное_id = животные.id " +
                    "JOIN врачи ON записи.врач_id = врачи.id " +
                    "WHERE врачи.телефон = ? and " +
                    "записи.дата >= ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.setDate(2, Date.valueOf(date));


            resultSet = preparedStatement.executeQuery();

            // Обработка результатов запроса
            while (resultSet.next()) {
                String str = "";
                str+= resultSet.getInt("id_")+ " ";
                str+= resultSet.getString("дата")+ " ";
                str+= resultSet.getString("имя")+" ";
                str+= resultSet.getString("время_записи");



                list.add(str);
                System.out.println("успешно");
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
