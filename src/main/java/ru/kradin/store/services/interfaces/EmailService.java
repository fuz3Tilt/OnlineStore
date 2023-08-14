package ru.kradin.store.services.interfaces;

/**
 * Uses for sending emails
 */
public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
