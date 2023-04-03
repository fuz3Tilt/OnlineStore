package ru.kradin.store.services.interfaces;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistException;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;
import ru.kradin.store.models.User;

public interface UserService {

    public void setEmail(User user, String email);

    public void sendVerificationEmail(User user) throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistException;

    public void verifyEmail(String token) throws UserVerificationTokenNotFoundException;

    public void sendPasswordResetEmail(String email);

    public void verifyPasswordReset(String token);

    public User getUserByUsername(String username) throws UsernameNotFoundException;
}
