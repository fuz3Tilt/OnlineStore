package ru.kradin.store.DTOs;

import org.springframework.web.multipart.MultipartFile;
import ru.kradin.store.enums.Status;

public class GoodCreateDTO {
    private String name;
    private String description;
    private MultipartFile image;
    private Status status;
    private long price;
    private CatalogDTO catalog;

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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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

    public CatalogDTO getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogDTO catalog) {
        this.catalog = catalog;
    }
}