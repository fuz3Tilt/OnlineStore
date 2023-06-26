package ru.kradin.store.services.interfaces;

public interface AdditionalPriceService {
    public void add(String reason, Long price, Long orderId);
    public void remove(Long id);
}
