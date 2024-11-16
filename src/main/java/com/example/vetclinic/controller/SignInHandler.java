package com.example.vetclinic.controller;

import com.example.vetclinic.module.UserSQL;
import com.example.vetclinic.module.VeterinarianSQL;

public class SignInHandler {

    private static UserSQL userSQL = UserSQL.getInstance();
    private static VeterinarianSQL vetSQL = VeterinarianSQL.getInstance();

    // Метод для проверки логина и пароля пользователя
    public static boolean isValidLogin(String phone, String password, String role) {
        boolean isValid = false;

        // В зависимости от роли проверяем данные в нужной таблице
        if (role.equalsIgnoreCase("Owner")) {
            isValid = userSQL.isUsers(phone, password);  // Проверка для владельца
        } else if (role.equalsIgnoreCase("Veterinarian")) {
            isValid = vetSQL.isUsers(phone, password);  // Проверка для ветеринара
        }

        return isValid;
    }

    // Получение логина (можно использовать во всех контроллерах)
    public static String getLogin(String phoneNumber) {
        return phoneNumber;  // Возвращаем номер телефона как логин
    }

    // Получение пароля (можно использовать во всех контроллерах)
    public static String getPassword(String password) {
        return password;  // Возвращаем введенный пароль
    }
}
