package ru.kradin.store.DTOs;

public abstract class GoodQuantityDTO {
    private GoodDTO good;
    private Long quantity;

    public GoodDTO getGood() {
        return good;
    }

    public void setGood(GoodDTO good) {
        this.good = good;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
