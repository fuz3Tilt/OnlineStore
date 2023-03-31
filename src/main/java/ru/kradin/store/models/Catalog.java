package ru.kradin.store.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "name",nullable = false,unique = true, length = 50)
    private String name;
    @Column(name = "image_name",nullable = false)
    private String imageName;
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Goods> goodsList;

    public Catalog() {
    }
    //constructor for tests
    public Catalog(String name) {
        this.name = name;
        imageName = "";
        goodsList = new ArrayList<>();
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catalog catalog)) return false;
        return getId() == catalog.getId() && getName().equals(catalog.getName()) && getImageName().equals(catalog.getImageName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImageName());
    }
}
