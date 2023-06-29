package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.OrderDTO;
import ru.kradin.store.enums.Status;

import java.util.List;

public interface AdminOrderService {
    public List<OrderDTO> getAll();
    public void setStatus(Status status, Long orderId);
    public void setTrackCode(String trackCode, Long orderId);
}
