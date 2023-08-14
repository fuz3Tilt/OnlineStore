package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.UserNotFoundException;
import ru.kradin.store.exceptions.VerificationTokenAlreadyExistsException;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;

/**
 * Uses resseting password
 */
public interface PasswordVerificationService {
    void sendPasswordResetEmail(String email) throws UserNotFoundException, VerificationTokenAlreadyExistsException;

    void resetPasswordWithToken(String password, String token) throws VerificationTokenNotFoundException;

}
