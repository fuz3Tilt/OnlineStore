package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordr")
public class Order extends AbstractPersistable<Long> {
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<OrderGoodQuantity> goodQuantityList;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(List<OrderGoodQuantity> goodQuantityList, User user, String address, LocalDateTime createdAt) {
        this.goodQuantityList = goodQuantityList;
        this.user = user;
        this.address = address;
        this.createdAt = createdAt;
    }

    public List<OrderGoodQuantity> getGoodQuantityList() {
        return goodQuantityList;
    }

    public void setGoodQuantityList(List<OrderGoodQuantity> goodQuantityList) {
        this.goodQuantityList = goodQuantityList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
