package ru.kradin.store.services.implementations;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kradin.store.enums.Role;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.AdminService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.mail.username}")
    String adminEmail;

    @Override
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
            log.info("Created admin account");
        }
    }
}
