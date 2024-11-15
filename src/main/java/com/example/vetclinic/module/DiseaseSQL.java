package com.example.vetclinic.module;

public class DiseaseSQL {
    // Статическая переменная, которая содержит единственный экземпляр класса
    private static DiseaseSQL instance;

    // Приватный конструктор для предотвращения создания экземпляров класса извне
    private DiseaseSQL() {
    }

    public static synchronized DiseaseSQL getInstance() {
        if (instance == null) {
            instance = new DiseaseSQL();
        }
        return instance;
    }
}