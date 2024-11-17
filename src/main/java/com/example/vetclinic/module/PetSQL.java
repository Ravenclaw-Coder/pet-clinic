package com.example.vetclinic.module;

import java.sql.*;

public class PetSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static PetSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private PetSQL() {
    }

    public static synchronized PetSQL getInstance() {
        if (instance == null) {
            instance = new PetSQL();
        }
        return instance;
    }

    // Метод для получения ID питомца по имени и номеру телефона владельца
    public int getPetId(String petName, String ownerPhone) {
        Connection connection = null;
        int petId = -1; // Если питомец не найден, возвращаем -1
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "");

            String query = "SELECT p.id FROM pets p JOIN owners o ON p.owner_id = o.id WHERE p.name = ? AND o.phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, petName);
            preparedStatement.setString(2, ownerPhone);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                petId = resultSet.getInt("id"); // Если питомец найден, получаем его ID
            }

            resultSet.close();
            preparedStatement.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка при получении ID питомца: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
        return petId; // Если питомец найден, возвращаем его ID, иначе -1
    }

    // Метод для добавления информации о животном
    public boolean addPet(String petName, String hasBreed, String breed, String ownerPhone, boolean isOwner) {
        Connection connection = null;
        boolean success = false;
        int ownerId = getOwnerId(ownerPhone);

        if (ownerId == -1) {
            // Если владельца нет, добавляем его в таблицу owners
            ownerId = addOwner(ownerPhone); // Метод для добавления владельца
            if (ownerId == -1) {
                return false; // Ошибка при добавлении владельца
            }
        }

        // Обработка поля breed
        Integer breedValue = null;
        if ("1".equals(hasBreed) && breed != null && !breed.isEmpty()) {
            breedValue = getBreedId(breed);  // Получаем id породы по имени
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Запрос на добавление питомца
            String query = "INSERT INTO pets (name, has_breed, breed_id, owner_id, is_owner) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, petName);
            preparedStatement.setString(2, hasBreed);  // Устанавливаем 1 или 0 для наличия породы
            preparedStatement.setObject(3, breedValue);  // Если breed не указан, передаем null
            preparedStatement.setInt(4, ownerId);  // Указываем owner_id владельца
            preparedStatement.setBoolean(5, isOwner);  // Указываем флаг is_owner

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
                System.out.println("Питомец успешно добавлен!");
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



    // Метод для получения breed_id по имени породы
    // Метод для получения id породы по названию породы
    private Integer getBreedId(String breed) {
        Connection connection = null;
        Integer breedId = null;  // Используем Integer для обработки возможного null

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", "root", "");

            // Запрос для получения id породы по названию
            String query = "SELECT id FROM breeds WHERE breed_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, breed);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                breedId = resultSet.getInt("id");  // Получаем id породы
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

        return breedId;  // Возвращаем Integer или null, если не нашли
    }





    // Метод для получения owner_id по номеру телефона владельца
    private int getOwnerId(String ownerPhone) {
        Connection connection = null;
        int ownerId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", "root", "");

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

    // Метод для добавления нового владельца
    private int addOwner(String ownerPhone) {
        Connection connection = null;
        int ownerId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/petclinic", "root", "");

            String query = "INSERT INTO owners (phone) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ownerPhone);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    ownerId = generatedKeys.getInt(1);
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
        return ownerId;
    }


}
