package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CartDTO;

public interface CartService {
    public CartDTO getCurtOfCurrentUser();
    public void changeGoodQuantity(Long goodId, Long quantity);
    public void emptyCart();
}
