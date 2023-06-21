package ru.kradin.store.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO getUserDTO() {
        return null;
    }

    @Override
    public void updateEmail(String email) {

    }

    @Override
    public void updatePassword(String password) {

    }
}
