package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
public class Cart extends AbstractPersistable<Long> {
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<CartGoodQuantity> goodQuantityList;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, unique = true)
    private User user;

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    public List<CartGoodQuantity> getGoodQuantityList() {
        return goodQuantityList;
    }

    public void setGoodQuantityList(List<CartGoodQuantity> goodQuantityList) {
        this.goodQuantityList = goodQuantityList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
