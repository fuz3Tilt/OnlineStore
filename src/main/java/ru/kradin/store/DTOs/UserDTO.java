package ru.kradin.store.DTOs;

import ru.kradin.store.enums.Role;

import java.time.LocalDateTime;

public class UserDTO {
    private String username;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private boolean emailVerified;
    private boolean accountNonLocked;
    private boolean enabled;
    private LocalDateTime createdAt;
    private Role role;
}
