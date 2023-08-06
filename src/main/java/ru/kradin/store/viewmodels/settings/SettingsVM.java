package ru.kradin.store.viewmodels.settings;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.services.interfaces.UserService;

@VariableResolver(DelegatingVariableResolver.class)
public class SettingsVM {
    private Window window;
    private UserDTO admin;

    @WireVariable("userServiceImp")
    private UserService userService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
        this.admin = userService.getCurrent();
    }

    @Command("logout")
    public void logout() {
        Executions.sendRedirect("/logout");
    }

    @Command("edit")
    public void edit() {
        Window newWindow = (Window) Executions.createComponents("~./settings/settingsEdit.zul",window,null);
        newWindow.addEventListener("onAdminChange", event -> {
            admin = userService.getCurrent();
            BindUtils.postNotifyChange(SettingsVM.this, "admin");
        });
    }
    public UserDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserDTO admin) {
        this.admin = admin;
    }
}
