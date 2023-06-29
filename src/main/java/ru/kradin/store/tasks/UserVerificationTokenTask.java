package ru.kradin.store.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.repositories.UserVerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class UserVerificationTokenTask extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(UserVerificationTokenTask.class);
    private UserVerificationTokenRepository userVerificationTokenRepository;

    public UserVerificationTokenTask(UserVerificationTokenRepository userVerificationTokenRepository) {
        this.userVerificationTokenRepository = userVerificationTokenRepository;
    }

    @Override
    @Transactional
    public void run() {
        userVerificationTokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());
        log.info("Expired verification tokens removed");
    }
}
