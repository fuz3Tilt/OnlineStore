package ru.kradin.store.DTOs;

public class GoodQuantityCreateDTO {
    private Long goodId;
    private Long quantity;

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
