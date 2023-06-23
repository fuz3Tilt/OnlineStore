package ru.kradin.store.services.implementations;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kradin.store.DTOs.RegistrationDTO;
import ru.kradin.store.enums.Role;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.RegistrationService;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImp implements RegistrationService {
    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImp.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegistrationDTO registrationDTO) {
        User user = new User(
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword()),
                registrationDTO.getEmail(),
                false,
                true,
                true,
                LocalDateTime.now(),
                Role.ROLE_ADMIN);
        userRepository.save(user);
        log.info("User {} created", user.getUsername());
    }
}
