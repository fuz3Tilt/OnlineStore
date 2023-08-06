package ru.kradin.store.models;

import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Good extends AbstractPersistable<Long> {
    @Column(nullable = false,unique = true,length = 50)
    private String name;
    @Lob
    @Column(nullable = false)
    private String description;
    @Lob
    @Column(nullable = false)
    private String imageURL;
    @Column(nullable = false)
    private Long inStock;
    @Column(nullable = false)
    private long price;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Catalog catalog;

    public Good() {
    }

    public Good(String name, String description, String imageURL, Long inStock, long price, Catalog catalog) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.inStock = inStock;
        this.price = price;
        this.catalog = catalog;
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

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
