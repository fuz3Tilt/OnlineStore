package ru.kradin.store.services.interfaces;

/**
 * Uses for verificating email
 */
public interface EmailVerificationService {
    void requestTokenForEmail(String email);
    boolean isEmailVerified (String email, Integer token);
}
