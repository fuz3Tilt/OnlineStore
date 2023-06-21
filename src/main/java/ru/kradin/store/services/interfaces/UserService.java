package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.UserDTO;

public interface UserService {

    public UserDTO getCurentUserDTO();

    public void updateEmail(String email);

    public void updatePassword(String password);
}
