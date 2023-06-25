package ru.kradin.store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;
import ru.kradin.store.utils.StoreScheduling;

@Configuration
public class SchedulingConfig {

    @Bean
    public StoreScheduling myScheduling(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            CartRepository cartRepository,
            UserVerificationTokenRepository userVerificationTokenRepository) {

        StoreScheduling storeScheduling = new StoreScheduling(
                passwordEncoder,
                userRepository,
                cartRepository,
                userVerificationTokenRepository);

        storeScheduling.createTasks();
        return storeScheduling;
    }
}
