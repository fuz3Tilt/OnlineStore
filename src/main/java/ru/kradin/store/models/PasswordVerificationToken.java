package ru.kradin.store.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PasswordVerificationToken extends VerificationToken {
    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    public PasswordVerificationToken() {
    }

    public PasswordVerificationToken(User user, String token, LocalDateTime expiryDate) {
        super(token, expiryDate);
        this.user = user;
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
}
