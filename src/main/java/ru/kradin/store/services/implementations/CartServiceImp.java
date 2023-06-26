package ru.kradin.store.services.implementations;

import ru.kradin.store.DTOs.CartDTO;
import ru.kradin.store.DTOs.GoodQuantityCreateDTO;
import ru.kradin.store.services.interfaces.CartService;

public class CartServiceImp implements CartService {
    @Override
    public CartDTO getCurtOfCurrentUser() {
        return null;
    }

    @Override
    public void changeGoodQuantity(GoodQuantityCreateDTO goodQuantityCreateDTO) {

    }

    @Override
    public void emptyCart() {

    }
}
