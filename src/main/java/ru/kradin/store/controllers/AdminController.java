package ru.kradin.store.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kradin.store.services.interfaces.AdminControlService;

@Controller
@RequestMapping("/store/admin")
public class AdminController {

    @Autowired
    AdminControlService adminControlService;

    @GetMapping
    public String adminHomePage(){
        return "admin/home";
    }

    @GetMapping("/login")
    public String loginForm(Authentication authentication){
        if (authentication!=null&&authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
            return "redirect:/store/admin";

        adminControlService.checkAdminAccount();
        return "admin/login";
    }

    @GetMapping("/update")
    public String updateForm(){
        return "admin/password-update";
    }

    @PatchMapping
    public String updateAdminParameters(HttpServletRequest request){
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if(password1.equals(password2)){
            adminControlService.updatePassword(password1);
            return "redirect:/store/admin/login";
        } else
        return "redirect:/store/admin/update?error";
    }
}
