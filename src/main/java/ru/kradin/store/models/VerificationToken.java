package ru.kradin.store.models;

import javax.persistence.*;

import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.TokenPurpose;

import java.time.LocalDateTime;

@Entity
public class VerificationToken extends AbstractPersistable<Long> {
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenPurpose tokenPurpose;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiryDate;
    @Column(nullable = false)
    private String email;

    public VerificationToken() {
    }

    public VerificationToken(String token, TokenPurpose tokenPurpose, LocalDateTime expiryDate, String email) {
        this.token = token;
        this.tokenPurpose = tokenPurpose;
        this.expiryDate = expiryDate;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenPurpose getTokenPurpose() {
        return tokenPurpose;
    }

    public void setTokenPurpose(TokenPurpose tokenPurpose) {
        this.tokenPurpose = tokenPurpose;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
