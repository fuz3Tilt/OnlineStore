package ru.kradin.store.services.interfaces;

public interface AdminControlService {
    public void checkAdminAccount();

    public void updatePassword(String newPassword);
}
