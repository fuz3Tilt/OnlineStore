package ru.kradin.store.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.VerificationTokenRepository;
import ru.kradin.store.tasks.AdminTask;
import ru.kradin.store.tasks.VerificationTokenTask;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Bean
    public AdminTask adminTask() {
        return new AdminTask(userRepository, passwordEncoder, cartRepository);
    }

    @Bean
    public VerificationTokenTask verificationTokenTask() {
        return new VerificationTokenTask(verificationTokenRepository);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void runAdminTask() {
        adminTask().run();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void runVerificationTokenTask() {
        verificationTokenTask().run();
    }
}
