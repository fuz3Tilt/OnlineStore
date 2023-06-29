package ru.kradin.store.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;
import ru.kradin.store.services.interfaces.CurrentUserService;

import java.util.Optional;

@Service
public class CurrentUserServiceImp implements CurrentUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> user = userRepository.findByUsername(username);

        return user.get();
    }
}
