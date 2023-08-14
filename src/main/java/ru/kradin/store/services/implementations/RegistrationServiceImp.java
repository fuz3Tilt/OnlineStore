package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.RegistrationDTO;
import ru.kradin.store.enums.Role;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.EmailVerificationService;
import ru.kradin.store.services.interfaces.RegistrationService;

import java.time.LocalDateTime;

/**
 * Uses for registration
 */
@Service
public class RegistrationServiceImp implements RegistrationService {
    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImp.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailVerificationService emailVerificationService;

    /**
     * Use UserValidator in a controller before perform!!!
     */
    @Override
    @Transactional
    public void register(RegistrationDTO registrationDTO) throws VerificationTokenNotFoundException {
        if (emailVerificationService.isEmailVerified(registrationDTO.getEmail(),registrationDTO.getToken())) {
            User user = new User(
                    registrationDTO.getUsername(),
                    passwordEncoder.encode(registrationDTO.getPassword()),
                    registrationDTO.getFirstName(),
                    registrationDTO.getMiddleName(),
                    registrationDTO.getLastName(),
                    registrationDTO.getEmail(),
                    true,
                    true,
                    LocalDateTime.now(),
                    Role.ROLE_USER);
            user = userRepository.save(user);
            Cart cart = new Cart(user);
            cartRepository.save(cart);
            log.info("User {} created", user.getUsername());
        } else {
            throw new VerificationTokenNotFoundException();
        }
    }
}
