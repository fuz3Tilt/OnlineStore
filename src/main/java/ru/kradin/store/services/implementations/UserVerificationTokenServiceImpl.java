package ru.kradin.store.services.implementations;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kradin.store.repositories.UserVerificationTokenRepository;

import java.time.LocalDateTime;

@Service
public class UserVerificationTokenServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(UserVerificationTokenServiceImpl.class);

    @Autowired
    private UserVerificationTokenRepository tokenRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 0)
    @Transactional
    public void removeExpiredTokens() {
        tokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());
        log.info("Expired verification tokens removed");
    }
}
