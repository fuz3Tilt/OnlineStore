package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class GoodQuantity extends AbstractPersistable<Long> {
    @OneToOne
    @JoinColumn(nullable = false)
    private Good good;
    @Column(nullable = false)
    private Long quantity;

    public GoodQuantity() {
    }

    public GoodQuantity(Good good, Long quantity) {
        this.good = good;
        this.quantity = quantity;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
