package ru.kradin.store.services.interfaces;

import org.springframework.security.access.AccessDeniedException;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.exceptions.UserNotFoundException;

import java.util.List;

public interface AdminService {

    public List<UserDTO> getUsers();

    public void toggleUserBan(String username) throws UserNotFoundException, AccessDeniedException;
}
