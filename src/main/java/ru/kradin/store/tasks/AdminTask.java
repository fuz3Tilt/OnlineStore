package ru.kradin.store.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.enums.Role;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimerTask;

public class AdminTask extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(AdminTask.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CartRepository cartRepository;
    @Value("${spring.mail.username}")
    private String adminEmail;

    public AdminTask(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void run() {
        Optional<User> user = userRepository.findByUsername("admin");
        if(user.isEmpty()){
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    "Администратов",
                    "Администратор",
                    "Администраторович",
                    adminEmail,
                    true,
                    true,
                    true,
                    LocalDateTime.now(),
                    Role.ROLE_ADMIN);
            admin = userRepository.save(admin);
            Cart cart = new Cart(admin);
            cartRepository.save(cart);
            log.info("Admin account created");
        }
    }
}
