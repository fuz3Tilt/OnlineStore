package ru.kradin.store.models;

import javax.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
public class Catalog extends AbstractPersistable<Long> {
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false)
    private String imageURL;
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Good> goodList;

    public Catalog() {
    }

    public Catalog(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
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

    public List<Good> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Good> goodList) {
        this.goodList = goodList;
    }
}
