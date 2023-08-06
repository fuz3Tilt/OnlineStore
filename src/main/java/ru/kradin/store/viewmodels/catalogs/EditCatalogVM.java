package ru.kradin.store.viewmodels.catalogs;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.CatalogEditDTO;
import ru.kradin.store.services.interfaces.AdminCatalogService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditCatalogVM {
    private Window window;
    private CatalogDTO editCatalog;
    private CatalogEditDTO catalog = new CatalogEditDTO();

    @WireVariable("catalogServiceImp")
    private AdminCatalogService adminCatalogService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("catalog") CatalogDTO editCatalog) {
        this.window = window;
        this.editCatalog = editCatalog;
        catalog.setId(editCatalog.getId());
        catalog.setName(editCatalog.getName());
        catalog.setImageURL(editCatalog.getImageURL());
    }

    @Command("close")
    public void close() {
        window.detach(); 
    }

    @Command("save")
    public void save() {
        if (catalog.getName().isBlank() || catalog.getImageURL().isBlank()) {
            Messagebox.show("Name and image url cannot be empty","Error",Messagebox.OK,Messagebox.ERROR);
        } else {
            adminCatalogService.update(catalog);
            Events.postEvent("onCatalogsChange", window, null);
            close();
        }
    }

    @Command("cancel")
    @NotifyChange("catalog")
    public void cancel() {
        catalog.setName(editCatalog.getName());
        catalog.setImageURL(editCatalog.getImageURL());
    }

    public CatalogEditDTO getcatalog() {
        return catalog;
    }

    public void setNewCatalog(CatalogEditDTO catalog) {
        this.catalog = catalog;
    }
}
