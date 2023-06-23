package ru.kradin.store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;
import ru.kradin.store.utils.MyScheduling;

@Configuration
public class SchedulingConfig {

    @Bean
    public MyScheduling myScheduling(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UserVerificationTokenRepository userVerificationTokenRepository) {

        MyScheduling myScheduling = new MyScheduling(
                passwordEncoder,
                userRepository,
                userVerificationTokenRepository);

        myScheduling.createTasks();
        return myScheduling;
    }
}
