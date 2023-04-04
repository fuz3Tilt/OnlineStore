package ru.kradin.store.models;

import jakarta.persistence.*;
import ru.kradin.store.enums.TokenPurpose;

import java.time.LocalDateTime;

@Entity
public class UserVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
