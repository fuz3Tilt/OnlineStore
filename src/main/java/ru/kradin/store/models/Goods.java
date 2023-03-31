package ru.kradin.store.models;

import jakarta.persistence.*;
import ru.kradin.store.models.enums.Status;

import java.util.Objects;

@Entity
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
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

    public Goods() {
    }
    //constructor for tests
    public Goods(String name, Catalog catalog) {
        this.name = name;
        description = "";
        imageName = "";
        status = Status.AVAILABLE;
        price = 0;
        this.catalog = catalog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Goods goods)) return false;
        return getId() == goods.getId() && getPrice() == goods.getPrice() && getName().equals(goods.getName()) && getDescription().equals(goods.getDescription()) && getImageName().equals(goods.getImageName()) && getStatus() == goods.getStatus() && getCatalog().equals(goods.getCatalog());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getImageName(), getStatus(), getPrice(), getCatalog());
    }
}
