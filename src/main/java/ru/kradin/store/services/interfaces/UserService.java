package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.UserDTO;

public interface UserService {

    public UserDTO getCurrent();

    public void updateEmail(String email);

    public void updatePassword(String password);

    public void updateLastName(String lastName);

    public void updateFirstName(String firstName);

    public void updateMiddleName(String middleName);
}
