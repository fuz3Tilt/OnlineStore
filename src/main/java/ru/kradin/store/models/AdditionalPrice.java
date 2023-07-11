package ru.kradin.store.models;

import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class AdditionalPrice extends AbstractPersistable<Long> {
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private Long price;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Order order;

    public AdditionalPrice() {
    }

    public AdditionalPrice(String reason, Long price, Order order) {
        this.reason = reason;
        this.price = price;
        this.order = order;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
