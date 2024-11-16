package com.example.vetclinic.module;

public class DiseaseSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static DiseaseSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private DiseaseSQL() {
    }

    // Синглтон для получения экземпляра класса
    public static synchronized DiseaseSQL getInstance() {
        if (instance == null) {
            instance = new DiseaseSQL();
        }
        return instance;
    }

    // Дополнительные методы для работы с болезнями в базе данных можно добавить ниже
    // Например, методы для добавления болезни, получения информации о болезни и т.д.
}
