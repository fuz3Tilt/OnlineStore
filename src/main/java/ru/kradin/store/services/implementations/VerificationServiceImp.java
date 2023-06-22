package ru.kradin.store.services.implementations;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistsException;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;
import ru.kradin.store.models.User;
import ru.kradin.store.models.UserVerificationToken;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;
import ru.kradin.store.services.interfaces.AuthenticatedUserService;
import ru.kradin.store.services.interfaces.EmailService;
import ru.kradin.store.services.interfaces.VerificationService;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationServiceImp implements VerificationService {
    private static final Logger log = LoggerFactory.getLogger(VerificationServiceImp.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserVerificationTokenRepository userVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @Value("${store.email.verify.url}")
    private String emailVerifyUrl;

    @Value("${store.password.reset.url}")
    private String passwordResetUrl;

    private static final String EMAIL_VERIFYING_SUBJECT = "Подтверждение почты";
    private static final String EMAIL_VERIFYING_TEXT = "Перейдите по ссылке, чтобы подтвердить почту: ";
    private static final String PASSWORD_RESET_SUBJECT = "Сброс пароля";
    private static final String PASSWORD_RESET_TEXT = "Перейдите по ссылке, чтобы сбросить пароль: ";

    @Override
    @PreAuthorize("isAuthenticated()")
    public void sendVerificationEmail() throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistsException {
        User user = getUserForSendingVerificationEmail();

        String token = generateVerificationToken(user,TokenPurpose.EMAIL_CONFIRMATION,60);

        String confirmationUrl = emailVerifyUrl + token;

        emailService.sendSimpleMessage(user.getEmail(),EMAIL_VERIFYING_SUBJECT,EMAIL_VERIFYING_TEXT+confirmationUrl);
        log.info("Verification email sent for {} .", user.getUsername());
    }

    @Override
    @Transactional
    public void verifyEmail(String token) throws UserVerificationTokenNotFoundException {
        User user = getUserForVerifyingEmail(token);
        user.setEmailVerified(true);
        userRepository.save(user);
        log.info("{} email verified.", user.getUsername());
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user;

        try {
            user = getUserForSendingPasswordResetEmail(email);
        } catch (NullPointerException e) {
            return;
        }

        String token = generateVerificationToken(user,TokenPurpose.PASSWORD_RESET,60);

        String resetUrl = passwordResetUrl + token;

        emailService.sendSimpleMessage(email,PASSWORD_RESET_SUBJECT,PASSWORD_RESET_TEXT+resetUrl);
        log.info("Password reset email sent for {} .", user.getUsername());
    }

    @Override
    @Transactional
    public void resetPasswordWithToken(String token, String password) throws UserVerificationTokenNotFoundException {
        User user = getUserForResettingPasswordWithToken(token);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("{} password updated.", user.getUsername());
    }

    private User getUserForSendingVerificationEmail() throws EmailAlreadyVerifiedException, UserDoesNotHaveEmailException, UserVerificationTokenAlreadyExistsException {
        User user = authenticatedUserService.getCurentUser();

        if (user.isEmailVerified())
            throw new EmailAlreadyVerifiedException();

        if (user.getEmail().isEmpty())
            throw new UserDoesNotHaveEmailException();

        Optional<UserVerificationToken> userVerificationTokenOptional
                = userVerificationTokenRepository.findByUserAndTokenPurpose(user, TokenPurpose.EMAIL_CONFIRMATION);

        if(userVerificationTokenOptional.isPresent()) {
            UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();
            if(!isTokenExpired(userVerificationToken))
                throw new UserVerificationTokenAlreadyExistsException();
        }

        return user;
    }

    @Transactional
    private User getUserForVerifyingEmail(String token) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationTokenOptional
                = userVerificationTokenRepository.findByTokenAndTokenPurpose(token,TokenPurpose.EMAIL_CONFIRMATION);
        if (userVerificationTokenOptional.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();

        if (isTokenExpired(userVerificationToken))
            throw new UserVerificationTokenNotFoundException();

        userVerificationTokenRepository.delete(userVerificationToken);

        return userVerificationToken.getUser();
    }

    private User getUserForSendingPasswordResetEmail(String email) throws NullPointerException{
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            throw new NullPointerException();

        User user = userOptional.get();

        if(!user.isEmailVerified())
            throw new NullPointerException();

        Optional<UserVerificationToken> userVerificationTokenOptional
                = userVerificationTokenRepository.findByUserAndTokenPurpose(user,TokenPurpose.PASSWORD_RESET);

        if(userVerificationTokenOptional.isPresent()) {
            UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();
            if (!isTokenExpired(userVerificationToken))
                throw new NullPointerException();
        }
        return user;
    }

    @Transactional
    private User getUserForResettingPasswordWithToken(String token) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationTokenOptional =
                userVerificationTokenRepository.findByTokenAndTokenPurpose(token,TokenPurpose.PASSWORD_RESET);

        if (userVerificationTokenOptional.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();

        if (isTokenExpired(userVerificationToken))
            throw new UserVerificationTokenNotFoundException();

        userVerificationTokenRepository.delete(userVerificationToken);

        return userVerificationToken.getUser();
    }

    private String generateVerificationToken(User user, TokenPurpose tokenPurpose, int tokenLifetimeInMinutes){
        String token = randomToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);
        UserVerificationToken userVerificationToken = new UserVerificationToken(
                user,
                token,
                tokenPurpose,
                expiryDate
        );
        userVerificationTokenRepository.save(userVerificationToken);
        return userVerificationToken.getToken();
    }

    private static String randomToken() {
        try {
        int numberOfPieces = 5;
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom secureRandom;
            secureRandom = SecureRandom.getInstanceStrong();
        for (int i = 0; i < numberOfPieces; i++) {
            byte[] partOfTokenBytes = new byte[12];
            secureRandom.nextBytes(partOfTokenBytes);
            String partOfToken = Base64.getEncoder().encodeToString(partOfTokenBytes);
            tokenBuilder.append(partOfToken);
            if (i < numberOfPieces-1) {
                tokenBuilder.append("-");
            }
        }
        return tokenBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return UUID.randomUUID().toString();
        }
    }

    @Transactional
    private boolean isTokenExpired(UserVerificationToken userVerificationToken){
        boolean isExpired = userVerificationToken.getExpiryDate().isBefore(LocalDateTime.now());
        if(isExpired)
            userVerificationTokenRepository.delete(userVerificationToken);

        return isExpired;
    }
}
