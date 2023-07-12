package ru.kradin.store.viewmodels.users;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.Window;
import ru.kradin.store.DTOs.UserDTO;

public class UserVM {
    private Window window;
    private UserDTO user;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("user") UserDTO user) {
        this.window = window;
        this.user = user;
    }

    @Command("close")
    public void close() {
        window.detach();
    }
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
