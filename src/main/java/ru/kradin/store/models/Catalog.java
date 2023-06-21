package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
public class Catalog extends AbstractPersistable<Long> {
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false)
    private String imageUrl;
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Good> goodList;

    public Catalog() {
    }

    public Catalog(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Good> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Good> goodList) {
        this.goodList = goodList;
    }
}
