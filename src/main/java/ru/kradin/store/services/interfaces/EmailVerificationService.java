package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.*;

public interface EmailVerificationService {
    void sendTokenToEmail(String email) throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, VerificationTokenAlreadyExistsException;
    boolean isEmailVerified (String email, String token) throws VerificationTokenNotFoundException;
}
