package ru.kradin.store.utils;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kradin.store.enums.Role;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class SchedulingUtil {
    private static final Logger log = LoggerFactory.getLogger(SchedulingUtil.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationTokenRepository tokenRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Transactional
    @Scheduled(fixedRate = 1000*60*60*24, initialDelay = 0)
    public void createAdminAccountIfNotExist() {
        Optional<User> user = userRepository.findByUsername("admin");
        if(user.isEmpty()){
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    adminEmail,
                    true,
                    true,
                    true,
                    LocalDateTime.now(),
                    Role.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Admin account created");
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 0)
    @Transactional
    public void removeExpiredTokens() {
        tokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());
        log.info("Expired verification tokens removed");
    }
}
