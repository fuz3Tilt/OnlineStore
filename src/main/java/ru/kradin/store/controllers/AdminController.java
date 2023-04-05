package ru.kradin.store.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kradin.store.services.interfaces.AdminControlService;
import ru.kradin.store.services.interfaces.UserService;
import ru.kradin.store.validators.EmailInfo;

@Controller
@RequestMapping("/store/admin")
public class AdminController {

    @Autowired
    AdminControlService adminControlService;

    @Autowired
    UserService userService;

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

    @GetMapping("/info")
    public String getInfo(Authentication authentication, Model model) throws UsernameNotFoundException {
        EmailInfo emailInfo = userService.getEmailInfo(authentication);

        if(!(emailInfo.getEmail()==null)) {
            model.addAttribute("email", emailInfo.getEmail());
            model.addAttribute("emailVerified", emailInfo.isEmailVerified());
        }
        return "admin/info";
    }

    @GetMapping("/update/password")
    public String updatePasswordForm(){
        return "admin/update-password";
    }

    @PatchMapping("/update/password")
    public String updateAdminPassword(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) {
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        if (password1 == null || password1.isEmpty()) {
            return "redirect:/store/admin/update/password?errorIsNull";
        }

        if (!password1.equals(password2)) {
            return "redirect:/store/admin/update/password?errorNotEqual";
        }

        if (password1.length() < 4) {
            return "redirect:/store/admin/update/password?errorSmallLength";
        }

        userService.updatePassword(authentication,password1);

        SecurityContextHolder.clearContext();
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/store/admin/login";
    }
}
