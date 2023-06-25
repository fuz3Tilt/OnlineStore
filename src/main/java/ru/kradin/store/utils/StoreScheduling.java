package ru.kradin.store.utils;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kradin.store.enums.Role;
import ru.kradin.store.models.Cart;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.CartRepository;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.repositories.UserVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.*;

public class StoreScheduling {
    private static final Logger log = LoggerFactory.getLogger(StoreScheduling.class);

    private static final int PERIOD = 1000 * 60 * 60 * 24;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    private final UserVerificationTokenRepository tokenRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    public StoreScheduling(PasswordEncoder passwordEncoder, UserRepository userRepository, CartRepository cartRepository, UserVerificationTokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
    }

    public void createTasks() {
        TimerTask createAdminAccountIfNotExist = getCreateAdminAccountIfNotExist();
        TimerTask removeExpiredTokens = getRemoveExpiredTokens();
        TimerTask changeKeyAndShiftAmount = getChangeKeyAndShiftAmount();

        Timer intstantTimer = new Timer();
        Timer planedTimer = new Timer();

        Calendar instantDate = Calendar.getInstance();
        Calendar planedDate = Calendar.getInstance();
        planedDate.set(Calendar.HOUR_OF_DAY, 3);
        planedDate.set(Calendar.MINUTE, 0);
        planedDate.set(Calendar.SECOND, 0);

        if (planedDate.getTime().compareTo(new Date()) < 0) {
            planedDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        CryptoUtil.generateNewKeyAndShiftAmount();

        intstantTimer.schedule(createAdminAccountIfNotExist, instantDate.getTime(), PERIOD);
        intstantTimer.schedule(removeExpiredTokens, instantDate.getTime(),PERIOD);
        planedTimer.schedule(changeKeyAndShiftAmount, planedDate.getTime(),PERIOD);
    }

    private TimerTask getCreateAdminAccountIfNotExist() {
        return new TimerTask() {
            @Override
            @Transactional
            public void run() {
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
                    admin = userRepository.save(admin);
                    Cart cart = new Cart(admin);
                    cartRepository.save(cart);
                    log.info("Admin account created");
                }
            }
        };
    }

    private TimerTask getRemoveExpiredTokens() {
        return new TimerTask() {
            @Override
            @Transactional
            public void run() {
                tokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());
                log.info("Expired verification tokens removed");
            }
        };
    }

    private TimerTask getChangeKeyAndShiftAmount() {
        return new TimerTask() {
            @Override
            public void run() {
                CryptoUtil.generateNewKeyAndShiftAmount();
            }
        };
    }
}
