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
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.CurrentUserService;
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

    @Override
    @PreAuthorize("isAuthenticated()")
    public UserDTO getCurrent() {
        User user = currentUserService.get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateEmail(String email) {
        User user = currentUserService.get();
        user.setEmail(email);
        user.setEmailVerified(false);
        userRepository.save(user);
        log.info("{} email updated.", user.getUsername());
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updatePassword(String password) {
        User user = currentUserService.get();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("{} password updated.", user.getUsername());
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateLastName(String lastName) {
        User user = currentUserService.get();
        user.setLastName(lastName);
        userRepository.save(user);
        log.info("{} last name updated.", user.getUsername());
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateFirstName(String firstName) {
        User user = currentUserService.get();
        user.setFirstName(firstName);
        userRepository.save(user);
        log.info("{} first name updated.", user.getUsername());
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void updateMiddleName(String middleName) {
        User user = currentUserService.get();
        user.setMiddleName(middleName);
        userRepository.save(user);
        log.info("{} middle name updated.", user.getUsername());
    }
}
