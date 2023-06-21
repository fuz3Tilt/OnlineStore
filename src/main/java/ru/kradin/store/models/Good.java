package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.Status;

@Entity
public class Good extends AbstractPersistable<Long> {
    @Column(name = "name",nullable = false,unique = true,length = 50)
    private String name;
    @Column(name = "description",nullable = false,length = 500)
    private String description;
    @Column(name = "image_name",nullable = false)
    private String imageName;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private Status status;
    @Column(name = "price",nullable = false)
    private long price;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    public Good(String name, String description, String imageName, Status status, long price, Catalog catalog) {
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        this.status = status;
        this.price = price;
        this.catalog = catalog;
    }

    public Long getId() {
        return super.getId();
    }

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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
