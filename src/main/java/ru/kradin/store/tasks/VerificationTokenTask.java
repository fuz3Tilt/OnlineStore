package ru.kradin.store.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.repositories.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.TimerTask;

public class VerificationTokenTask extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(VerificationTokenTask.class);
    private VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenTask(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    @Transactional
    public void run() {
        verificationTokenRepository.deleteByExpiryDateLessThan(LocalDateTime.now());
        log.info("Expired verification tokens removed");
    }
}
