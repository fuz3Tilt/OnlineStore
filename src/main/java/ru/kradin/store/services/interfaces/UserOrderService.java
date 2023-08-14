package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.GoodQuantityCreateDTO;
import ru.kradin.store.DTOs.OrderCreateDTO;
import ru.kradin.store.DTOs.OrderDTO;

import java.util.List;

/**
 * Uses for management orders of user
 */
public interface UserOrderService {
    public List<OrderDTO> getOrders();
    public void create(OrderCreateDTO orderCreateDTO);
    public void create(OrderCreateDTO orderCreateDTO, GoodQuantityCreateDTO goodQuantityCreateDTO);
}
