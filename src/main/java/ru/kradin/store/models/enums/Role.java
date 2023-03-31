package ru.kradin.store.models.enums;

public enum Role {
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
