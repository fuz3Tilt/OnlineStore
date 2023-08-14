package ru.kradin.store.viewmodels.orders;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.enums.Status;
import ru.kradin.store.services.interfaces.AdminOrderService;

@VariableResolver(DelegatingVariableResolver.class)
public class OrderVM {
    private Window window;
    private OrderDTO order;
    private String trackCode;
    private int statusIndex = 0;
    private Status[] statusList;

    @WireVariable("orderServiceImp")
    private AdminOrderService adminOrderService;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("order") OrderDTO order) {
        this.window = window;
        this.order = order;
        trackCode = order.getTrackCode();
        statusIndex = order.getStatus().ordinal();
        statusList = Status.values();
    }

    @Command("close")
    public void close() {
        window.detach();
    }

    @Command("save")
    public void save() {
        adminOrderService.setStatus(statusList[statusIndex], order.getId());
        adminOrderService.setTrackCode(trackCode, order.getId());
        Events.postEvent("onOrdersChange", window, null);
    }

    @Command("cancel")
    public void cancel() {
        trackCode = order.getTrackCode();
        statusIndex = order.getStatus().ordinal();
    }

    public OrderDTO getOrder() {
        return order;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public Status[] getStatusList() {
        return statusList;
    }

    public int getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(int statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getSum() {
        long sum = order.getGoodQuantityList().stream().mapToLong(goodQuantity -> goodQuantity.getQuantity()*goodQuantity.getGood().getPrice()).sum();
        return "Sum: "+sum;
    }
}
