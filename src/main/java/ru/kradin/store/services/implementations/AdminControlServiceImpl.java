package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.models.enums.Role;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.AdminControlService;

import java.util.Optional;

@Service
public class AdminControlServiceImpl implements AdminControlService {

    private static final Logger log = LoggerFactory.getLogger(AdminControlServiceImpl.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public void checkAdminAccount() {
        Optional<User> user = userRepository.findByUsername("admin");
        if(user.isEmpty()){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Created admin account");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePassword(String newPassword) {
        Optional<User> user = userRepository.findByUsername("admin");
        if (user.isPresent()) {
            User admin = user.get();
            admin.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(admin);
            log.info("Updated password for admin account");
        }
    }
}
