package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;

public interface TokenVerificationService {
    void verifyEmail(String token) throws UserVerificationTokenNotFoundException;
    void resetPasswordWithToken(String password, String token) throws UserVerificationTokenNotFoundException;
}
