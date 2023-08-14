package ru.kradin.store.viewmodels.catalogs;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;
import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.services.interfaces.AdminCatalogService;

import java.util.List;
import java.util.Map;

@VariableResolver(DelegatingVariableResolver.class)
public class CatalogsVM {
    private Window window;
    private List<CatalogDTO> nonFilteredCatalogs;
    private List<CatalogDTO> searchedCatalogs;
    private String keyWord = "";

    @WireVariable("catalogServiceImp")
    private AdminCatalogService adminCatalogService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
        this.nonFilteredCatalogs = adminCatalogService.getAll();
    }

    @Command("search")
    @NotifyChange("catalogs")
    public void search() {
        if (keyWord.isEmpty() || keyWord.isBlank()) {
            searchedCatalogs = null;
        } else {
            searchedCatalogs = nonFilteredCatalogs.stream().filter(catalog -> {
                if (catalog.getName().toLowerCase().contains(keyWord.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            }).toList();
        }
    }

    @Command("newCatalog")
    public void newCatalog() {
        Window newWindow = (Window) Executions.createComponents("~./catalogs/newCatalog.zul", window, null);
        newWindow.addEventListener("onCatalogsChange", event -> {
            nonFilteredCatalogs = adminCatalogService.getAll();
            search();
            BindUtils.postNotifyChange(CatalogsVM.this, "catalogs");
        });
    }

    @Command("edit")
    public void edit(@BindingParam("catalog") CatalogDTO catalog) {
        Map<String, CatalogDTO> args = Map.of("catalog", catalog);
        Window newWindow = (Window) Executions.createComponents("~./catalogs/editCatalog.zul", window, args);
        newWindow.addEventListener("onCatalogsChange", event -> {
            nonFilteredCatalogs = adminCatalogService.getAll();
            search();
            BindUtils.postNotifyChange(CatalogsVM.this, "catalogs");
        });
    }

    @Command("delete")
    @NotifyChange("catalogs")
    public void delete(@BindingParam("catalog") CatalogDTO catalog) {
        adminCatalogService.delete(catalog.getId());
        nonFilteredCatalogs.remove(catalog);
        search();
    }

    @Command("open")
    public void open(@BindingParam("catalog") CatalogDTO catalog) {
        Map<String, CatalogDTO> args = Map.of("catalog", catalog);
        Window newWindow = (Window) Executions.createComponents("~./goods/catalogGoods.zul", window, args);
        newWindow.addEventListener("onGoodsChange", event -> {
            nonFilteredCatalogs = adminCatalogService.getAll();
            search();
            BindUtils.postNotifyChange(CatalogsVM.this, "catalogs");
        });
    }

    public List<CatalogDTO> getCatalogs() {
        if (searchedCatalogs == null)
            return nonFilteredCatalogs;
        else
            return searchedCatalogs;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
