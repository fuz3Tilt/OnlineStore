package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;

public class CatalogEditDTO extends AbstractPersistable<Long> {
    private String name;
    private String imageURL;

    public void setId(Long id) {
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
}
