package ru.kradin.store.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartGoodQuantity extends GoodQuantity{
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
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
