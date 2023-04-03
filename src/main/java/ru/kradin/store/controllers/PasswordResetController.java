package ru.kradin.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kradin.store.exceptions.UserVerificationTokenNotFoundException;
import ru.kradin.store.services.interfaces.UserService;

@Controller
@RequestMapping("/store/password/reset")
public class PasswordResetController {

    @Autowired
    UserService userService;

    @GetMapping
    public String reset(@RequestParam("token") String token, Model model) throws UserVerificationTokenNotFoundException {
        model.addAttribute("token",token);
        return "password_reset/reset";
    }

    @PatchMapping
    public String executeReset(@RequestParam("token") String token,
                               @RequestParam(value = "password1", required = false) String password1,
                               @RequestParam(value = "password2", required = false) String password2) throws UserVerificationTokenNotFoundException {

        if (password1 == null || password1.isEmpty()) {
            return "redirect:/store/password/reset?errorIsNull&token="+token;
        }

        if (!password1.equals(password2)) {
            return "redirect:/store/password/reset?errorNotEqual&token="+token;
        }

        if (password1.length() < 4) {
            return "redirect:/store/password/reset?errorSmallLength&token="+token;
        }

        userService.resetPasswordWithToken(token, password1);
        return "redirect:/store/admin/login";
    }

    @GetMapping("/request")
    public String resetRequest(){
        return "password_reset/request";
    }

    @PostMapping("/request")
    public String executeResetRequest(@RequestParam("email") String email){
        userService.sendPasswordResetEmail(email);
        return "redirect:/store/password/reset/request/sent";
    }

    @GetMapping("/request/sent")
    public String requestSent(){
        return "password_reset/request-sent";
    }
}
