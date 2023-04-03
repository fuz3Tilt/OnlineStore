package ru.kradin.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kradin.store.exceptions.EmailAlreadyVerifiedException;
import ru.kradin.store.exceptions.UserDoesNotHaveEmailException;
import ru.kradin.store.exceptions.UserVerificationTokenAlreadyExistException;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;
import ru.kradin.store.models.User;
import ru.kradin.store.services.interfaces.UserService;

@Controller
@RequestMapping("/store/email")
public class EmailController {

    @Autowired
    UserService userService;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) throws UserVerificationTokenNotFoundException {
            userService.verifyEmail(token);
        return "redirect:/store/admin/info?success";
    }

    @PostMapping("/send-confirm")
    public String confirm(Authentication authentication) throws EmailAlreadyVerifiedException, UserDoesNotHaveEmailException, UserVerificationTokenAlreadyExistException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        userService.sendVerificationEmail(currentUser);
        return "redirect:/store/admin/info?confirm";
    }

    @GetMapping("/new")
    public String newEmail(Authentication authentication){
        return "email/new";
    }

    @PostMapping
    public String setEmail(@RequestParam("email") String email,Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        userService.updateEmail(currentUser,email);
        return "redirect:/store/admin/info";
    }
}
