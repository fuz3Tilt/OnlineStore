package ru.kradin.store.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kradin.store.DTOs.RegistrationDTO;
import ru.kradin.store.models.User;
import ru.kradin.store.repositories.UserRepository;

import java.util.Optional;

/**
 * Uses for validating new users in a controller. Use before performe register(RegistrationDTO registrationDTO)!!!
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegistrationDTO userRegistrationDTO = (RegistrationDTO) o;

        Optional<User> userWithTheSameEmail = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (userWithTheSameEmail.isPresent())
            errors.rejectValue("email", "", "Email is already taken");

        Optional<User> userWithTheSameUsername = userRepository.findByUsername(userRegistrationDTO.getUsername());
        if (userWithTheSameUsername.isPresent())
            errors.rejectValue("username", "", "Username is already taken");
    }
}
