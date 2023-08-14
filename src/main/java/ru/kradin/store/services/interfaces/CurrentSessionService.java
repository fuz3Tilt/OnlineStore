package ru.kradin.store.services.interfaces;

import ru.kradin.store.models.User;

/**
 * Uses for other service
 */
public interface CurrentSessionService {
    public User getUser();
}
