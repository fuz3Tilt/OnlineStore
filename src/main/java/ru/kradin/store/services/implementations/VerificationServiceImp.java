package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.exceptions.*;
import ru.kradin.store.models.EmailVerificationToken;
import ru.kradin.store.models.User;
import ru.kradin.store.models.PasswordVerificationToken;
import ru.kradin.store.models.VerificationToken;
import ru.kradin.store.repositories.EmailVerificationTokenRepository;
import ru.kradin.store.repositories.PasswordVerificationTokenRepository;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.VerificationTokenRepository;
import ru.kradin.store.services.interfaces.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationServiceImp implements EmailVerificationService, PasswordVerificationService {
    private static final Logger log = LoggerFactory.getLogger(VerificationServiceImp.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private PasswordVerificationTokenRepository passwordVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CurrentUserService currentUserService;

    @Value("${store.passwordResetURL}")
    private String passwordResetUrl;

    private static final String EMAIL_VERIFYING_SUBJECT = "Подтверждение почты";
    private static final String EMAIL_VERIFYING_TEXT = "Код подтверждения: ";
    private static final String PASSWORD_RESET_SUBJECT = "Сброс пароля";
    private static final String PASSWORD_RESET_TEXT = "Перейдите по ссылке, чтобы сбросить пароль: ";

    @Override
    @PreAuthorize("isAuthenticated()")
    public void sendTokenToEmail(String email) {
        deleteOldEmailTokenIfExist(email);

        String token = generateVerificationTokenForEmailConfirmation(email,10);

        emailService.sendSimpleMessage(email,EMAIL_VERIFYING_SUBJECT,EMAIL_VERIFYING_TEXT+token);
        log.info("Verification email sent to {} .", email);
    }

    @Override
    public boolean isEmailVerified(String email, String token) {
        boolean emailVerified = false;
        Optional<EmailVerificationToken> emailVerificationTokenOptional = emailVerificationTokenRepository.findByEmailAndToken(email,token);
        if (emailVerificationTokenOptional.isPresent()) {
            emailVerified = true;
            emailVerificationTokenRepository.delete(emailVerificationTokenOptional.get());
        }
        return emailVerified;
    }

    @Override
    public void sendPasswordResetEmail(String email) throws UserNotFoundException, VerificationTokenAlreadyExistsException {
        User user = getUserForSendingPasswordResetEmail(email);

        String token = generateVerificationTokenForPasswordReset(user, 60);

        String resetUrl = passwordResetUrl + token;

        emailService.sendSimpleMessage(email,PASSWORD_RESET_SUBJECT,PASSWORD_RESET_TEXT+resetUrl);
        log.info("Password reset email sent for {} .", user.getUsername());
    }

    @Override
    @Transactional
    public void resetPasswordWithToken(String token, String password) throws VerificationTokenNotFoundException {
        User user = getUserForResettingPasswordWithToken(token);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("{} password updated.", user.getUsername());
    }

    private User getUserForSendingPasswordResetEmail(String email) throws UserNotFoundException, VerificationTokenAlreadyExistsException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            throw new UserNotFoundException();

        User user = userOptional.get();

        if(!user.isEmailVerified())
            throw new UserNotFoundException();

        Optional<PasswordVerificationToken> passwordVerificationTokenOptional
                = passwordVerificationTokenRepository.findByUser(user);

        if(passwordVerificationTokenOptional.isPresent()) {
            PasswordVerificationToken passwordVerificationToken = passwordVerificationTokenOptional.get();
            if (!isTokenExpired(passwordVerificationToken))
                throw new VerificationTokenAlreadyExistsException();
        }
        return user;
    }

    @Transactional
    private User getUserForResettingPasswordWithToken(String token) throws VerificationTokenNotFoundException {
        Optional<PasswordVerificationToken> passwordVerificationTokenOptional =
                passwordVerificationTokenRepository.findByToken(token);

        if (passwordVerificationTokenOptional.isEmpty())
            throw new VerificationTokenNotFoundException();

        PasswordVerificationToken passwordVerificationToken = passwordVerificationTokenOptional.get();

        if (isTokenExpired(passwordVerificationToken))
            throw new VerificationTokenNotFoundException();

        passwordVerificationTokenRepository.delete(passwordVerificationToken);

        return passwordVerificationToken.getUser();
    }

    public void deleteOldEmailTokenIfExist(String email) {
        Optional<EmailVerificationToken> emailVerificationTokenOptional = emailVerificationTokenRepository.findByEmail(email);
        if (emailVerificationTokenOptional.isPresent())
            emailVerificationTokenRepository.delete(emailVerificationTokenOptional.get());
    }

    private String generateVerificationTokenForPasswordReset(User user, int tokenLifetimeInMinutes){
        String token = randomStringToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);
        PasswordVerificationToken passwordVerificationToken = new PasswordVerificationToken(
                user,
                token,
                expiryDate
        );
        passwordVerificationTokenRepository.save(passwordVerificationToken);
        return passwordVerificationToken.getToken();
    }

    private String generateVerificationTokenForEmailConfirmation(String email, int tokenLifetimeInMinutes) {
        String token = randomNumberToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);
        EmailVerificationToken  emailVerificationToken= new EmailVerificationToken(
                email,
                token,
                expiryDate
        );
        emailVerificationTokenRepository.save(emailVerificationToken);
        return emailVerificationToken.getToken();
    }

    private static String randomStringToken() {
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
        }
        return tokenBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return UUID.randomUUID().toString();
        }
    }

    public String randomNumberToken() {
        SecureRandom secureRandom;
        StringBuilder tokenBuilder = new StringBuilder();
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            secureRandom = new SecureRandom();
        }
        for (int i = 1;i<=6;i++) {
            int randomNumber;

            if (i == 1)
                randomNumber = secureRandom.nextInt(1,10);
            else
                randomNumber = secureRandom.nextInt(10);

            tokenBuilder.append(randomNumber);
        }
        return tokenBuilder.toString();
    }

    @Transactional
    private boolean isTokenExpired(VerificationToken verificationToken){
        boolean isExpired = verificationToken.getExpiryDate().isBefore(LocalDateTime.now());
        if(isExpired)
            verificationTokenRepository.delete(verificationToken);

        return isExpired;
    }
}
