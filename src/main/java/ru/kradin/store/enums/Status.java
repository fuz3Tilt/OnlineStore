package ru.kradin.store.enums;

public enum Status {
    AVAILABLE("Есть в наличии"),
    UNAVAILABLE("Нет в наличии");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
