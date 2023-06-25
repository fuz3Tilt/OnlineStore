package ru.kradin.store.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderGoodQuantity extends GoodQuantity{
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Order order;

    public OrderGoodQuantity() {
    }

    public OrderGoodQuantity(Good good, Long quantity, Order order) {
        super(good, quantity);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
