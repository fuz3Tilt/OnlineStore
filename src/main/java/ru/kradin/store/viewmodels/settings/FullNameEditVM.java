package ru.kradin.store.viewmodels.settings;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;
import ru.kradin.store.DTOs.UserDTO;
import ru.kradin.store.services.implementations.UserServiceImp;

@VariableResolver(DelegatingVariableResolver.class)
public class FullNameEditVM {
    private Window parentWindow;
    private UserDTO admin;
    /*
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Cannot be used via an interface in a ZK bean
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    @WireVariable
    private UserServiceImp userServiceImp;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.parentWindow = (Window) window.getParent().getParent().getParent().getParent().getParent();
        this.admin = userServiceImp.getCurrent();
    }

    @Command("save")
    public void save() {
        userServiceImp.updateFullName(admin.getFirstName(), admin.getMiddleName(), admin.getLastName());
        Events.postEvent("onAdminChange", parentWindow, null);
    }

    @Command("cancel")
    @NotifyChange("admin")
    public void cancel() {
        admin = userServiceImp.getCurrent();
    }

    public UserDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserDTO admin) {
        this.admin = admin;
    }
}
