package ru.kradin.store.viewmodels.orders;

import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.CatalogDTO;
import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.services.interfaces.AdminOrderService;
import ru.kradin.store.viewmodels.catalogs.CatalogsVM;

@VariableResolver(DelegatingVariableResolver.class)
public class OrdersVM {
    private Window window;
    private List<OrderDTO> nonFilteredOrders;
    private List<OrderDTO> searchedOrders;
    private String keyWord = "";

    @WireVariable("orderServiceImp")
    private AdminOrderService adminOrderService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.window = window;
        this.nonFilteredOrders = adminOrderService.getAll();
    }

    @Command("search")
    @NotifyChange("goods")
    public void search() {
        if (keyWord.isEmpty() || keyWord.isBlank()) {
            searchedOrders = null;
        } else {
            searchedOrders = nonFilteredOrders.stream().filter(order -> {
                if (order.getUser().getUsername().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    order.getTrackCode().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    order.getAddress().toLowerCase().contains(keyWord.toLowerCase())
                        ||
                    order.getStatus().toString().toLowerCase().contains(keyWord.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            }).toList();
        }
    }

    @Command("open")
    public void open(@BindingParam("order") OrderDTO order) {
        Map<String, OrderDTO> args = Map.of("order", order);
        Window newWindow = (Window) Executions.createComponents("~./orders/order.zul", window, args);
        newWindow.addEventListener("onOrdersChange", event -> {
            nonFilteredOrders = adminOrderService.getAll();
            search();
            BindUtils.postNotifyChange(OrdersVM.this, "orders");
        });
    }

    public List<OrderDTO> getOrders() {
        if (searchedOrders == null)
            return nonFilteredOrders;
        else
            return searchedOrders;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
