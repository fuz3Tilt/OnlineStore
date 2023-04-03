package ru.kradin.store.services.interfaces;

public interface UserVerificationTokenService {
    public void removeExpiredTokens();
}
