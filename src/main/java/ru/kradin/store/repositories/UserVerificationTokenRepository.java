package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.enums.TokenPurpose;
import ru.kradin.store.models.User;
import ru.kradin.store.models.UserVerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {
    Optional<UserVerificationToken> findByTokenAndTokenPurposeAndExpiryDateGreaterThan(String token, TokenPurpose tokenPurpose, LocalDateTime now);


    @Transactional
    @Modifying
    @Query("delete from UserVerificationToken u where u.expiryDate < ?1")
    void deleteByExpiryDateLessThan(LocalDateTime now);

    Optional<UserVerificationToken> findByUserAndTokenPurposeAndExpiryDateGreaterThan(User user, TokenPurpose tokenPurpose, LocalDateTime now);
}