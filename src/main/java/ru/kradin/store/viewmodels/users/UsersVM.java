package ru.kradin.store.viewmodels.users;

import org.springframework.security.access.AccessDeniedException;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.exceptions.UserNotFoundException;
import ru.kradin.store.services.interfaces.AdminService;

import java.util.List;
import java.util.Map;

@VariableResolver(DelegatingVariableResolver.class)
public class UsersVM {
    private Window window;
    private List<UserDTO> nonFilteredUsers;
    private List<UserDTO> searchedUsers;
    private String keyWord = "";

    @WireVariable("adminServiceImp")
    private AdminService adminService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
        nonFilteredUsers = adminService.getUsers();
    }

    @Command("search")
    @NotifyChange("users")
    public void search() {
        if (keyWord.isEmpty() || keyWord.isBlank()) {
            searchedUsers = null;
        } else {
            searchedUsers = nonFilteredUsers.stream().filter(user -> {
                if (user.getUsername().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    user.getFirstName().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    user.getMiddleName().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    user.getLastName().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    user.getEmail().toLowerCase().contains(keyWord.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            }).toList();
        }
    }

    @Command("toggleBan")
    @NotifyChange("users")
    public void toggleBan(@BindingParam("username") String username) {
        try {
            adminService.toggleUserBan(username);
            nonFilteredUsers = adminService.getUsers();
            search();
            Messagebox.show("User ban successfully toggled","Message",Messagebox.OK,Messagebox.INFORMATION);
        } catch (UserNotFoundException e) {
            Messagebox.show("User not found","Error",Messagebox.OK,Messagebox.ERROR);
        } catch (AccessDeniedException e) {
            Messagebox.show(e.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
        }
    }

    @Command("getAdditionalInfo")
    public void getAdditionalInfo(@BindingParam("username") String username) {
        UserDTO user = getUsers().stream().filter(userDTO -> userDTO.getUsername().equals(username)).toList().get(0);
        Map<String,UserDTO> args = Map.of("user",user);
        Executions.createComponents("~./users/user.zul", window, args);
    }

    public String getButtonLabel(@BindingParam("accountNonLocked") boolean accountNonLocked) {
        if (accountNonLocked)
            return "ban";
        else
            return "unban";
    }

    public List<UserDTO> getUsers() {
        if (searchedUsers == null)
            return nonFilteredUsers;
        else
            return searchedUsers;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
