package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

public class CatalogDTO extends AbstractPersistable<Long> {
    private String name;
    private String imageURL;
    private List<GoodDTO> goodList;

    public void setId(long id) {
        super.setId(id);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<GoodDTO> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodDTO> goodList) {
        this.goodList = goodList;
    }
}
