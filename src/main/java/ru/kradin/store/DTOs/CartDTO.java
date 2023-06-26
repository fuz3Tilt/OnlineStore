package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

public class CartDTO extends AbstractPersistable<Long> {
    List<CartGoodQuantityDTO> goodQuantityList;

    public void setId(Long id) {
        super.setId(id);
    }

    public List<CartGoodQuantityDTO> getGoodQuantityList() {
        return goodQuantityList;
    }

    public void setGoodQuantityList(List<CartGoodQuantityDTO> goodQuantityList) {
        this.goodQuantityList = goodQuantityList;
    }
}
