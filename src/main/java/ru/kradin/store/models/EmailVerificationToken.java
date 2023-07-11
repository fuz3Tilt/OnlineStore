package ru.kradin.store.models;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EmailVerificationToken extends VerificationToken{
    @Column(unique = true)
    private String email;

    public EmailVerificationToken() {
    }

    public EmailVerificationToken(String email, String token, LocalDateTime expiryDate) {
        super(token, expiryDate);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
