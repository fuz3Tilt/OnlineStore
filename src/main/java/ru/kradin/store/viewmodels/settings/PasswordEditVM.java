package ru.kradin.store.viewmodels.settings;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import ru.kradin.store.exceptions.PasswordMismatchException;
import ru.kradin.store.services.implementations.UserServiceImp;

@VariableResolver(DelegatingVariableResolver.class)

public class PasswordEditVM {
    private String oldPassword = "";
    private String newPassword = "";
    private String passwordConfirm = "";
    /*
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Cannot be used via an interface in a ZK bean
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    @WireVariable
    private UserServiceImp userServiceImp;

    @Command("save")
    @NotifyChange("errorMessage")
    public void save() {
        try {
            userServiceImp.updatePassword(oldPassword, newPassword, passwordConfirm);
            Executions.sendRedirect("/logout");
        } catch (PasswordMismatchException e) {
            Messagebox.show(e.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
        }
    }

    @Command("cancel")
    @NotifyChange("*")
    public void cancel() {
        oldPassword = "";
        newPassword = "";
        passwordConfirm = "";
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
