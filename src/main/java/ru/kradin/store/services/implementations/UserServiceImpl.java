package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import ru.kradin.store.validators.EmailInfo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserVerificationTokenRepository userVerificationTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Value("${store.host}")
    String host;

    @Override
    @PreAuthorize("isAuthenticated()")
    public void updateEmail(Authentication authentication, String email) {
        User user = getUserByAuthentication(authentication);

        user.setEmail(email);
        user.setEmailVerified(false);
        userRepository.save(user);
        log.info("{} email updated.", user.getUsername());
    }

    @Override
    public EmailInfo getEmailInfo(Authentication authentication) {
        User user = getUserByAuthentication(authentication);

        return new EmailInfo(user.getEmail(),user.isEmailVerified());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void updatePassword(Authentication authentication, String password) {
        User user = getUserByAuthentication(authentication);

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("{} password updated.", user.getUsername());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void sendVerificationEmail(Authentication authentication) throws UserDoesNotHaveEmailException, EmailAlreadyVerifiedException, UserVerificationTokenAlreadyExistException {
        User user = getUserByAuthentication(authentication);

        if (user.isEmailVerified())
            throw new EmailAlreadyVerifiedException();

        if (user.getEmail().isEmpty())
            throw new UserDoesNotHaveEmailException();

        Optional<UserVerificationToken> userVerificationToken
                = userVerificationTokenRepository.findByUserAndTokenPurposeAndExpiryDateGreaterThan(user,TokenPurpose.EMAIL_CONFIRMATION,LocalDateTime.now());

        if(userVerificationToken.isPresent())
            throw new UserVerificationTokenAlreadyExistException();

        String token = generateVerificationToken(user,TokenPurpose.EMAIL_CONFIRMATION,5);
        String confirmationUrl = host + "store/email/verify?token=" + token;

        String email = user.getEmail();
        String subject = "Подтвердите почту";
        String text = "Пожалуйста, перейдите по ссылке чтобы подтвердить почту: "+confirmationUrl;

        emailService.sendSimpleMessage(email,subject,text);
        log.info("Verification email sent for {} .", user.getUsername());
    }

    @Override
    public void verifyEmail(String token) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationTokenOptional
                = userVerificationTokenRepository.findByTokenAndTokenPurposeAndExpiryDateGreaterThan(token,TokenPurpose.EMAIL_CONFIRMATION, LocalDateTime.now());
        if (userVerificationTokenOptional.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        UserVerificationToken userVerificationToken = userVerificationTokenOptional.get();

        User user = userVerificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        userVerificationTokenRepository.delete(userVerificationToken);
        log.info("{} email verified.", user.getUsername());
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if(!user.isEmailVerified())
            return;

        Optional<UserVerificationToken> userVerificationToken
                = userVerificationTokenRepository.findByUserAndTokenPurposeAndExpiryDateGreaterThan(user,TokenPurpose.PASSWORD_RESET,LocalDateTime.now());

        if(userVerificationToken.isPresent())
            return;

        String token = generateVerificationToken(user,TokenPurpose.PASSWORD_RESET,5);
        String passwordResetUrl = host + "store/password/reset?token=" + token;

        String subject = "Сброс пароля";
        String text = "Перейдите по ссылке чтобы сбросить пароль: "+passwordResetUrl;

        emailService.sendSimpleMessage(email,subject,text);
        log.info("Password reset email sent for {} .", user.getUsername());
    }

    @Override
    public void resetPasswordWithToken(String token, String password) throws UserVerificationTokenNotFoundException {
        Optional<UserVerificationToken> userVerificationToken =
                userVerificationTokenRepository.findByTokenAndTokenPurposeAndExpiryDateGreaterThan(token,TokenPurpose.PASSWORD_RESET,LocalDateTime.now());

        if (userVerificationToken.isEmpty())
            throw new UserVerificationTokenNotFoundException();

        User user = userVerificationToken.get().getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
	    userVerificationTokenRepository.delete(userVerificationToken.get());
        log.info("{} password updated.", user.getUsername());
    }

    private User getUserByAuthentication(Authentication authentication) throws UsernameNotFoundException{
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

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
