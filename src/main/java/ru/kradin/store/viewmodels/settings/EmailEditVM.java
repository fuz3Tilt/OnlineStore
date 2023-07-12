package ru.kradin.store.viewmodels.settings;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;
import ru.kradin.store.services.interfaces.UserService;

@VariableResolver(DelegatingVariableResolver.class)
public class EmailEditVM {
    private Window parentWindow;
    private String newEmail = "";
    private Integer token = 0;

    @WireVariable("userServiceImp")
    private UserService userService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.parentWindow = (Window) window.getParent().getParent().getParent().getParent().getParent();
    }

    @Command("sendToken")
    public void sendToken() {
        if (newEmail.contains("@")) {
            userService.requestTokenForEmail(newEmail);
            Messagebox.show("Check your email", "Message", Messagebox.OK, Messagebox.INFORMATION);
        }
    }
    @Command("save")
    @NotifyChange("*")
    public void save() {
        try {
            userService.updateEmail(newEmail,token);
            cancel();
            Events.postEvent("onAdminChange", parentWindow, null);
            Messagebox.show("Email successfully updated", "Message", Messagebox.OK, Messagebox.INFORMATION);
        } catch (VerificationTokenNotFoundException e) {
            Messagebox.show("Invalid token","Error",Messagebox.OK,Messagebox.ERROR);
        }
    }
    @Command("cancel")
    @NotifyChange("*")
    public void cancel() {
        newEmail = "";
        token = 0;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }
}
