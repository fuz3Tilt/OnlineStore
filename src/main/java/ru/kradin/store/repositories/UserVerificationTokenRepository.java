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

    Optional<UserVerificationToken> findByTokenAndTokenPurpose(String token, TokenPurpose tokenPurpose);

    @Transactional
    @Modifying
    @Query("delete from UserVerificationToken u where u.expiryDate < ?1")
    void deleteByExpiryDateLessThan(LocalDateTime expiryDate);

    Optional<UserVerificationToken> findByUserAndTokenPurpose(User user, TokenPurpose tokenPurpose);


}