package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.TokenPurpose;

import java.time.LocalDateTime;

@Entity
public class UserVerificationToken extends AbstractPersistable<Long> {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    @Enumerated(EnumType.STRING)
    @Column(name = "token_purpose", nullable = false)
    private TokenPurpose tokenPurpose;
    @Column(name = "expiry_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime expiryDate;

    public UserVerificationToken(User user, String token, TokenPurpose tokenPurpose, LocalDateTime expiryDate) {
        this.user = user;
        this.token = token;
        this.tokenPurpose = tokenPurpose;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public TokenPurpose getTokenPurpose() {
        return tokenPurpose;
    }

    public void setTokenPurpose(TokenPurpose tokenPurpose) {
        this.tokenPurpose = tokenPurpose;
    }
}
