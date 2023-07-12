package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.models.VerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByTokenAndTokenPurpose(String token, TokenPurpose tokenPurpose);

    Optional<VerificationToken> findByEmailAndTokenPurpose(String email, TokenPurpose tokenPurpose);

    public void deleteByExpiryDateLessThan(LocalDateTime now);
}
