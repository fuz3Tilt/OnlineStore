package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.models.User;
import ru.kradin.store.models.UserVerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {
    Optional<UserVerificationToken> findByTokenAndTokenPurpose(String token, TokenPurpose tokenPurpose);

    Optional<UserVerificationToken> findByUserAndTokenPurpose(User user, TokenPurpose tokenPurpose);

    void deleteByExpiryDateLessThan(LocalDateTime now);
}