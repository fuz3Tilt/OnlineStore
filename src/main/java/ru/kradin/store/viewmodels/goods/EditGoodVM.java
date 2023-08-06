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

import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.DTOs.GoodEditDTO;
import ru.kradin.store.services.interfaces.AdminGoodService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditGoodVM {
    private Window window;
    private GoodDTO editGood;
    private GoodEditDTO good = new GoodEditDTO();

    @WireVariable("goodServiceImp")
    private AdminGoodService adminGoodService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("good") GoodDTO editGood) {
        this.window = window;
        this.editGood = editGood;
        good.setId(editGood.getId());
        good.setName(editGood.getName());
        good.setDescription(editGood.getDescription());
        good.setImageURL(editGood.getDescription());
        good.setPrice(editGood.getPrice());
        good.setInStock(editGood.getInStock());
    }

    @Command("close")
    public void close() {
        window.detach(); 
    }

    @Command("save")
    public void save() {
        if (good.getName().isBlank()
            || 
            good.getImageURL().isBlank()
            || 
            good.getDescription().isBlank()
            || 
            good.getPrice() < 0
            ||
            good.getInStock() < 0) {
            Messagebox.show("Name and image url and desription cannot be empty. Numbers cannot be less than 0.","Error",Messagebox.OK,Messagebox.ERROR);
        } else {
            adminGoodService.update(good);
            Events.postEvent("onGoodsChange", window, null);
            close();
        }
    }

    @Command("cancel")
    @NotifyChange("good")
    public void cancel() {
        good.setId(editGood.getId());
        good.setName(editGood.getName());
        good.setDescription(editGood.getDescription());
        good.setImageURL(editGood.getDescription());
        good.setPrice(editGood.getPrice());
        good.setInStock(editGood.getInStock());
    }

    public GoodEditDTO getGood() {
        return good;
    }

    public void setGood(GoodEditDTO good) {
        this.good = good;
    }
}
