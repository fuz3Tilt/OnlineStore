package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
public class Catalog extends AbstractPersistable<Long> {
    @Column(name = "name",nullable = false,unique = true, length = 50)
    private String name;
    @Column(name = "image_name",nullable = false)
    private String imageName;
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Good> goodList;

    public Catalog(String name, String imageName) {
        this.name = name;
        this.imageName = imageName;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public List<Good> getGoodsList() {
        return goodList;
    }

    public void setGoodsList(List<Good> goodList) {
        this.goodList = goodList;
    }
}
