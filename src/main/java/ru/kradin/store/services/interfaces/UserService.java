package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.exceptions.PasswordMismatchException;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;

/**
 * Uses for management user data
 */
public interface UserService {

    public UserDTO getCurrent();

    public void requestTokenForEmail(String email);

    public void updateEmail(String email, Integer token) throws VerificationTokenNotFoundException;

    public void updatePassword(String oldPassword, String newPassword, String passwordConfirm) throws PasswordMismatchException;

    public void updateFullName(String firstName, String middleName, String lastName);
}
