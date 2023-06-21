package ru.kradin.store.services.interfaces;

import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistsException;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;

public interface VerificationService {

    public void sendVerificationEmail() throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistsException;

    public void verifyEmail(String token) throws UserVerificationTokenNotFoundException;

    public void sendPasswordResetEmail(String email);

    public void resetPasswordWithToken(String password, String token) throws UserVerificationTokenNotFoundException;
}
