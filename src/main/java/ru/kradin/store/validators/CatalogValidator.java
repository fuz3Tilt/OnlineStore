package ru.kradin.store.validators;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class CatalogValidator {
    private int id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, max = 50, message = "Название должно быть в пределах 2-50 символов")
    private String name;
    private MultipartFile imageToUpload;
    private String imageName;

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

    public MultipartFile getImageToUpload() {
        return imageToUpload;
    }

    public void setImageToUpload(MultipartFile imageToUpload) {
        this.imageToUpload = imageToUpload;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof CatalogValidator that)) return false;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
