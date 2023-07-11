package ru.kradin.store.services.interfaces;

public interface EmailVerificationService {
    void requestTokenForEmail(String email);
    boolean isEmailVerified (String email, Integer token);
}
