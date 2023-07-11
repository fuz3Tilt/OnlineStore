package ru.kradin.store.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CartGoodQuantity extends GoodQuantity {
    @ManyToOne(cascade = CascadeType.MERGE)
    private Cart cart;

    public CartGoodQuantity() {
    }

    public CartGoodQuantity(Good good, Long quantity, Cart cart) {
        super(good, quantity);
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
