package ru.kradin.store.enums;

public enum Status {
    REGISTERED("Зарегистрирован"),
    SENT("Отправлен"),
    COMPLETED("Выполнен");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
