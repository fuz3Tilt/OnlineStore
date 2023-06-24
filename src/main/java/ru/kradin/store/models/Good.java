package ru.kradin.store.models;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;
import ru.kradin.store.enums.Status;

@Entity
public class Good extends AbstractPersistable<Long> {
    @Column(nullable = false,unique = true,length = 50)
    private String name;
    @Column(nullable = false,length = 500)
    private String description;
    @Column(nullable = false)
    private String imageURL;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private long price;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private Catalog catalog;

    public Good() {
    }

    public Good(String name, String description, String imageURL, Status status, long price, Catalog catalog) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
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
        return imageURL;
    }

    public void setImageName(String imageURL) {
        this.imageURL = imageURL;
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
