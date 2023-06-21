package ru.kradin.store.services.interfaces;

import ru.kradin.store.models.User;

public interface AuthenticatedUserService {
    public User getCurentUser();
}
