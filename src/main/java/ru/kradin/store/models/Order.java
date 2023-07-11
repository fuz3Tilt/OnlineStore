package ru.kradin.store.models;

import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordr")
public class Order extends AbstractPersistable<Long> {
    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<OrderGoodQuantity> goodQuantityList;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer postalCode;
    @Column(nullable = false)
    private String message;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<AdditionalPrice> additionalPriceList;
    private String trackCode;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime closedAt;

    public Order() {
    }

    public Order(User user, String address, Integer postalCode, String message, LocalDateTime createdAt) {
        this.user = user;
        this.address = address;
        this.postalCode = postalCode;
        this.message = message;
        this.status = Status.REGISTERED;
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

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AdditionalPrice> getAdditionalPriceList() {
        return additionalPriceList;
    }

    public void setAdditionalPriceList(List<AdditionalPrice> additionalPriceList) {
        this.additionalPriceList = additionalPriceList;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
}
