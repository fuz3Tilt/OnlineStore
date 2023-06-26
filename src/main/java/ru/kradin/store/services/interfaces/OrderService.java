package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.OrderCreateDTO;
import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.enums.Status;

import java.util.List;

public interface OrderService {
    public OrderDTO getOrderOfCurrentUser();
    public void create(OrderCreateDTO orderCreateDTO, Long cartId);
    public void create(OrderCreateDTO orderCreateDTO, Long goodId, Long quantity);
    public List<OrderDTO> getAll();
    public void setStatus(Status status, Long orderId);
}
