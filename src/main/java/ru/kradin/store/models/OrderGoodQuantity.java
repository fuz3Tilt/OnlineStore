package ru.kradin.store.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class OrderGoodQuantity extends GoodQuantity{
    @ManyToOne(cascade = CascadeType.MERGE)
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
