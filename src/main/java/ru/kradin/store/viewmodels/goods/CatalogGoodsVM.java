package ru.kradin.store.viewmodels.goods;

import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.GoodDTO;
import ru.kradin.store.services.interfaces.AdminGoodService;
import ru.kradin.store.viewmodels.catalogs.CatalogsVM;

@VariableResolver(DelegatingVariableResolver.class)
public class CatalogGoodsVM {
    private Window window;
    private List<GoodDTO> nonFilteredGoods;
    private List<GoodDTO> searchedGoods;
    private String keyWord = "";
    private CatalogDTO parentCatalog;

    @WireVariable("goodServiceImp")
    private AdminGoodService adminGoodService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("catalog") CatalogDTO parentCatalog) {
        this.window = window;
        this.parentCatalog = parentCatalog;
        this.nonFilteredGoods = parentCatalog.getGoodList();
    }

    @Command("close")
    public void close() {
        window.detach(); 
    }

    @Command("search")
    @NotifyChange("goods")
    public void search() {
        if (keyWord.isEmpty() || keyWord.isBlank()) {
            searchedGoods = null;
        } else {
            searchedGoods = nonFilteredGoods.stream().filter(good -> {
                if (good.getName().toLowerCase().contains(keyWord.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            }).toList();
        }
    }

    @Command("newGood")
    public void newGood() {
        Map<String, Long> args = Map.of("catalogId", parentCatalog.getId());
        Window newWindow = (Window) Executions.createComponents("~./goods/newGood.zul", window, args);
        newWindow.addEventListener("onGoodsChange", event -> {
            nonFilteredGoods = adminGoodService.getByCatalogId(parentCatalog.getId());
            search();
            BindUtils.postNotifyChange(CatalogGoodsVM.this, "goods");
        });
    }

    @Command("edit")
    public void edit(@BindingParam("goodId") Long goodId) {
        GoodDTO good = getGoods().stream().filter(goodDTO -> goodDTO.getId().equals(goodId)).toList().get(0);
        Map<String, GoodDTO> args = Map.of("good", good);
        Window newWindow = (Window) Executions.createComponents("~./goods/editGood.zul", window, args);
        newWindow.addEventListener("onGoodsChange", event -> {
            nonFilteredGoods = adminGoodService.getByCatalogId(parentCatalog.getId());
            search();
            BindUtils.postNotifyChange(CatalogGoodsVM.this, "goods");
        });
    }

    @Command("delete")
    @NotifyChange("goods")
    public void delete(@BindingParam("goodId") Long goodId) {
        adminGoodService.delete(goodId);
        nonFilteredGoods = adminGoodService.getByCatalogId(parentCatalog.getId());
        search();
    }

    public List<GoodDTO> getGoods() {
        if (searchedGoods == null)
            return nonFilteredGoods;
        else
            return searchedGoods;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public CatalogDTO getParentCatalog() {
        return parentCatalog;
    }
}
