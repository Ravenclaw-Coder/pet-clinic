package com.example.vetclinic.module;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Appointment {
    private final SimpleIntegerProperty appointmentId;
    private final SimpleStringProperty appointmentDate;
    private final SimpleStringProperty petName;
    private final SimpleStringProperty vetName;
    private final SimpleStringProperty appointmentTime;
    private final SimpleBooleanProperty isCompleted; // Финальное поле должно быть инициализировано

    // Основной конструктор
    public Appointment(int appointmentId, String appointmentDate, String petName, String appointmentTime, boolean isCompleted) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.appointmentDate = new SimpleStringProperty(appointmentDate);
        this.petName = new SimpleStringProperty(petName);
        this.vetName = new SimpleStringProperty(petName);
        this.appointmentTime = new SimpleStringProperty(appointmentTime);
        this.isCompleted = new SimpleBooleanProperty(isCompleted); // Инициализация
    }

    // Альтернативный конструктор с isCompleted по умолчанию
    public Appointment(int appointmentId, String appointmentDate, String petName, String appointmentTime) {
        this(appointmentId, appointmentDate, petName, appointmentTime, false); // Указываем значение isCompleted
    }

    // Геттеры
    public int getAppointmentId() {
        return appointmentId.get();
    }

    public String getAppointmentDate() {
        return appointmentDate.get();
    }

    public String getPetName() {
        return petName.get();
    }

    public String getVetName() {
        return vetName.get();
    }

    public String getAppointmentTime() {
        return appointmentTime.get();
    }

    public boolean isCompleted() {
        return isCompleted.get();
    }

    public String getStatus() {
        return isCompleted.get() ? "Выполнен" : "Активен";
    }
}


