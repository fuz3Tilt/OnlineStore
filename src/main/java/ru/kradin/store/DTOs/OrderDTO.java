package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO extends AbstractPersistable<Long> {
    private List<OrderGoodQuantityDTO> goodQuantityList;
    private UserDTO user;
    private String address;
    private Integer postalCode;
    private String message;
    private List<AdditionalPriceDTO> additionalPriceList;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public void setId(Long id) {
        super.setId(id);
    }

    public List<OrderGoodQuantityDTO> getGoodQuantityList() {
        return goodQuantityList;
    }

    public void setGoodQuantityList(List<OrderGoodQuantityDTO> goodQuantityList) {
        this.goodQuantityList = goodQuantityList;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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

    public List<AdditionalPriceDTO> getAdditionalPriceList() {
        return additionalPriceList;
    }

    public void setAdditionalPriceList(List<AdditionalPriceDTO> additionalPriceList) {
        this.additionalPriceList = additionalPriceList;
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
