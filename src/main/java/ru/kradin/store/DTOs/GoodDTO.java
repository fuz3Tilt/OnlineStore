package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;

public class GoodDTO extends AbstractPersistable<Long> {
    private String name;
    private String description;
    private String imageURL;
    private Long inStock;
    private long price;
    private CatalogDTO catalog;

    public void setId(Long id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getInStock() {
        return inStock;
    }

    public void setInStock(Long inStock) {
        this.inStock = inStock;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public CatalogDTO getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogDTO catalog) {
        this.catalog = catalog;
    }
}
