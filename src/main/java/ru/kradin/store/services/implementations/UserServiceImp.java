package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.exceptions.PasswordMismatchException;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.CurrentUserService;
import ru.kradin.store.services.interfaces.EmailVerificationService;
import ru.kradin.store.services.interfaces.UserService;

@Service
public class UserServiceImp implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserDTO getCurrent() {
        User user = currentUserService.get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public void requestTokenForEmail(String email) {
        emailVerificationService.requestTokenForEmail(email);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateEmail(String email, Integer token) throws VerificationTokenNotFoundException {
        if (emailVerificationService.isEmailVerified(email, token)) {
            User user = currentUserService.get();
            user.setEmail(email);
            user.setEmailVerified(false);
            userRepository.save(user);
            log.info("{} email updated.", user.getUsername());
        } else {
            throw new VerificationTokenNotFoundException();
        }
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updatePassword(String oldPassword, String newPassword, String passwordConfirm) throws PasswordMismatchException {
        User user = currentUserService.get();
        if (!newPassword.equals(passwordConfirm))
            throw new PasswordMismatchException("Passwords don't match");

        if (newPassword.length() <= 4)
            throw new PasswordMismatchException("Password length should be greater than 4");

        if (!passwordEncoder.matches(oldPassword,user.getPassword()))
            throw new PasswordMismatchException("Invalid old password");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("{} password updated.", user.getUsername());
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateFullName(String firstName, String middleName, String lastName) {
        User user = currentUserService.get();
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        userRepository.save(user);
        log.info("{} full name updated.", user.getUsername());
    }
}
