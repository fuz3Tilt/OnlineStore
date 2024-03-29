package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.CartDTO;
import ru.kradin.store.DTOs.GoodQuantityCreateDTO;

/**
 * Uses for management carts
 */
public interface CartService {
    public CartDTO getCurtOfCurrentUser();
    public void changeGoodQuantity(GoodQuantityCreateDTO goodQuantityCreateDTO);
    public void emptyCart();
}
