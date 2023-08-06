package ru.kradin.store.viewmodels.goods;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.GoodCreateDTO;
import ru.kradin.store.services.interfaces.AdminGoodService;

@VariableResolver(DelegatingVariableResolver.class)
public class NewGoodVM {
    private Window window;
    private GoodCreateDTO newGood = new GoodCreateDTO();

    @WireVariable("goodServiceImp")
    private AdminGoodService adminGoodService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("catalogId") Long catalogId) {
        this.window = window;
        newGood.setName("");
        newGood.setDescription("");
        newGood.setImageURL("");
        newGood.setCatalogId(catalogId);
    }

    @Command("close")
    public void close() {
        window.detach(); 
    }

    @Command("save")
    public void save() {
        if (newGood.getName().isBlank()
            || 
            newGood.getImageURL().isBlank()
            || 
            newGood.getDescription().isBlank()
            || 
            newGood.getPrice() < 0
            ||
            newGood.getInStock() < 0) {
            Messagebox.show("Name and image url and desription cannot be empty. Numbers cannot be less than 0.","Error",Messagebox.OK,Messagebox.ERROR);
        } else {
            adminGoodService.create(newGood);
            Events.postEvent("onGoodsChange", window, null);
            close();
        }
    }

    @Command("cancel")
    @NotifyChange("newGood")
    public void cancel() {
        newGood.setName("");
        newGood.setDescription("");
        newGood.setImageURL("");
        newGood.setPrice(0);
        newGood.setInStock(0);
    }

    public GoodCreateDTO getNewGood() {
        return newGood;
    }

    public void setNewGood(GoodCreateDTO newGood) {
        this.newGood = newGood;
    }
}
