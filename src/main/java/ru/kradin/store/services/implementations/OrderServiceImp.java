package ru.kradin.store.services.implementations;

import ru.kradin.store.DTOs.GoodQuantityCreateDTO;
import ru.kradin.store.DTOs.OrderCreateDTO;
import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.enums.Status;
import ru.kradin.store.services.interfaces.OrderService;

import java.util.List;

public class OrderServiceImp implements OrderService {
    @Override
    public List<OrderDTO> getOrdersOfCurrentUser() {
        return null;
    }

    @Override
    public void create(OrderCreateDTO orderCreateDTO, Long cartId) {

    }

    @Override
    public void create(OrderCreateDTO orderCreateDTO, GoodQuantityCreateDTO goodQuantityCreateDTO) {

    }

    @Override
    public List<OrderDTO> getAll() {
        return null;
    }

    @Override
    public void setStatus(Status status, Long orderId) {

    }
}
