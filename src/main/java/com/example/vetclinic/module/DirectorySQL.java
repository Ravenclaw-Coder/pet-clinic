package com.example.vetclinic.module;

import java.sql.*;
import java.util.ArrayList;

public class DirectorySQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static DirectorySQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private DirectorySQL() {
    }

    public static synchronized DirectorySQL getInstance() {
        if (instance == null) {
            instance = new DirectorySQL();
        }
        return instance;
    }
    public ArrayList<String> listAppointments(String name){
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

            String query = "SELECT название, научное_название FROM частые_заболевания WHERE порода = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String str = "";
                str += resultSet.getString("название")+" ";
                str+= resultSet.getString("научное_название");
                list.add(str);
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