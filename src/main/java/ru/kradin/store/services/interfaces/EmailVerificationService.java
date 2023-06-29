package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistsException;

public interface EmailVerificationService {
    void sendVerificationEmail() throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistsException;
    void sendPasswordResetEmail(String email);
}
