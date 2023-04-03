package ru.kradin.store.services.interfaces;

public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
