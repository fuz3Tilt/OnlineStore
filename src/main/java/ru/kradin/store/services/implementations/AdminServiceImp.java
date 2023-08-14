package ru.kradin.store.services.implementations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.enums.Role;
import ru.kradin.store.exceptions.UserNotFoundException;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.AdminService;

import java.util.List;

@Service
public class AdminServiceImp implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImp.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
        return userDTOs;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void toggleUserBan(String username) throws UserNotFoundException, AccessDeniedException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());

        if (user.getRole().equals(Role.ROLE_ADMIN))
            throw new AccessDeniedException("You can't ban the administrator");

        toggleAccountNonLockedField(user);
        userRepository.save(user);
    }

    private void toggleAccountNonLockedField(User user) {
        if (user.isAccountNonLocked()) {
            user.setAccountNonLocked(false);
            log.info("User {} locked.", user.getUsername());
        } else {
            user.setAccountNonLocked(true);
            log.info("User {} unlocked.", user.getUsername());
        }
    }
}
