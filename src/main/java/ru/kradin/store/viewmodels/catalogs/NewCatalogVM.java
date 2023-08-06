package ru.kradin.store.viewmodels.catalogs;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.CatalogCreateDTO;
import ru.kradin.store.services.interfaces.AdminCatalogService;

@VariableResolver(DelegatingVariableResolver.class)
public class NewCatalogVM {
    private Window window;
    private CatalogCreateDTO newCatalog = new CatalogCreateDTO();

    @WireVariable("catalogServiceImp")
    private AdminCatalogService adminCatalogService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
        newCatalog.setName("");
        newCatalog.setImageURL("");
    }

    @Command("close")
    public void close() {
        window.detach(); 
    }

    @Command("save")
    public void save() {
        if (newCatalog.getName().isBlank() || newCatalog.getImageURL().isBlank()) {
            Messagebox.show("Name and image url cannot be empty","Error",Messagebox.OK,Messagebox.ERROR);
        } else {
            adminCatalogService.create(newCatalog);
            Events.postEvent("onCatalogAdd", window, null);
            close();
        }
    }

    @Command("cancel")
    @NotifyChange("newCatalog")
    public void cancel() {
        newCatalog.setName("");
        newCatalog.setImageURL("");
    }

    public CatalogCreateDTO getNewCatalog() {
        return newCatalog;
    }

    public void setNewCatalog(CatalogCreateDTO newCatalog) {
        this.newCatalog = newCatalog;
    }
}
