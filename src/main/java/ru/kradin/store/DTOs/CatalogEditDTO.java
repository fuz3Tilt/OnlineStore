package ru.kradin.store.DTOs;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.web.multipart.MultipartFile;

public class CatalogEditDTO extends AbstractPersistable<Long> {
    private String name;
    private MultipartFile image;

    public void setId(Long id) {
        super.setId(id);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
