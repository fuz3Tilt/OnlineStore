package ru.kradin.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kradin.store.models.PasswordVerificationToken;
import ru.kradin.store.models.User;

import java.util.Optional;

public interface PasswordVerificationTokenRepository extends JpaRepository<PasswordVerificationToken, Long> {
    Optional<PasswordVerificationToken> findByToken(String token);

    Optional<PasswordVerificationToken> findByUser(User user);
}
