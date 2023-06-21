package ru.kradin.store.enums;

public enum Role {
    ROLE_ADMIN("Администратор"),
    ROLE_USER("Пользователь");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
