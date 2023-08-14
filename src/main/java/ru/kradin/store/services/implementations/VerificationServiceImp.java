package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.exceptions.*;
import ru.kradin.store.models.User;
import ru.kradin.store.models.VerificationToken;
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
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Value("${store.passwordResetURL}")
    private String passwordResetUrl;

    private static final String EMAIL_VERIFYING_SUBJECT = "Подтверждение почты";
    private static final String EMAIL_VERIFYING_TEXT = "Код подтверждения: ";
    private static final String PASSWORD_RESET_SUBJECT = "Сброс пароля";
    private static final String PASSWORD_RESET_TEXT = "Перейдите по ссылке, чтобы сбросить пароль: ";

    @Override
    public void requestTokenForEmail(String email) {
        deleteOldTokenIfExist(email, TokenPurpose.EMAIL_CONFIRMATION);

        String token = generateVerificationToken(TokenPurpose.EMAIL_CONFIRMATION, email,10);

        emailService.sendSimpleMessage(email,EMAIL_VERIFYING_SUBJECT,EMAIL_VERIFYING_TEXT+token);
        log.info("Verification email sent to {} .", email);
    }

    @Override
    @Transactional
    public boolean isEmailVerified(String email, Integer token) {
        boolean emailVerified = false;
        Optional<VerificationToken> verificationTokenOptional
                = verificationTokenRepository.findByEmailAndTokenPurpose(email,TokenPurpose.EMAIL_CONFIRMATION);
        if (verificationTokenOptional.isPresent() && verificationTokenOptional.get().getToken().equals(String.valueOf(token))) {
            emailVerified = true;
            verificationTokenRepository.delete(verificationTokenOptional.get());
        }
        return emailVerified;
    }

    @Override
    public void sendPasswordResetEmail(String email) throws UserNotFoundException, VerificationTokenAlreadyExistsException {
        User user = getUserForSendingPasswordResetEmail(email);

        String token = generateVerificationToken(TokenPurpose.PASSWORD_RESET, user.getEmail(), 60);

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

        Optional<VerificationToken> verificationTokenOptional
                = verificationTokenRepository.findByEmailAndTokenPurpose(email, TokenPurpose.PASSWORD_RESET);

        if(verificationTokenOptional.isPresent()) {
            VerificationToken verificationToken = verificationTokenOptional.get();
            if (!isTokenExpired(verificationToken))
                throw new VerificationTokenAlreadyExistsException();
        }
        return user;
    }

    @Transactional
    private User getUserForResettingPasswordWithToken(String token) throws VerificationTokenNotFoundException {
        Optional<VerificationToken> verificationTokenOptional =
                verificationTokenRepository.findByTokenAndTokenPurpose(token, TokenPurpose.PASSWORD_RESET);

        if (verificationTokenOptional.isEmpty())
            throw new VerificationTokenNotFoundException();

        VerificationToken verificationToken = verificationTokenOptional.get();

        if (isTokenExpired(verificationToken))
            throw new VerificationTokenNotFoundException();

        verificationTokenRepository.delete(verificationToken);

        return userRepository.findByEmail(verificationToken.getEmail()).get();
    }

    @Transactional
    public void deleteOldTokenIfExist(String email, TokenPurpose tokenPurpose) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByEmailAndTokenPurpose(email, tokenPurpose);
        if (verificationTokenOptional.isPresent())
            verificationTokenRepository.delete(verificationTokenOptional.get());
    }

    @Transactional
    private String generateVerificationToken(TokenPurpose tokenPurpose, String email, int tokenLifetimeInMinutes) {
        String token;

        if (tokenPurpose.equals(TokenPurpose.PASSWORD_RESET)) {
            token = randomStringToken();
        } else {
            token = randomNumberToken();
        }

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(tokenLifetimeInMinutes);
        VerificationToken  verificationToken= new VerificationToken(
                token,
                tokenPurpose,
                expiryDate,
                email
        );
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
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
