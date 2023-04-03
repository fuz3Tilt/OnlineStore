package ru.kradin.store.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistException;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;
import ru.kradin.store.models.User;
import ru.kradin.store.models.UserVerificationToken;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;
import ru.kradin.store.services.interfaces.EmailService;
import ru.kradin.store.services.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserVerificationTokenRepository userVerificationTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public void updateEmail(User user, String email) {
        user.setEmail(email);
        user.setEmailVerified(false);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void sendVerificationEmail(User user) throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistException {
        if (user.isEmailVerified())
            throw new EmailAlreadyVerifiedException();

        if (user.getEmail().isEmpty())
            throw new UserDoesNotHaveEmailException();

        Optional<UserVerificationToken> userVerificationToken
                = userVerificationTokenRepository.findByUserAndTokenPurpose(user,TokenPurpose.EMAIL_CONFIRMATION);

        if(userVerificationToken.isPresent())
            throw new UserVerificationTokenAlreadyExistException();

        String token = generateVerificationToken(user,TokenPurpose.EMAIL_CONFIRMATION,5);
        String confirmationUrl = "http://localhost:8080/store/email/verify?token=" + token;

        String email = user.getEmail();
        String subject = "Подтвердите почту";
        String text = "Пожалуйста, перейдите по ссылке чтобы подтвердить почту: "+confirmationUrl;

        emailService.sendSimpleMessage(email,subject,text);
    }

    @Override
    public void verifyEmail(String token) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationTokenOptional
                = userVerificationTokenRepository.findByTokenAndTokenPurpose(token,TokenPurpose.EMAIL_CONFIRMATION);
        if (userVerificationTokenOptional.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();

        User user = userVerificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        userVerificationTokenRepository.delete(userVerificationToken);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            return;

        User user = userOptional.get();

        Optional<UserVerificationToken> userVerificationToken
                = userVerificationTokenRepository.findByUserAndTokenPurpose(user,TokenPurpose.PASSWORD_RESET);

        if(userVerificationToken.isPresent())
            return;

        String token = generateVerificationToken(user,TokenPurpose.PASSWORD_RESET,5);
        String passwordResetUrl = "http://localhost:8080/store/password/reset?token=" + token;

        String subject = "Сброс пароля";
        String text = "Перейдите по ссылке чтобы сбросить пароль: "+passwordResetUrl;

        emailService.sendSimpleMessage(email,subject,text);
    }

    @Override ///////////
    public void resetPasswordWithToken(String token, String password) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationToken =
                userVerificationTokenRepository.findByTokenAndTokenPurpose(token,TokenPurpose.PASSWORD_RESET);

        if (userVerificationToken.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        User user = userVerificationToken.get().getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public User getUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }
    private String generateVerificationToken(User user, TokenPurpose tokenPurpose, int tokenLifetimeInMinutes){
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);
        UserVerificationToken userVerificationToken = new UserVerificationToken();
        userVerificationToken.setUser(user);
        userVerificationToken.setToken(token);
        userVerificationToken.setTokenPurpose(tokenPurpose);
        userVerificationToken.setExpiryDate(expiryDate);
        userVerificationTokenRepository.save(userVerificationToken);
        return userVerificationToken.getToken();
    }
}
