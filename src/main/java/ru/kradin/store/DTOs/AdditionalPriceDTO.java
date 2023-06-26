package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;

public class AdditionalPriceDTO extends AbstractPersistable<Long> {
    private String reason;
    private Long price;

    public void setId(Long id) {
        super.setId(id);
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
}
